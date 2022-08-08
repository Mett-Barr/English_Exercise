package com.example.english.ui.page.test

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.english.tool.isNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import kotlin.random.Random

//@Composable
//fun WebViewTest() {
//    val state = rememberWebViewState(url = "https://www.bbc.com/news/newsbeat-44124396")
//    WebView(state = state)
//}


@Composable
fun WebViewTest() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var webView: WebView? = null


    BackHandler {
        if (webView != null) {
            if (webView!!.canGoBack()) {
                webView!!.goBack()
            }
        } else {

        }
    }

    val color = remember {
        mutableStateOf(Color.Red)
    }

    fun urlTest(url: String) {
//        Log.d("!!", "urlTest: $url")

//        url.split("/", " ", ":").forEach {
//            Log.d("!!", "urlTest: $it")
//        }
//        if (isNumber(url.split("/", " ", ":").last().split("-").last())) {
//            color.value = Color.Green
//        } else color.value = Color.Red


        coroutineScope.launch(Dispatchers.IO) {
            val html = Jsoup.connect(url).get()
            if (html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6").isNotEmpty()) {
                color.value = Color.Green
            } else color.value = Color.Red

            Log.d("!!", "urlTest: ${html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6")}")
        }
    }


    Column(modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())) {

        Spacer(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = color.value))

        AndroidView(factory = {
            WebView(context).apply {
                webView = this
                webViewClient = WebViewClientTest { urlTest(it) }

                loadUrl("https://www.bbc.com/news/newsbeat-44124396")
            }
        })
    }
}

class WebViewClientTest(private val function: (String) -> Unit) : WebViewClient() {
//    override fun onLoadResource(view: WebView?, url: String?) {
//        super.onLoadResource(view, url)
//        if (url != null) {
//            function(url)
//        }
//    }
//
//    override fun onPageFinished(view: WebView?, url: String?) {
//        super.onPageFinished(view, url)
//        if (url != null) {
////            function(url)
//        }
//    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (url != null) {
            function(url)
        }
    }
}