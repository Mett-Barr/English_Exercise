package com.example.english.network

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.english.MainViewModel
import com.example.english.data.image.ImageOperator
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

    var image by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            jsoupImageTest().also {
                url = it
                image = imageStore(it, context)
            }
        }
    }

//    AsyncImage(
//        model = url,
//        contentDescription = null
//    )

//    if (image != null) {
    image?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null) }
//    }
}


@Composable
fun ImageTest() {
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    if (bitmap.value != null) {
        Image(bitmap = bitmap.value!!.asImageBitmap(), contentDescription = null)
    }

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            bitmap.value = ImageOperator().imageTest(context)
        }
    }
}