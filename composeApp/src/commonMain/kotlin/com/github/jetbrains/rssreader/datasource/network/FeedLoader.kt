package com.github.jetbrains.rssreader.datasource.network

import com.github.jetbrains.rssreader.entity.Feed
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
class FeedLoader(
    private val httpClient: HttpClient
) {
    suspend fun getFeed(url: String, isDefault: Boolean): Feed {
        return httpClient.get(url).body()

        //TODO isDefault
    }
}