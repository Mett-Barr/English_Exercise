package com.example.english.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.english.MainViewModel
import com.example.english.ui.page.InsertPage
import com.example.english.ui.page.MainPage
import com.example.english.ui.page.NewsArticlePage

sealed class MainRoute(val route: String) {
    object Main : MainRoute("MainPage")
    object Insert : MainRoute("InsertPage")
    object News : MainRoute("NewsPage")
}

@Composable
fun MainNavigation(viewModel: MainViewModel) {

    val list by viewModel.list.collectAsState(initial = emptyList())

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainRoute.Main.route) {
        composable(MainRoute.Main.route) {
            MainPage(viewModel = viewModel, navController)
        }
        composable(MainRoute.Insert.route) {
            InsertPage(viewModel = viewModel, navController = navController)
        }
        composable(MainRoute.News.route) {
            NewsArticlePage(viewModel = viewModel, viewModel.currentTitle)
        }
    }
}