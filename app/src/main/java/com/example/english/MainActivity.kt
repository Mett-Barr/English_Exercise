package com.example.english

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.english.data.image.ImageOperatorObject
import com.example.english.network.imageStore
import com.example.english.ui.navigation.MainNavigation
import com.example.english.ui.page.WebPage
import com.example.english.ui.page.test.WebViewTest
import com.example.english.ui.theme.EnglishTheme
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        Log.d("!!!", uri.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            EnglishTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainNavigation(viewModel)


//                    WebPage()
//                    Test05(viewModel)
//                    TestJsoupImage()
//                    ImageTest()

//                    WebViewTest()
                }
            }
        }


        // Jsoup test
//        viewModel.addNewsByJsoup(url = "https://www.bbc.com/news/world-asia-62419858", this)

        // image store test
//        val imageUrl = "https://ichef.bbci.co.uk/news/976/cpsprodpb/FD2F/production/_126151846_sun.jpg"
//        lifecycleScope.launch(Dispatchers.IO) {
//            val bitmap = imageStore(imageUrl, this@MainActivity)
//            ImageOperatorObject.addImage("1", bitmap!!, this@MainActivity)
//        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("!!!", "onBackPressed: ")
    }


//    private fun init() {
//        // Create an English-German translator:
//        val options = TranslatorOptions.Builder()
//            .setSourceLanguage(TranslateLanguage.ENGLISH)
//            .setTargetLanguage(TranslateLanguage.CHINESE)
//            .build()
//        val englishGermanTranslator = Translation.getClient(options)
//
//        Log.d("!!!", "init fun")
//
//        var conditions = DownloadConditions.Builder()
//            .requireWifi()
//            .build()
//        englishGermanTranslator.downloadModelIfNeeded(conditions)
//            .addOnSuccessListener {
//                // Model downloaded successfully. Okay to start translating.
//                // (Set a flag, unhide the translation UI, etc.)
//
//                Log.d("!!!", "addOnSuccessListener")
//            }
//            .addOnFailureListener { exception ->
//                // Model couldn’t be downloaded or other internal error.
//                // ...
//                Log.d("!!!", exception.toString())
//            }
//
//        englishGermanTranslator.translate("\"Especially at a time when the COVID situation is still serious, IPs being swiftly revealed can effectively reduce the appearance of disgusting content from rumor-makers and rumor-spreaders,\" wrote user UltraScarry.\n")
//            .addOnSuccessListener { translatedText ->
//                // com.example.english.translation.format.Translation successful.
//                Log.d("!!!", "init: $translatedText")
//            }
//            .addOnFailureListener { exception ->
//                // Error.
//                // ...
//                Log.d("!!!", "error")
//            }
//
////        val translator = com.example.english.translation.format.Translation.getClient(options)
//        lifecycle.addObserver(englishGermanTranslator)
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