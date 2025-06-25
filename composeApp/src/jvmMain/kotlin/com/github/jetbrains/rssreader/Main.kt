package com.github.jetbrains.rssreader

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.jetbrains.rssreader.app.FeedStore
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

private val appModule = module {
    single { createAndroid(get<Context>(), BuildConfig.DEBUG) }
    single { FeedStore(get()) }
}

private fun initKoin() {
    startKoin {
        if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

        androidContext(this@App)
        modules(appModule)
    }
}

fun main() = application {
    KoinApplication.init()

    Window(
        onCloseRequest = ::exitApplication,
        title = "RSS reader",
    ) {
        RssReaderApp()
    }
}