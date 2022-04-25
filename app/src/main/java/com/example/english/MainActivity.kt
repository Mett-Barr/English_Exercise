package com.example.english

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.english.article.*
import com.example.english.stringconverter.StringConverter
import com.example.english.ui.components.CustomTextField
import com.example.english.ui.page.*
import com.example.english.ui.theme.EnglishTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnglishTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                    Article()
//                    TestPage01(list = viewModel.list.value)
//                    CustomTextField()

//                    MainPage(viewModel)
                    InsertPage()
//                    TestPage2()
                }
            }
        }

//        init()

        lifecycleScope.launch {
            susInit()
        }

//        viewModel.roomTest()
    }

    private fun init() {
        val fos = this.openFileOutput("text.txt", Context.MODE_PRIVATE)
        val string = "qwe asd zxc"
//        val charset = Charsets.

        // 寫檔，把string寫進去
//        fos.writer(Charsets.UTF_8)
        fos.write(string.toByteArray())
        fos.close()

        StringConverter().getParagraphsSize(this, "text.txt")
    }

    private suspend fun susInit() {
        coroutineScope {
//            saveFileTest(this@MainActivity)
            saveFileTest2(this@MainActivity)
            readFileTest(this@MainActivity)
//            Log.d("!!!", "fileExistTest " + (fileExistTest(this@MainActivity)).toString())

//            File(this@MainActivity.filesDir.path + "/default.txt").forEachLine {
//                Log.d("!!!", "susInit: $it")
//            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EnglishTheme {
        Greeting("Android")
    }
}