package com.example.english.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.data.newslist.room.News
import com.example.english.ui.theme.Typography
import kotlinx.coroutines.flow.Flow

@Composable
fun MainPage(viewModel: MainViewModel) {

    val list by viewModel.list.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
            }
        },
        modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 56.dp + 32.dp, start = 16.dp,top = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(list) {
                NewsItem(it)
            }
        }
    }
}

@Composable
fun NewsItem(news: News) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)) {
        Row {
            Column(modifier = Modifier
                .weight(1F)
                .padding(8.dp)) {
                Text(text = news.title + (10..99).random().toString(), maxLines = 2, style = Typography.h5)
                Text(text = news.caption, maxLines = 2, style = Typography.caption)
            }
            Spacer(modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .aspectRatio(1F)
                .fillMaxHeight()
                .background(Color.Magenta))
        }
    }
}