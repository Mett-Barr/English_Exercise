package com.example.english.ui.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.data.word.word.room.EmptyWord
import com.example.english.data.word.word.room.Word
import com.example.english.isDone
import com.example.english.ui.components.ClickableIcon
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.SelectableIcon
import com.example.english.ui.components.WordComponent
import com.example.english.ui.components.test.PopupInfo
import com.example.english.ui.theme.ColorDone
import com.example.english.ui.theme.PrimaryVariant
import com.example.english.ui.theme.TextBackgroundAlphaLight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


enum class AnnotationState {
    TRANSLATION, WORDS, CLOSE
}

//enum class ReadMode(val position: Float) {
//    CARD_VIEW(-1F), ARTICLE_VIEW(1F)
//}

const val ratio = 12f

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ArticlePage(viewModel: MainViewModel, title: String, navController: NavController) {
//    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidth.toPx() }
//    val imageHeight = 16f / ratio * screenWidth.value
    val imageHeight = screenWidth / 16 * ratio
    val imageHeightPx = with(LocalDensity.current) { (screenWidth / 16 * ratio).toPx() }
    val range = screenWidth / 16 * (ratio - 9)


    // Popup Info
    var infoCardIsOpening by remember { mutableStateOf(false) }
    val maskColor by animateColorAsState(targetValue = if (infoCardIsOpening) MaterialTheme.colors.background.copy(
        alpha = 0.8f) else Color.Transparent, animationSpec = tween(300))


    // system bar

    val naviBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    val lazyListState = rememberLazyListState()

//    val systemUiController = rememberSystemUiController()
//    val useDarkIcons = MaterialTheme.colors.isLight

//    systemUiController.setStatusBarColor(color = Color.Transparent)

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding().toPx() * 2
    }

    val scrollOffset by remember { derivedStateOf { if (lazyListState.firstVisibleItemIndex == 0) imageHeightPx - lazyListState.firstVisibleItemScrollOffset.toFloat() else 0f } }

    val statusBarAlpha by remember {
        derivedStateOf {
            val alpha = if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset <= statusBarHeight * 2) {
                lazyListState.firstVisibleItemScrollOffset / (statusBarHeight * 2) / 5 * 4
            } else 0.8f
            if (maskColor.alpha > 0) {
                if (maskColor.alpha > alpha) 0f
                else alpha - maskColor.alpha
            } else alpha


//            if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset <= statusBarHeight) {
//                lazyListState.firstVisibleItemScrollOffset / statusBarHeight / 2
//            } else 0.5f
        }
    }

    val backgroundColor = MaterialTheme.colors.background
    fun getColor(): Color {
        return backgroundColor.copy(alpha = statusBarAlpha)
    }

    val color = remember { Animatable(getColor()) }

    fun changeColor() {
        scope.launch {
            if (infoCardIsOpening) color.animateTo(Color.Transparent)
            else color.animateTo(Color.Transparent.copy(alpha = statusBarAlpha))
        }
    }



    // data
    val progress by remember(viewModel.currentContent) {
        derivedStateOf {
            var done = 0
            viewModel.currentContent.forEach { if (it.text.isDone()) done++ }

            val progress = (done * 100 / viewModel.currentContent.size)

            progress
        }
    }



//    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    val hideKeyboardModifier = Modifier.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    fun getRid(boolean: Boolean): Int =
        if (boolean) R.drawable.arrow_less else R.drawable.arrow_more

    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    fun popBack() {
        focusManager.clearFocus()
        viewModel.updateProgress()
        viewModel.saveCurrentFile(context)
        navController.popBackStack()
    }

    BackHandler {
        popBack()
        Log.d("!!!", "NewsArticlePage: BackHandler")
    }


    // Dialog State
    var deleteParagraphDialog by remember {
        mutableStateOf(false)
    }

    var deleteArticleDialog by remember {
        mutableStateOf(false)
    }

//    var editTagDialog by remember {
//        mutableStateOf(false)
//    }
//
//    var editTitleDialog by remember {
//        mutableStateOf(false)
//    }


    var currentParagraphIndex by remember {
        mutableStateOf(0)
    }

    var currentParagraphContent by remember {
        mutableStateOf("")
    }


    val brush = Brush.verticalGradient(
        listOf(
            Color(0f, 0f, 0f, 0f),
            MaterialTheme.colors.surface.copy(alpha = 1f)
        )
    )

    fun dp(offset: Int): Dp = with(density) {
        offset.toDp()
    }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            while (true) {
                delay(1000)
                Log.d("!!", "NewsArticlePage: $naviBarPadding")
            }
        }
    }

    /** SegmentedControls */
//    val width = 96.dp
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { 48.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    val readMode by remember { derivedStateOf { swipeableState.currentValue } }
    val animateModifier by remember { derivedStateOf { if (readMode == 0) Modifier else Modifier.animateContentSize() } }
//    val transition = updateTransition(targetState = readMode, label = "readMode content alpha animation")
    val alphaAnimation by animateFloatAsState(targetValue = if (readMode == 0) 2f else 0f)
//    val alphaAnimation by animateFloatAsState(targetValue = if(readMode == 0) 2f else 0f, spring(stiffness = 3f))
    val cardViewAlpha by remember { derivedStateOf { if (alphaAnimation < 1f) 0f else alphaAnimation - 1f } }
    val cardViewSizeModifier by remember { derivedStateOf { if (alphaAnimation < 1f) Modifier.size(1.dp) else Modifier.wrapContentHeight() } }
    val articleViewAlpha by remember { derivedStateOf { if (alphaAnimation > 1f) 0f else 1f - alphaAnimation } }
    val articleViewSizeModifier by remember {
        derivedStateOf {
            if (alphaAnimation > 1f) Modifier.size(
                1.dp
            ) else Modifier.wrapContentHeight()
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Row(
                    modifier = Modifier
//                    .wrapContentHeight()
//                    .height(64.dp + naviBarPadding)
//                    .padding(bottom = naviBarPadding)
                        .padding(4.dp)
//                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        ClickableIcon(painter = painterResource(R.drawable.back)) {
//                            dispatcher.onBackPressed()
                            popBack()
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                    )
//                    ClickableIcon(painter = painterResource(R.drawable.delete), enabled = true,
//                        tint = MaterialTheme.colors.error) {
//                        deleteArticleDialog = true
//                    }

                    ClickableIcon(painter = painterResource(R.drawable.info), enabled = true) {
//                        deleteArticleDialog = true
                        infoCardIsOpening = !infoCardIsOpening
                    }

                    // change to info icon
//                    ClickableIcon(painter = painterResource(R.drawable.lable), enabled = false)


                    /** SegmentedControls */
                    val contentColor =
                        MaterialTheme.colors.contentColorFor(MaterialTheme.colors.surface)

                    fun gestureColor(state: Float): Color {
                        val rangeState = if (state > 1f) 1f else if (state < 0) 0f else state
                        return Color(
                            red = PrimaryVariant.red * rangeState + (1 - rangeState) * contentColor.red,
                            green = PrimaryVariant.green * rangeState + (1 - rangeState) * contentColor.green,
                            blue = PrimaryVariant.blue * rangeState + (1 - rangeState) * contentColor.blue
                        )
                    }

                    val cardViewTint by remember { derivedStateOf { gestureColor(1 - swipeableState.offset.value / sizePx) } }
                    val articleView by remember { derivedStateOf { gestureColor(swipeableState.offset.value / sizePx) } }

                    val color = LocalElevationOverlay.current!!.apply(
                        MaterialTheme.colors.surface,
                        LocalAbsoluteElevation.current + 50.dp
                    )


                    Box(
                        modifier = Modifier
//                        .clickable { horizontalBias *= -1 }
                            .clip(CircleShape)
//                        .background(Color.DarkGray)
                            .background(color)
                            .wrapContentSize()
                            .height(intrinsicSize = IntrinsicSize.Min)
                            .swipeable(
                                state = swipeableState,
                                anchors = anchors,
                                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                                orientation = Orientation.Horizontal
                            )
                    ) {

                        fun swipeAnimate(position: Int) {
//                            scope.cancel()
                            scope.launch { swipeableState.animateTo(position) }
//                            swipeableState.
                        }
                        Row {
                            Spacer(modifier = Modifier
                                .size(48.dp)
                                .clickable(
//                                    indication = null,
//                                    interactionSource = remember { MutableInteractionSource() }
                                ) { swipeAnimate(0) })
                            Spacer(modifier = Modifier
                                .size(48.dp)
                                .clickable(
                                    indication = rememberRipple(radius = 48.dp),
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { swipeAnimate(1) })
//                           Spacer(modifier = Modifier.size(48.dp).clickable { horizontalBias = ReadMode.CARD_VIEW })
//                           Spacer(modifier = Modifier.size(48.dp).clickable { horizontalBias = ReadMode.ARTICLE_VIEW })
                        }

                        Spacer(
                            modifier = Modifier
                                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                                .padding(6.dp)
                                .clip(CircleShape)
                                .size(36.dp)
                                .background(Color.White)
//                                .align(alignment)

                        )
                        Row {
//                            Icon(
//                                painter = painterResource(id = R.drawable.card_view),
//                                contentDescription = null
//                            )
//                            Icon(
//                                painterResource(id = R.drawable.article),
//                                contentDescription = null
//                            )
                            ClickableIcon(
                                painter = painterResource(id = R.drawable.card_view),
                                tint = cardViewTint,
//                                enabled = false
                            )
//                            {
//                                horizontalBias = ReadMode.CARD_VIEW
//                            }
                            ClickableIcon(
                                painter = painterResource(id = R.drawable.article),
                                tint = articleView,
//                                enabled = false
                            )
//                            {
//                                horizontalBias = ReadMode.ARTICLE_VIEW
//                            }
                        }
                    }

                    ClickableIcon(painter = painterResource(R.drawable.done_broad)) {
                        viewModel.apply {
                            if (allDoneList.isEmpty()) allDone()
                            else undoAllDone()
                        }
                    }
                }
            }
        },

        modifier = hideKeyboardModifier

    ) { paddingValues ->


        val imageRatio by remember {
            derivedStateOf {
                if (lazyListState.firstVisibleItemIndex == 0 && dp(lazyListState.firstVisibleItemScrollOffset) < range) {
                    screenWidth / (screenWidth / 16 * ratio - dp(lazyListState.firstVisibleItemScrollOffset))
                } else 16f / 9f
//                if (ratio < 16f / 9f && ratio >= 0f) ratio else 16f / 9f
            }
        }

        /** Scaffold content */
        Box {

            /** article */
            Box {

                /** image cover */
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .aspectRatio(16f / ratio)
                ) {
                    Crossfade(
                        targetState = viewModel.currentImage,
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) {
                        if (it != null) {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(imageRatio)
//                            .aspectRatio(14f / 9f)
                                    .fillMaxWidth(),
//                                    .blur(8.dp)
                                contentScale = ContentScale.Crop
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


                    val spacerHeight by remember {
                        derivedStateOf {
                            if (lazyListState.firstVisibleItemIndex == 0) {
                                dp(lazyListState.firstVisibleItemScrollOffset)
                            } else 500.dp
                        }
                    }


                    // 遮罩
                    val surfaceColor = MaterialTheme.colors.surface
                    Spacer(
                        modifier = Modifier
//                    .background(imageBrush)
//                    .drawBehind { BrushTest() }
                            .align(Alignment.BottomCenter)
//                        .background(Color.Black)
                            .fillMaxSize()
//                        .fillMaxWidth()
//                        .height(spacerHeight)
                            .drawBehind {
                                drawRect(
                                    surfaceColor,
                                    topLeft = Offset(x = 0f, y = scrollOffset),
                                    size = Size(
                                        width = screenWidth.toPx(),
                                        height = imageHeight.toPx()
                                    )
                                )
                            }
                    )

//                BrushCanvas(imageBrushCenter, imageBrushBottom)
                }

                LazyColumn(
                    contentPadding = PaddingValues(bottom =
//                    WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() +
                    paddingValues.calculateBottomPadding() + 4.dp
//                                    - 8.dp,
                    ),
                    state = lazyListState,
                    modifier = Modifier.animateContentSize()
                ) {

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / ratio)
                        ) {
//                    Crossfade(targetState = viewModel.currentImage) {
//                        if (it != null) {
//                            Image(
//                                bitmap = it.asImageBitmap(),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .aspectRatio(14f / 9f)
//                                    .fillMaxWidth(),
////                                    .blur(8.dp)
//                            contentScale = ContentScale.Crop
//                            )
//                        } else {
//                            Spacer(
//                                modifier = Modifier
//                                    .aspectRatio(16f / 9f)
//                                    .fillMaxWidth()
//                                    .background(Color.DarkGray)
//                            )
//                        }
//                    }
                            Text(
                                text = viewModel.currentTitle,
                                style = Typography().h5,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(brush)
                                    .padding(top = 32.dp)
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }



                    itemsIndexed(viewModel.currentContent) { paragraphIndex, paragraphContent ->

//                    Box() {
//                    Box(modifier = animateModifier) {
//                    Box(modifier = Modifier.animateContentSize()) {
//                        if (readMode == 1) {
//                        AnimatedVisibility (readMode == 1, enter = scaleIn(tween(3000)), exit = scaleOut(tween(3000))) {
                        Text(
                            text = paragraphContent.text,
                            style = Typography().body1,
                            modifier = Modifier

                                .then(articleViewSizeModifier)
                                .alpha(articleViewAlpha)

                                .padding(vertical = 10.dp, horizontal = 16.dp)
                        )
//                        }
//                        if (readMode == 0) {
//                        AnimatedVisibility (readMode == 0, enter = scaleIn(tween(3000)), exit = scaleOut(tween(3000))) {
                        var openState by remember {
                            mutableStateOf(false)
                        }

                        var annotationState by remember {
                            mutableStateOf(AnnotationState.CLOSE)
                        }

                        val getElevation = if (MaterialTheme.colors.isLight) 24.dp else 1.dp
                        Card(
                            elevation = getElevation,
                            modifier = Modifier

                                .then(cardViewSizeModifier)
                                .alpha(cardViewAlpha)

//                                    .background(MaterialTheme.colors.background)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
//                        .clip(RoundedCornerShape(16.dp)),
                            backgroundColor = MaterialTheme.colors.surface,
//                    elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp)
//                        .background(MaterialTheme.colors.background)
//                        .animateItemPlacement(TweenSpec())
//                        .then(hideKeyboardModifier)
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    top = 8.dp,
                                    start = 8.dp,
                                    end = 8.dp
                                )
                            ) {

                                FlatTextField(
                                    value = if (paragraphContent.text.isDone()) {
                                        paragraphContent.copy(
                                            text = paragraphContent.text.removeRange(
                                                0,
                                                1
                                            )
                                        )
                                    } else paragraphContent,
                                    onValueChange = {
                                        viewModel.currentContent[paragraphIndex] = it
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    readOnly = true
                                )

//                        Spacer(modifier = Modifier.height(8.dp))

                                // chinese
                                AnimatedVisibility(visible = openState) {
                                    FlatTextField(
                                        value = viewModel.currentContentCn[paragraphIndex],
                                        onValueChange = {
                                            viewModel.currentContentCn[paragraphIndex] = it
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    )
                                }


                                // button row
                                Row(modifier = Modifier.fillMaxWidth()) {

                                    fun isDone(): Boolean {
                                        return if (paragraphContent.text.isNotEmpty()) {
                                            paragraphContent.text.isDone()
                                        } else false
                                    }

                                    val transition =
                                        updateTransition(
                                            targetState = isDone(),
                                            label = "transition"
                                        )

                                    val doneColor by transition.animateColor(label = "") {
                                        if (it) ColorDone else LocalContentColor.current
                                    }
                                    val doneAlpha by transition.animateFloat(label = "") {
                                        if (it) 1f else 0.80f
                                    }
//                            val doneColor by animateColorAsState(targetValue = if (isDone()) ColorDone else LocalContentColor.current)
                                    SelectableIcon(
                                        painter = painterResource(id = R.drawable.done_broad),
                                        onClick = {
//                                    currentParagraphIndex = paragraphIndex
//                                    curr；entParagraphContent = paragraphContent.text
//                                    deleteParagraphDialog = true
                                            viewModel.changeDoneState(paragraphIndex)
                                            Log.d(
                                                "!!",
                                                "NewsArticlePage: \n${paragraphContent.text}\n${paragraphContent.text.firstOrNull()}"
                                            )
                                        },
//                                        modifier = Modifier.alpha(doneAlpha),
//                                        selectedColor = ColorDone,
//                                        selectedColor = doneColor,
                                        isSelected = isDone()
                                    )
//                                    ClickableIcon(
//                                        painter = painterResource(id = R.drawable.done_broad),
//                                        onClick = {
////                                    currentParagraphIndex = paragraphIndex
////                                    curr；entParagraphContent = paragraphContent.text
////                                    deleteParagraphDialog = true
//                                            viewModel.changeDoneState(paragraphIndex)
//                                            Log.d(
//                                                "!!",
//                                                "NewsArticlePage: \n${paragraphContent.text}\n${paragraphContent.text.firstOrNull()}"
//                                            )
//                                        },
//                                        modifier = Modifier.alpha(doneAlpha),
//                                        tint = doneColor
//                                    )

                                    Spacer(modifier = Modifier
                                        .weight(1F)
                                        .focusable()
                                        .clickable { })

                                    SelectableIcon(
                                        painter = painterResource(id = R.drawable.translation),
                                        isSelected = annotationState == AnnotationState.TRANSLATION,
//                                        selectedColor = ColorDone,
                                        normalColor = Color.White
                                    )
//                                tint = color.value,
                                    {
                                        annotationState =
                                            if (annotationState == AnnotationState.TRANSLATION) AnnotationState.CLOSE
                                            else AnnotationState.TRANSLATION

//                                    viewModel.translation(paragraph.text)
//                                    viewModel.translateTest(context, paragraph.text)
//                                    translate(context)
                                    }

                                    // do not work
//                            val contentText by remember {
//                                derivedStateOf { paragraphContent.getSelectedText().text }
//                            }
                                    val contentText by remember {
                                        derivedStateOf { viewModel.currentContent[paragraphIndex].getSelectedText().text }
                                    }


                                    val wordIconState by remember {
                                        derivedStateOf { contentText.isNotBlank() || viewModel.wordListTable[paragraphIndex].isNotEmpty() }
                                    }
                                    SelectableIcon(
                                        painter = painterResource(id = R.drawable.word),
                                        enabled = wordIconState,
                                        isSelected = annotationState == AnnotationState.WORDS,
//                                        selectedColor = ColorDone,
                                        modifier = Modifier.focusable()
                                    ) {

                                        // 清除上一次選重的單字
                                        viewModel.noCurrentWord()

                                        // 1.檢測是否選取單字
//                                    val contentText = paragraphContent.getSelectedText().text
                                        annotationState = if (contentText.isNotBlank()) {

//                                        viewModel.curr

                                            viewModel.addWordListTable(
                                                contentText,
                                                paragraphIndex
                                            )

                                            viewModel.currentContent[paragraphIndex] =
                                                viewModel.currentContent[paragraphIndex].copy(
                                                    selection = TextRange.Zero
                                                )

//                                        wordTranslate(context, contentText)


                                            // 2.translate並且開啟annotation欄位

                                            AnnotationState.WORDS
                                        } else {
                                            // 3.檢測開啟狀態，決定開關annotation欄位
                                            if (annotationState == AnnotationState.WORDS) AnnotationState.CLOSE
                                            else AnnotationState.WORDS
                                        }
                                    }
                                    AnimatedContent(targetState = openState) {
                                        ClickableIcon(
                                            painter = painterResource(id = getRid(it))
                                        ) {
                                            openState = !openState
                                        }
                                    }
                                }

                                val color = remember {
                                    Animatable(Color.White)
                                }

                                AnimatedContent(targetState = annotationState) { it ->
                                    when (it) {
                                        AnnotationState.TRANSLATION -> {
                                            Card(
                                                elevation = 0.dp,
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier
                                                    .padding(bottom = 8.dp)
                                                    .fillMaxWidth(),
                                                backgroundColor = if (MaterialTheme.colors.isLight)
                                                    MaterialTheme.colors.onSurface.copy(alpha = TextBackgroundAlphaLight)
                                                else TextFieldDefaults.textFieldColors()
                                                    .backgroundColor(true).value

                                            ) {
                                                Text(
                                                    text = viewModel.currentContentTr[paragraphIndex].text,
                                                    style = Typography().h6,
                                                    modifier = Modifier.padding(16.dp)
                                                )
                                            }
//                                    FlatTextField(
//                                        value = viewModel.currentContentTr[paragraphIndex],
//                                        onValueChange = {
//                                            viewModel.currentContentTr[paragraphIndex] = it
//                                        },
//                                        readOnly = true,
//                                        modifier = Modifier
//                                            .fillMaxWidth()
////                                            .padding(top = 8.dp)
//                                    )

                                            LaunchedEffect(key1 = 1) {
                                                color.animateTo(Color.Yellow)
                                            }
                                        }


                                        AnnotationState.WORDS -> {

                                            val list = remember {
                                                viewModel.wordListTable[paragraphIndex]
                                            }


//                                            Log.d("!!!", "AnnotationState.WORDS: $paragraphIndex")
                                            // 測試中
                                            val wordList: SnapshotStateList<MutableState<Word>> =
                                                list.map {
                                                    viewModel.getWordById(it)
                                                        .collectAsState(initial = EmptyWord.word) as MutableState<Word>
                                                }.toMutableStateList()
//                                        emptyList<MutableState<Word>>().toMutableStateList()

//                                    wordList = list
//                                        .map { viewModel.getWordById(it)
//                                            .collectAsState(initial = EmptyWord.word) as MutableState<Word>}
//                                        .map(it -> viewModel.getWordById(it))
//                                        .toCollection()

//                                    list.forEachIndexed { index, wordId ->
//                                        wordList.add(viewModel.getWordById(wordId)
//                                            .collectAsState(initial = EmptyWord.word) as MutableState<Word>)
//                                        Log.d("!!!", "WordListTable: $index")
//                                    }

//                                    LazyColumn(
//                                        modifier = Modifier
//                                            .padding(bottom = 8.dp)
//                                            .focusable()
//                                            .animateContentSize()
//                                            .heightIn(max = 300.dp)
//                                    ) {
//
////                                        items(wordList, key = { it -> it }) { word ->
////                                            WordComponent(word = word,
////                                                onValueChange = {
////                                                    wordList[index].value =
////                                                        word.value.copy(chinese = it)
////                                                },
////                                                remove = {
////                                                    viewModel.wordListTable[index].remove(word.value.id)
////                                                },
////                                                updateWord = { viewModel.updateWord(word.value) },
////                                                updateChinese = {
////                                                    viewModel.updateWord(Word(word.value.id,
////                                                        word.value.english,
////                                                        it))
////                                                })
////                                        }
//
//                                        itemsIndexed(wordList) { index, word ->
//                                            WordComponent(word = word,
//                                                onValueChange = {
//                                                    wordList[index].value =
//                                                        word.value.copy(chinese = it)
//                                                },
//                                                remove = {
//                                                    viewModel.wordListTable[paragraphIndex].remove(
//                                                        word.value.id
//                                                    )
//                                                },
//                                                updateWord = { viewModel.updateWord(word.value) })
//                                        }
//                                    }

                                            Column(
                                                modifier = Modifier
                                                    .padding(bottom = 6.dp)
//                                        .animateContentSize()
                                                    .focusable()
                                            ) {

                                                wordList.forEachIndexed { index, word ->
                                                    WordComponent(
                                                        word = word,
                                                        onValueChange = {
                                                            wordList[index].value = word.value.copy(chinese = it)
                                                        },
                                                        remove = {
                                                            Log.d(
                                                                "!!",
                                                                viewModel.wordListTable[index].toList()
                                                                    .toString() + index.toString()
                                                            )
//                                                    Log.d("!!", viewModel.wordListTable[index].toList().toString())
//                                                            viewModel.wordListTable[index].remove(
//                                                                word.value.id
//                                                            )
                                                    viewModel.wordListTable[paragraphIndex].remove(word.value.id)
//                                                            Log.d(
//                                                                "!!",
//                                                                viewModel.wordListTable[index].toList()
//                                                                    .toString()
//                                                            )
                                                        },
                                                        updateWord = { viewModel.updateWord(word.value) },
//                                                updateChinese = {
//                                                    viewModel.updateWord(
//                                                        Word(
//                                                            word.value.id,
//                                                            word.value.english,
//                                                            it
//                                                        )
//                                                    )
//                                                }
                                                        viewModel = viewModel
                                                    )
                                                }
                                            }

//                                    WordListTable(
//                                        wordList = wordList,
//                                        getWordById = { viewModel.getWordById(it) },
//                                        deleteWord = { list.remove(it) },
//                                        updateWord = { viewModel.updateWord(it) },
//                                        paragraphIndex,
//                                        viewModel
//                                    )

                                            LaunchedEffect(key1 = 1) {
                                                color.animateTo(Color.White)
                                            }


//                                    viewModel.currentContentWordList.toList()
//                                        .forEachIndexed { index, snapshotStateList ->
////                                        Log.d("!!! forEach", "$index：${snapshotStateList.toList()}")
//                                        }
                                        }
                                        AnnotationState.CLOSE -> {
                                            LaunchedEffect(key1 = 1) {
                                                color.animateTo(Color.White)
                                            }
                                        }
                                    }
                                }
//                        Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
//                        }
//                    }

                    }
                }

                /** infoCard mask */

                val clickableModifier = Modifier.clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    infoCardIsOpening = false
                }
                val maskModifier by remember { derivedStateOf { if (infoCardIsOpening) clickableModifier else Modifier } }
                Spacer(modifier = maskModifier
                    .fillMaxSize()
                    .drawBehind { drawRect(maskColor) }
//                    .background(maskColor)
                )
            }

            /** StatusBar mask */
            Spacer(modifier = Modifier
                .drawBehind { drawRect(getColor()) }
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(
                    WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                ))


            /** Popup info */
            AnimatedVisibility(
                visible = infoCardIsOpening,
                enter = scaleIn() + fadeIn() + slideIn { fullSize -> IntOffset(0, fullSize.height / 5 * 2) },
                exit = scaleOut() + fadeOut() + slideOut { fullSize -> IntOffset(0, fullSize.height / 5 * 2) },
                modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .align(Alignment.BottomCenter)) {
                PopupInfo(viewModel.currentNews, progress) { deleteArticleDialog = true }
//                PopupInfo(modifier = Modifier
//                    .padding(bottom = paddingValues.calculateBottomPadding())
//                    .align(Alignment.BottomCenter))
            }
//            if (infoCardIsOpening) PopupInfo(modifier = Modifier
//                .padding(bottom = paddingValues.calculateBottomPadding())
//                .align(Alignment.BottomCenter))

        }


    }

    if (deleteParagraphDialog) {
        AlertDialog(
            onDismissRequest = { deleteParagraphDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeCurrentParagraph(
                        currentParagraphIndex
                    )
                    deleteParagraphDialog = false
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteParagraphDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Delete Paragraph?", style = Typography().h5)
            },
            text = {
                Text(text = "\"$currentParagraphContent\"")
            },
        )
    }

    if (deleteArticleDialog) {
        AlertDialog(
            onDismissRequest = { deleteArticleDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    deleteArticleDialog = false
                    popBack()
                    viewModel.deleteNews(context)
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteArticleDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Delete Article?", style = Typography().h5)
            },
            text = {
                Text(text = "\"${viewModel.currentTitle}\"")
            },
        )
    }

//    if (editTagDialog) {
//    }

//    if (editTitleDialog) {
//    }
}