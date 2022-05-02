package com.example.english

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.english.data.newsarticle.NewsFileOperatorService
import com.example.english.ui.navigation.MainNavigation
import com.example.english.ui.theme.EnglishTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
//            val systemUiController = rememberSystemUiController()
//            val useDarkIcons = MaterialTheme.colors.isLight
//            SideEffect {
//                // Update all of the system bar colors to be transparent, and use
//                // dark icons if we're in light theme
//                systemUiController.setSystemBarsColor(
//                    color = Color.Transparent,
//                    darkIcons = useDarkIcons
//                )
//
//                // setStatusBarsColor() and setNavigationBarsColor() also exist
//            }

            ProvideWindowInsets() {
                EnglishTheme {
                    Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background) {
                        MainNavigation(viewModel)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("!!!", "onBackPressed: ")
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    EnglishTheme {
//        Greeting("Android")
//    }
//}