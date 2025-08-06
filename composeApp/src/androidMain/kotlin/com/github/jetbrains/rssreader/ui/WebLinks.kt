package com.github.jetbrains.rssreader.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.context.KoinContext
import org.koin.java.KoinJavaComponent.inject

class AndroidWebLinks(val context: Context): WebLinks, KoinComponent {
    override fun openWebView(url: String) {
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
