package com.github.jetbrains.rssreader.core

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.xml.xml
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlConfig

@OptIn(ExperimentalXmlUtilApi::class)
internal fun IosHttpClient(withLog: Boolean) = HttpClient(Darwin) {
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "IosHttpClient", message = message)
            }
        }
    }
    install(ContentNegotiation) {
        xml(contentType = ContentType.Application.Rss, format = XML {
            defaultPolicy {
                unknownChildHandler = XmlConfig.IGNORING_UNKNOWN_CHILD_HANDLER
            }
        })
    }
}