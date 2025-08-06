package com.github.jetbrains.rssreader.ui

import java.awt.Desktop
import java.net.URI

class JvmWebLinks(): WebLinks {
    override fun openWebView(url: String){
        Desktop.getDesktop().browse(URI(url))
    }
}