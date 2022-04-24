package com.example.english.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.english.ui.theme.Typography

@Composable
fun MainPage(list: List<String>) {
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
            items(30) {
                NewsItem()
            }
        }
    }
}

@Composable
fun NewsItem() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)) {
        Row {
            Column(modifier = Modifier.weight(1F).padding(8.dp)) {
                Text(text = "Title", maxLines = 2, style = Typography.h6)
                Text(text = "caption\ncaption", maxLines = 2, style = Typography.caption)
            }
            Spacer(modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .aspectRatio(1F)
                .fillMaxHeight()
                .background(Color.Magenta))
        }
    }
}