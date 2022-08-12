package com.example.english.ui.page

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.data.image.ImageOperator
import com.example.english.ui.navigation.MainRoute
import com.example.english.data.newslist.room.News
import com.example.english.ui.components.Movement
import com.example.english.ui.page.Obj.colorBottom
import com.example.english.ui.page.Obj.colorTop
import com.example.english.ui.theme.Typography

@Composable
fun MainPage(viewModel: MainViewModel, navController: NavController) {

    val context = LocalContext.current

    val list by viewModel.list.collectAsState(initial = emptyList())

//    val placeHolder = BitmapFactory.decodeResource(context.resources, R.drawable.darkgrayimage)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(
                    bottom = WindowInsets.systemBars.asPaddingValues()
                        .calculateBottomPadding()
                ),
                onClick = { navController.navigate(MainRoute.Insert.route) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp,
                bottom = WindowInsets.systemBars.asPaddingValues()
                    .calculateBottomPadding() + 8.dp + 80.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(list) {
                NewsCard(
                    it,
                    viewModel,
                    { navController.navigate(MainRoute.News.route) },
                    context
                )
            }
        }
    }
}

@Composable
fun NewsCard(
    news: News,
    viewModel: MainViewModel,
    navigation: () -> Unit,
    context: Context,
) {

    val isDownloading by remember {
        derivedStateOf() {
            viewModel.isDownloading == news.id
        }
    }

    val alpha by animateFloatAsState(targetValue = if (isDownloading) ContentAlpha.disabled else LocalContentAlpha.current)

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        bitmap = ImageOperator().getImage(news.id.toString(), context)
    }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {

        Card(
            modifier = Modifier
                .alpha(alpha)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(16.dp))
                .clickable(!isDownloading) {
                    viewModel.currentNews(news, context)
                    viewModel.currentImage = bitmap
                    navigation.invoke()
                }
//            .background(MaterialTheme.colors.background)
        ) {


            /**  brush animation  */

            val color by animateColorAsState(
                targetValue = if (bitmap == null) colorTop else colorBottom,
                animationSpec = tween(1500)
            )

            val brush by remember {
                derivedStateOf {
                    Brush.verticalGradient(listOf(colorTop, color))
                }
            }

            /**  --------------------  */

            Box {
                Crossfade(targetState = bitmap) {
                    if (it != null) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(16f / 9f)
                                .fillMaxWidth()
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .aspectRatio(16f / 9f)
                                .fillMaxWidth()
                                .background(Color.DarkGray)
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .background(brush = brush)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = news.title,
                        maxLines = 2,
                        style = Typography.h5,
                        modifier = Modifier.padding(top = 16.dp)
                    )
//                Text(text = news.caption, maxLines = 2, style = Typography.caption)
                }
            }
        }

        if (isDownloading) {
            Movement()
        } else {
            LaunchedEffect(key1 = Unit) {
                bitmap = ImageOperator().getImage(news.id.toString(), context)
            }
        }
    }
}

object Obj {
    val colorTop = Color(0f, 0f, 0f, 0f)
    val colorBottom = Color(38, 38, 38, 200)
    val brush = Brush.verticalGradient(colors = listOf(colorTop, colorBottom))
}
