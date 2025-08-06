package com.github.jetbrains.rssreader.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.github.jetbrains.rssreader.domain.Item
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant

@Composable
fun PostList(
    modifier: Modifier,
    posts: List<Item>,
    listState: LazyListState,
    onClick: (Item) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        state = listState
    ) {
        itemsIndexed(posts) { i, post ->
            if (i == 0) Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            PostItem(post) { onClick(post) }
            if (i != posts.size - 1) Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun PostItem(
    item: Item,
    onClick: () -> Unit
) {
    val padding = 16.dp
    Box {
        Card( //todo check elevation
            shape = RoundedCornerShape(padding)
        ) {
            Column(
                modifier = Modifier.clickable(onClick = onClick)
            ) {
                Spacer(modifier = Modifier.size(padding))
                if(item.title != null) {
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.headlineSmall,
                        text = item.title
                    )
                }
                item.mediaContent?.url?.let { url ->
                    Spacer(modifier = Modifier.size(padding))
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        modifier = Modifier.height(180.dp).fillMaxWidth(),
                        contentDescription = null
                    )
                }
                item.description?.let { desc ->
                    Spacer(modifier = Modifier.size(padding))
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        text = desc
                    )
                }
                Spacer(modifier = Modifier.size(padding))
                item.pubDate?.let {
                    Text(
                        modifier = Modifier.padding(start = padding, end = padding),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        text = item.pubDate
                    )
                    Spacer(modifier = Modifier.size(padding))
                }
            }
        }
    }
}