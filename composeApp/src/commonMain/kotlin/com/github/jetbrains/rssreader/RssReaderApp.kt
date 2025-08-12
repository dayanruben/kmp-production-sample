package com.github.jetbrains.rssreader

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.ui.*
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssReaderApp(navController: NavHostController = rememberNavController()) {
    AppTheme {
        // Get current back stack entry
        val backStackEntry by navController.currentBackStackEntryAsState()
        // Get the name of the current screen
        val currentScreen = Screens.valueOf(
            backStackEntry?.destination?.route ?: Screens.Main.name
        )
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier.padding(
                        WindowInsets.systemBars
                            .only(WindowInsetsSides.Bottom)
                            .asPaddingValues()
                    ), hostState = snackbarHostState
                )
            },
            topBar = {
                RssFeedAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.Main.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = Screens.Main.name) {
                    MainScreen(
                        onEditClick = { navController.navigate(Screens.FeedList.name) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
                composable(route = Screens.FeedList.name) {
                    FeedListScreen()
                }
            }

            val store: FeedStore = koinInject<FeedStore>()
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
        }
    }
}