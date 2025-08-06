package com.github.jetbrains.rssreader.ui

//import android.content.Intent
//import android.net.Uri
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalMaterial3Api::class)
class MainScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val navigator = LocalNavigator.currentOrThrow
        val state by store.observeState().collectAsStateWithLifecycle()
        val webLinks: WebLinks = koinInject<WebLinks>()

        LaunchedEffect(Unit) {
            store.dispatch(FeedAction.Refresh(false))
        }
        PullToRefreshBox(
            isRefreshing = state.progress,
            onRefresh = { store.dispatch(FeedAction.Refresh(true)) },
            content = {
                MainFeed(
                    store = store,
                    onPostClick = { post ->
                        post.link?.let { url ->
                            webLinks.openWebView(url)
                        }
                    },
                    onEditClick = {
                        navigator.push(FeedListScreen())
                    })
            })
    }
}

class FeedListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        FeedList(store = store)
    }
}