package com.example.english.ui.page

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.english.MainViewModel
import com.example.english.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

//@Preview
@Composable
fun WebPage(viewModel: MainViewModel) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var currentUrl by remember {
        mutableStateOf("")
    }

    var isNewsPage by remember {
        mutableStateOf(false)
    }

    fun pageCheck(url: String) {

        currentUrl = url

        coroutineScope.launch(Dispatchers.IO) {
            val html = Jsoup.connect(url).get()
            isNewsPage = html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6").isNotEmpty()

            Log.d("!!", "urlTest: ${html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6")}")
        }
    }


    Scaffold(
        bottomBar = {
            Row(modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface.copy(alpha = 0.8f))
                .padding(8.dp)) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.out),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.forward),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO*/ }) {
                    val contentAlpha by animateFloatAsState(if (isNewsPage) LocalContentAlpha.current else ContentAlpha.disabled)
                    Icon(painter = painterResource(id = R.drawable.download),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(contentAlpha)
                            .clickable(isNewsPage) {
                                viewModel.addBBCNews(currentUrl, context)
                            },
                        tint = LocalContentColor.current)
                }
            }
        }
    ) {
        AndroidView(
            factory = {
                WebView(context).apply {
                    webViewClient = MyWebViewClient { pageCheck(it) }

                    loadUrl("https://www.bbc.com/news")

                    this.settings.javaScriptEnabled = true
                    this.settings.domStorageEnabled = true
                }
            })
    }
}

class MyWebViewClient(private val function: (String) -> Unit) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (url != null) {
            function(url)
        }
    }

}