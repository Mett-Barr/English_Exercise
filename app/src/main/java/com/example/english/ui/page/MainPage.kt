package com.example.english.ui.page

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.ui.navigation.MainRoute
import com.example.english.data.newslist.room.News
import com.example.english.ui.theme.Typography

@Composable
fun MainPage(viewModel: MainViewModel, navController: NavController) {

    val context = LocalContext.current

    val list by viewModel.list.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(MainRoute.Insert.route) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(list) {
                NewsItem(it, viewModel, navController, context)
            }
        }
    }
}

@Composable
fun NewsItem(news: News, viewModel: MainViewModel, navController: NavController, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable {
                navController.navigate(MainRoute.News.route)
                viewModel.currentNews(news, context)
            }
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(8.dp)
            ) {
                Text(
                    text = news.title,
                    maxLines = 2,
                    style = Typography.h5
                )
                Text(text = news.caption, maxLines = 2, style = Typography.caption)
            }
            Spacer(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .aspectRatio(1F)
                    .fillMaxHeight()
                    .background(Color.Magenta)
            )
        }
    }
}