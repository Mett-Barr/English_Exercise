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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.english.MainViewModel
import com.example.english.data.newslist.room.News
import com.example.english.ui.theme.Typography

sealed class MainRoute(val route: String) {
    object Main : MainRoute("MainPage")
    object Insert : MainRoute("InsertPage")
    object News : MainRoute("NewsPage")
}

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val list by viewModel.list.collectAsState(initial = emptyList())

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainRoute.News.route) {
        composable(MainRoute.Main.route) {
            MainPage(list = list, navController)
        }
        composable(MainRoute.Insert.route) {
            InsertPage(viewModel = viewModel, navController = navController)
        }
        composable(MainRoute.News.route){
            NewsArticlePage(viewModel = viewModel)
        }
    }
}

@Composable
fun MainPage(list: List<News>, navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(MainRoute.News.route) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = 56.dp + 32.dp,
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            ),
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(8.dp)
            ) {
                Text(
                    text = news.title + (10..99).random().toString(),
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