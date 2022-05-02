package com.example.english.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.english.MainViewModel
import com.example.english.ui.page.InsertPage
import com.example.english.ui.page.MainPage
import com.example.english.ui.page.NewsArticlePage
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController

sealed class MainRoute(val route: String) {
    object Main : MainRoute("MainPage")
    object Insert : MainRoute("InsertPage")
    object News : MainRoute("NewsPage")
}

@Composable
fun MainNavigation(viewModel: MainViewModel) {

    val navController = rememberNavController()

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        // setStatusBarsColor() and setNavigationBarsColor() also exist
    }

    NavHost(navController = navController,
        startDestination = MainRoute.Main.route,
        modifier = Modifier.padding(
            rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyTop = false,
                applyBottom = false
            ))) {
        composable(MainRoute.Main.route) {
            MainPage(viewModel = viewModel, navController)
        }
        composable(MainRoute.Insert.route) {
            InsertPage(viewModel = viewModel, navController = navController)
        }
        composable(MainRoute.News.route) {
            NewsArticlePage(viewModel = viewModel,
                viewModel.currentTitle,
                navController = navController)
        }
    }
}