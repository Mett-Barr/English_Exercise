package com.example.english.ui.page

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.NewsWebsite
import com.example.english.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

//@Preview
@Composable
fun WebPage(viewModel: MainViewModel, navController: NavController) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var currentUrl by remember {
        mutableStateOf("")
    }

    var isNewsPage by remember {
        mutableStateOf(false)
    }

    fun pageCheck(url: String) {

        Log.d("!!", "pageCheck: ")

        currentUrl = url

        coroutineScope.launch(Dispatchers.IO) {
            val html = Jsoup.connect(url).get()
            isNewsPage =
                html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6").isNotEmpty()

            Log.d("!!",
                "urlTest: ${html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6")}")
        }
    }


//    var webViewClient: WebViewClient? = null
//    var webView: WebView? = null
    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler {
        if (webView?.canGoBack() == true) webView?.goBack()
        else navController.popBackStack()
    }



    Scaffold(
//        modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues()),
        bottomBar = {
            Row(modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface.copy(alpha = 0.8f))
                .padding(8.dp)) {
                IconButton(onClick = {
                    Log.d("!!", "WebPage: ${webView?.canGoBack() ?: false}")
                    navController.popBackStack()
                }) {
                    Icon(painter = painterResource(id = R.drawable.out),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current)
                }
                IconButton(onClick = { webView?.goBack() },
                    enabled = webView?.canGoBack() ?: false) {
                    val contentAlpha by animateFloatAsState(if (webView?.canGoBack() == true) LocalContentAlpha.current else ContentAlpha.disabled)
                    Icon(painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(contentAlpha),
                        tint = LocalContentColor.current)
                }
                IconButton(onClick = { webView?.goForward() },
                    enabled = webView?.canGoForward() ?: false) {
                    val contentAlpha by animateFloatAsState(if (webView?.canGoForward() == true) LocalContentAlpha.current else ContentAlpha.disabled)
                    Icon(painter = painterResource(id = R.drawable.forward),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(contentAlpha),
                        tint = LocalContentColor.current)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { viewModel.addBBCNews(currentUrl, context) },
                    enabled = isNewsPage) {
                    val contentAlpha by animateFloatAsState(if (isNewsPage) LocalContentAlpha.current else ContentAlpha.disabled)
                    Icon(painter = painterResource(id = R.drawable.download),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(contentAlpha),
                        tint = LocalContentColor.current)
                }
            }
        }
    ) {

//        LazyColumn {
//            item {
//                Spacer(modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()))
//            }
//            item {
//                AndroidView(
////            modifier = Modifier.padding(it),
//                    factory = {
//                        WebView(context).apply {
//                            webViewClient = MyWebViewClient { pageCheck(it) }
//
////                    loadUrl(viewModel.currentWebsite.url)
//                            loadUrl(NewsWebsite.BBC.url)
//
//                            this.settings.javaScriptEnabled = true
//                            this.settings.domStorageEnabled = true
//
////                    Log.d("!!", "WebPage: $webView")
//                            webView = this
////                    Log.d("!!", "WebPage: $webView")
//                        }
//                    })
//            }
//            item {
//                Spacer(modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()))
//            }
//        }
        Column(Modifier
            .wrapContentSize()
            .verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(WindowInsets.systemBars
                    .asPaddingValues()
                    .calculateTopPadding()))
            AndroidView(
//            modifier = Modifier.padding(it),
                factory = {
                    WebView(context).apply {
                        webViewClient = MyWebViewClient { pageCheck(it) }

//                    loadUrl(viewModel.currentWebsite.url)
                        loadUrl(NewsWebsite.BBC.url)

                        this.settings.javaScriptEnabled = true
                        this.settings.domStorageEnabled = true

//                    Log.d("!!", "WebPage: $webView")
                        webView = this
//                    Log.d("!!", "WebPage: $webView")
                    }
                })
            Spacer(modifier = Modifier
                .background(Color.Black)
                .padding(WindowInsets.navigationBars.asPaddingValues()))
        }
    }
}

class MyWebViewClient(private val function: (String) -> Unit) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (url != null) {
            function(url)
        }

        Log.d("!!", "onPageStarted: ")
        super.onPageStarted(view, url, favicon)
    }


    // 避免預設瀏覽器開啟連結
    @Deprecated("Deprecated in Java", ReplaceWith("super.shouldOverrideUrlLoading(view, url)",
        "android.webkit.WebViewClient"))
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }
}