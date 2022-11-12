package com.example.english.ui.navigation

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.english.MainViewModel
import com.example.english.ui.page.InsertPage
import com.example.english.ui.page.MainPage
import com.example.english.ui.page.ArticlePage
import com.example.english.ui.page.WebPage
import com.google.accompanist.systemuicontroller.rememberSystemUiController

sealed class MainRoute(val route: String) {
    object Main : MainRoute("MainPage")
    object Insert : MainRoute("InsertPage")
    object News : MainRoute("NewsPage")
    object Dictionary : MainRoute("DictionaryPage")
    object Website : MainRoute("WebsitePage")
}

@Composable
fun MainNavigation(viewModel: MainViewModel) {

    val navController = rememberNavController()

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.apply {
            setNavigationBarColor(
                color = Color.Transparent,
//                darkIcons = useDarkIcons
            )
            setStatusBarColor(
//                color = Color.Transparent,
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
        // setStatusBarsColor() and setNavigationBarsColor() also exist
    }

    fun navigateToWebPage(url: String) {
        viewModel.webPageUrl = url
        navController.navigate(MainRoute.Website.route)
    }


    NavHost(
        navController = navController,
//        startDestination = MainRoute.Website.route,
        startDestination = MainRoute.Main.route,
//        modifier = Modifier.systemBarsPadding()
    ) {
        composable(MainRoute.Main.route) {
            MainPage(viewModel = viewModel, navController) { navigateToWebPage(it) }
        }
        composable(MainRoute.Insert.route) {
//            viewModel.navToInsertPage()
            InsertPage(viewModel = viewModel, navController = navController)
//            WebPage(viewModel)
        }
        composable(MainRoute.News.route) {
            if (viewModel.currentContent.isEmpty()) {
                (LocalContext.current as Activity).apply {
                    LaunchedEffect(Unit) {
                        val intent = intent.apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
//            this.finish()
                        Log.d("!!!", "startActivity ")
                        startActivity(intent)
                    }
                }
            }
            else {

                ArticlePage(
                    viewModel = viewModel,
                    viewModel.currentTitle,
                    navController = navController
                ) {
                    navigateToWebPage(it)
                }
            }

        }
        composable(MainRoute.Dictionary.route) {

        }
        composable(MainRoute.Website.route) {
            WebPage(viewModel = viewModel, navController)
        }
    }


    val lifecycle  = LocalLifecycleOwner.current
    ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycle)

    Log.d("!!!", "MainNavigation: ${viewModel.currentNewsSize}")
}