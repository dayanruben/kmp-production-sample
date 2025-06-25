package com.github.jetbrains.rssreader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.ui.AppTheme
import com.github.jetbrains.rssreader.ui.MainScreen
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.compose.koinInject

@Composable
fun RssReaderApp() {
    AppTheme {
        val store: FeedStore = koinInject<FeedStore>()
        val snackbarHostState = remember { SnackbarHostState() }

        val error = store.observeSideEffect()
            .filterIsInstance<FeedSideEffect.Error>()
            .collectAsState(null)
        LaunchedEffect(error.value) {
            error.value?.let {
                snackbarHostState.showSnackbar(
                    it.error.message.toString()
                )
            }
        }

        Box(
            modifier = Modifier.safeContentPadding().fillMaxSize()
        )
        {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        modifier = Modifier.padding(
                            WindowInsets.systemBars
                                .only(WindowInsetsSides.Bottom)
                                .asPaddingValues()
                        ), hostState = snackbarHostState
                    )
                }) { contentPadding ->
                Column(modifier = Modifier.padding(contentPadding)) {
                    Navigator(MainScreen())
                }
            }
        }
    }
}