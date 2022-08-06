package com.example.english.network

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

suspend fun imageStore(url: String, context: Context): Bitmap? {

    val request = coil.request.ImageRequest.Builder(context).data(url).build()

    val imageLoader = ImageLoader(context)

    return imageLoader.execute(request).drawable?.toBitmap()

//    ImageRequest(url, myResponseListener, maxWidth,
//        maxHeight, scaleType, Bitmap.Config.RGB_565, myErrorListener)
}


@Composable
fun TestJsoupImage() {
    var url by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            url = jsoupImageTest()
        }
    }

    AsyncImage(
        model = url,
        contentDescription = null
    )
}



@Composable
fun ImageTest() {
    var bitmap: Bitmap? = null
    val coroutineScope = rememberCoroutineScope()

    if (bitmap != null) {
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
    }

    LaunchedEffect(key1 = Unit ) {
        coroutineScope.launch {
//            bitmap = imageStore(, LocalContext.current)
        }
    }
}