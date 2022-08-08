package com.example.english.ui.page

import android.content.res.Resources
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.english.R
import com.example.english.ui.page.test.WebViewClientTest

@Preview
@Composable
fun WebPage() {

    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            Row(modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface)
                .padding(8.dp)) {
                androidx.compose.material3.IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.out), contentDescription = null, modifier = Modifier.size(24.dp))
                }
                androidx.compose.material3.IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                }
                androidx.compose.material3.IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.forward), contentDescription = null, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { /*TODO*/ }) {
                    Text(Resources.getSystem().getString(android.R.string.ok))
                }
            }
        }
    ) {
        AndroidView(
            factory = {
            WebView(context).apply {
                webViewClient = WebViewClient()

                loadUrl("https://www.bbc.com/news/newsbeat-44124396")
            }
        })
    }
}