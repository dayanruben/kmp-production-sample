package com.github.jetbrains.rssreader.ui

import io.ktor.http.Url
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent

interface WebLinks{
    fun openWebView(url: String)
}