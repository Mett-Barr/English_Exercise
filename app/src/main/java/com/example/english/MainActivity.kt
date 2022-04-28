package com.example.english

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.english.data.newsarticle.NewsFileOperatorService
import com.example.english.ui.navigation.MainNavigation
import com.example.english.ui.theme.EnglishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnglishTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainNavigation(viewModel)
                }
            }
        }
    }

    fun fileService() {
        val intent = Intent(this, NewsFileOperatorService::class.java)

    }

//    private fun init() {
//        val fos = this.openFileOutput("text.txt", Context.MODE_PRIVATE)
//        val string = "qwe asd zxc"
////        val charset = Charsets.
//
//        // 寫檔，把string寫進去
////        fos.writer(Charsets.UTF_8)
//        fos.write(string.toByteArray())
//        fos.close()
//
//        StringConverter().getParagraphsSize(this, "text.txt")
//    }
//
//    private suspend fun susInit() {
//        coroutineScope {
////            saveFileTest(this@MainActivity)
//            saveFileTest2(this@MainActivity)
//            readFileTest(this@MainActivity)
////            Log.d("!!!", "fileExistTest " + (fileExistTest(this@MainActivity)).toString())
//
////            File(this@MainActivity.filesDir.path + "/default.txt").forEachLine {
////                Log.d("!!!", "susInit: $it")
////            }
//        }
//    }


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