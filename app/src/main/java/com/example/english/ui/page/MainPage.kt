package com.example.english.ui.page

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.NewsWebsite
import com.example.english.R
import com.example.english.data.image.ImageOperator
import com.example.english.data.newslist.room.News
import com.example.english.translation.BUG_URL
import com.example.english.ui.components.Movement
import com.example.english.ui.navigation.MainRoute
import com.example.english.ui.page.Obj.colorBottom
import com.example.english.ui.page.Obj.colorTop
import com.example.english.ui.theme.Typography
import kotlinx.coroutines.coroutineScope

@Composable
fun MainPage(viewModel: MainViewModel, navController: NavController) {

    val context = LocalContext.current

    val list by viewModel.list.collectAsState(initial = emptyList())


    /**     FAB animation      */
    var fabIsOpening by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = fabIsOpening, label = "fab state")

    val fabRotateAnimation by transition.animateFloat(transitionSpec = {
        if (false isTransitioningTo true) spring(0.5f, Spring.StiffnessLow)
        else spring()
    }, label = "fabRotateAnimation") { if (it) -135f else 0f }

    val fabColorAnimation by transition.animateColor(label = "fabColorAnimation") {
        if (it) Color(150, 150, 150) else MaterialTheme.colors.secondary
    }

    val fabAlphaAnimation by transition.animateFloat(transitionSpec = {
        if (false isTransitioningTo true) spring(Spring.DampingRatioNoBouncy, Spring.StiffnessLow)
        else spring()
    }, label = "fabAlphaAnimation") { if (it) 1f else 0f }

    val fabPaddingAdd by transition.animateDp(label = "fabPaddingAnimation") {
        if (it) 144.dp else 128.dp
    }

    val fabPaddingBBC by transition.animateDp(label = "fabPaddingAnimation") {
        if (it) 72.dp else 56.dp
    }

    val fabScaleAnimation by transition.animateFloat(label = "fabScaleAnimation") {
        if (it) 1f else 0f
    }
    /**      FAB animation      */

    Scaffold(
        floatingActionButton = {
            Box(modifier = Modifier
                .padding(
                    bottom = WindowInsets.systemBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                )
                .width(IntrinsicSize.Max)) {


                // Insert
                Row(modifier = Modifier
                    .alpha(fabAlphaAnimation)
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .padding(bottom = fabPaddingAdd)) {
//                    .padding(bottom = 144.dp)) {
                    Text(text = "新增", modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically), textAlign = TextAlign.Left)
//                    Spacer(modifier = Modifier.weight(1f))
                    FloatingActionButton(
                        modifier = Modifier.scale(fabScaleAnimation),
//                            .align(Alignment.BottomEnd),
                        onClick = {
                        navController.navigate(MainRoute.Insert.route)
//                    viewModel.addBBCNews(BUG_URL, context)
                            fabIsOpening = !fabIsOpening
                        }) {
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "add")
                    }
                }


                // BBC
                Row(modifier = Modifier
                    .alpha(fabAlphaAnimation)
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .padding(bottom = fabPaddingBBC)) {
//                    .padding(bottom = 72.dp)) {
                    Text(text = "BBC", modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically), textAlign = TextAlign.Left)
//                    Spacer(modifier = Modifier.weight(1f))
                    FloatingActionButton(
                        modifier = Modifier.scale(fabScaleAnimation), backgroundColor = Color.White,
//                            .align(Alignment.BottomEnd),
                        onClick = {
                            viewModel.currentWebsite = NewsWebsite.BBC
                            navController.navigate(MainRoute.Website.route)
//                            viewModel.addBBCNews(BUG_URL, context)
//                            fabIsOpening = !fabIsOpening
                        }) {
                        Icon(painter = painterResource(id = R.drawable.bbc),
                            contentDescription = "add",
//                        modifier = Modifier.size(24.dp),
                            tint = Color.Black)
                    }
                }



                Row(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)) {
                    Text(text = "取消", modifier = Modifier
                        .alpha(fabAlphaAnimation)
                        .weight(1f)
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically), textAlign = TextAlign.Left)
//                    Spacer(modifier = Modifier.weight(1f))
                    FloatingActionButton(
                        modifier = Modifier,
//                            .align(Alignment.BottomEnd),
                        backgroundColor = fabColorAnimation,
                        onClick = {
//                        navController.navigate(MainRoute.Insert.route)
//                    viewModel.addBBCNews(BUG_URL, context)
                            fabIsOpening = !fabIsOpening
                        }) {
                        Icon(imageVector = Icons.Rounded.Add,
                            contentDescription = "add",
                            modifier = Modifier.rotate(fabRotateAnimation),
                            tint = Color.Black)
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {

        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .alpha((1f - fabAlphaAnimation) / 2 + 0.5f)
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp,
                bottom = WindowInsets.systemBars.asPaddingValues()
                    .calculateBottomPadding() + 8.dp + 80.dp,
            ),
            reverseLayout = true,
            state = state,
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

        val modifier = if (fabIsOpening) Modifier.clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            fabIsOpening = false
        } else Modifier


        val spacerAlphaAnimation by transition.animateColor(label = "spacerAlphaAnimation") {
            if (it) Color.Black.copy(alpha = ContentAlpha.disabled) else Color.Transparent
        }

        Spacer(modifier = modifier
            .fillMaxSize()
            .background(spacerAlphaAnimation))
//            .background(if (fabIsOpening) Color.Black.copy(alpha = ContentAlpha.disabled) else Color.Transparent))


        if (list.isNotEmpty()) {
            LaunchedEffect(viewModel.isDownloading) {
                if (viewModel.isDownloading.isEmpty()) {
                    coroutineScope {
                        state.animateScrollToItem(list.size)
                    }
                } else {
                    coroutineScope {
//                        state.scrollToItem()
                    }
                }
            }

//            LaunchedEffect(Unit) {
//                coroutineScope {
//                    state.animateScrollToItem(list.size)
//                }
//            }
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
            viewModel.isDownloading.contains(news.id)
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

                if (news.progress < 100) {
                    Text(text = news.progress.toString() + "%",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp))
                } else {
                    Icon(painter = painterResource(id = R.drawable.done_broad),
                        contentDescription = "done",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp))
                }

                Column(
                    modifier = Modifier
                        .drawBehind { drawRect(brush) }
//                        .background(brush = brush)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = news.title,
//                        text = "${news.id}  $isDownloading",
                        maxLines = 2,
                        style = Typography.h5,
                        modifier = Modifier.padding(top = 16.dp)
                    )
//                Text(text = news.caption, maxLines = 2, style = Typography.caption)
                }
            }
        }

        Crossfade(targetState = isDownloading) {
            if (it) {
                Movement()
            } else {
                LaunchedEffect(key1 = Unit) {
                    bitmap = ImageOperator().getImage(news.id.toString(), context)
                }
            }
        }
    }
}

object Obj {
    val colorTop = Color(0f, 0f, 0f, 0f)
    val colorBottom = Color(30, 30, 30, 200)
//    val colorBottom = Color(20, 20, 20, 200)
    val brush = Brush.verticalGradient(colors = listOf(colorTop, colorBottom))
}
