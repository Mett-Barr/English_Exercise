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
import com.example.english.content
import com.example.english.data.word.word.room.EmptyWord
import com.example.english.data.word.word.room.Word
import com.example.english.isDone
import com.example.english.translation.translateWord
import com.example.english.ui.components.*
import com.example.english.ui.components.test.PopupInfo
import com.example.english.ui.theme.ColorDone
import com.example.english.ui.theme.TextBackgroundAlphaLight
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


enum class AnnotationState {
    TRANSLATION, WORDS, CLOSE, ONE_WORD
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
fun ArticlePage(
    viewModel: MainViewModel,
    title: String,
    navController: NavController,
    navigateToWebPage: (String) -> Unit,
) {
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
    val maskColor by animateColorAsState(
        targetValue = if (infoCardIsOpening) MaterialTheme.colors.background.copy(
            alpha = 0.8f
        ) else Color.Transparent, animationSpec = tween(300)
    )


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
            val alpha =
                if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset <= statusBarHeight * 2) {
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
    val progress by remember(viewModel.currentContentTr) {
        derivedStateOf {
            var done = 0
//            viewModel.currentContent.forEach { if (it.text.isDone()) done++ }
            viewModel.currentContentTr.forEach { if (it.text.isDone()) done++ }

            val progress = (done * 100 / viewModel.currentNewsSize)

            progress
        }
    }


//    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

//    var wordState by remember { mutableStateOf(false) }

    var focusWord by remember {
        mutableStateOf(Word())
    }

    var focused by remember {
        mutableStateOf(false)
    }


    val hideKeyboardModifier = Modifier


//        .focusable()
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            Log.d("!!!", "hideKeyboardModifier ")
            focusManager.clearFocus()
            keyboardController?.hide()


//            if (focusWord != Word()) focusWord = Word()
            if (focused) focused = false
            else focusWord = Word()
//            else
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

//    LaunchedEffect(key1 = Unit) {
//        scope.launch {
//            while (true) {
//                delay(1000)
//                Log.d("!!", "NewsArticlePage: $naviBarPadding")
//            }
//        }
//    }

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

                    val colorTint = MaterialTheme.colors.primaryVariant
                    val gray = if (MaterialTheme.colors.isLight) Color.Gray else contentColor
                    fun gestureColor(state: Float): Color {
                        val rangeState = if (state > 1f) 1f else if (state < 0) 0f else state
                        return Color(
                            red = colorTint.red * rangeState + (1 - rangeState) * contentColor.red,
                            green = colorTint.green * rangeState + (1 - rangeState) * contentColor.green,
                            blue = colorTint.blue * rangeState + (1 - rangeState) * contentColor.blue
//                            red = PrimaryVariant.red * rangeState + (1 - rangeState) * contentColor.red,
//                            green = PrimaryVariant.green * rangeState + (1 - rangeState) * contentColor.green,
//                            blue = PrimaryVariant.blue * rangeState + (1 - rangeState) * contentColor.blue
                        )
                    }

                    val cardViewTint by remember { derivedStateOf { gestureColor(1 - swipeableState.offset.value / sizePx) } }
                    val articleView by remember { derivedStateOf { gestureColor(swipeableState.offset.value / sizePx) } }

//                    val color = MaterialTheme.colors.onSurface.copy(0.1f)
                    val color = if (!MaterialTheme.colors.isLight) {
                        LocalElevationOverlay.current!!.apply(
                            MaterialTheme.colors.surface,
                            LocalAbsoluteElevation.current + 50.dp
                        )
                    } else {
                        MaterialTheme.colors.onSurface.copy(0.08f)
                    }


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

                        // picker
                        if (!MaterialTheme.colors.isLight) {

                            Spacer(
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            swipeableState.offset.value.roundToInt(),
                                            0
                                        )
                                    }
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .size(36.dp)
                                    .background(if (MaterialTheme.colors.isLight) Color(0xFF888888) else Color.White)
//                                .background(Color.White)
//                                .align(alignment)
                            )
                        } else {
                            Surface(
                                elevation = 4.dp,
                                shape = CircleShape,
                                color = Color(0xFF777777),
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            swipeableState.offset.value.roundToInt(),
                                            0
                                        )
                                    }
                                    .padding(6.dp)
                                    .size(36.dp)
                            ) {}
                        }
                        Row {
//                            Icon(
//                                painter = painterResource(id = R.drawable.card_view),
//                                contentDescription = null
//                            )
//                            Icon(
//                                painterResource(id = R.drawable.article),
//                                contentDescription = null
//                            )
//                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {

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

//                            }
                        }
                    }

                    ClickableIcon(painter = painterResource(R.drawable.done_broad)) {
                        viewModel.apply {
                            if (allDoneList.isEmpty()) {
                                if (progress == 100) allUndone()
                                else allDone()
                            } else undoAllDone()
                            Log.d(
                                "!!!",
                                "allDoneList.isEmpty() = ${allDoneList.isEmpty()}   progress = $progress"
                            )
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
                    contentPadding = PaddingValues(
                        bottom =
//                    WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() +
                        paddingValues.calculateBottomPadding() + 4.dp
//                                    - 8.dp,
                    ),
                    state = lazyListState,
                    modifier = hideKeyboardModifier.animateContentSize()
//                    modifier = Modifier.animateContentSize()
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
                            text = paragraphContent.text.content(),
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


                        /**  annotation state control */
                        var annotationState by remember {
                            mutableStateOf(AnnotationState.CLOSE)
                        }

//                        var focused by remember {
//                            mutableStateOf(false)
//                        }

                        suspend fun String.isNewWord(index: Int): Boolean {
                            val id = viewModel.getWordId(this)
                            return !viewModel.wordListTable[index].contains(id)
                        }

                        suspend fun String.hasTranslation(): Boolean {
                            val id = viewModel.getWordId(this)
                            return id?.let {
                                Log.d("!!!", "hasBeenTranslated: ^${viewModel.getWordByIdSus(it).chinese}^")
                                viewModel.getWordByIdSus(it).chinese.isNotBlank()
                            } ?: false
                        }

                        //!!!!!
                        val selectedText by remember {
                            derivedStateOf {
                                Log.d("!!!", "currentContent = ${viewModel.currentContent[paragraphIndex]}")
                                viewModel.currentContent[paragraphIndex].getSelectedText().text
                            }
                        }

                        val selectedWord = remember { mutableStateOf(Word()) }

                        var hasBeenAdded by remember {
                            mutableStateOf(false)
                        }

                        var justBeenAdded by remember {
                            mutableStateOf(false)
                        }


                        var textNeedTranslate by remember {
                            mutableStateOf(false)
                        }

                        fun annoInit() {
                            textNeedTranslate = false
                            hasBeenAdded = false
                            justBeenAdded = false
                        }

                        LaunchedEffect(annotationState) {
                            if (annotationState != AnnotationState.ONE_WORD) {
                                annoInit()
                            }
                        }


                        suspend fun oneWordChange() {
//                            Log.d(
//                                "!!!",
//                                "1 ArticlePage:  focusWord = $focusWord"
//                            )

//                            if (selectedText.isNotBlank()) {
//                                val id = viewModel.getWordId(selectedText)
//                                if (viewModel.wordListTable[paragraphIndex].contains(id)) {
////                                Log.d("!!!", "$selectedText true")
////                                if (annotationState)
//
//                                    selectedWord.value = viewModel.getWordByIdSus(id!!)
//                                    Log.d("!!!", "selectedText.isNotBlank  ${selectedWord.value}")
//                                    annotationState = AnnotationState.ONE_WORD
//                                } else {
//                                    if (annotationState == AnnotationState.WORDS) {
//
//                                    }
////                                    annotationState = AnnotationState.CLOSE
////                                    selectedWord.value = Word()
//                                }
//
////                            } else if (annotationState == AnnotationState.ON_WORD) {
//                            } else if (annotationState == AnnotationState.ONE_WORD && focusWord != selectedWord.value) {
//                                if (focusWord != Word()) {
//                                    focusWord = Word()
//                                } else {
//                                    annotationState = AnnotationState.CLOSE
//                                }
//                            }

                            if (selectedText.isNotBlank()) {

                                val selectedTextIsNewWord = selectedText.isNewWord(paragraphIndex)

//                                if (selectedTextIsNewWord || hasBeenAdded && !selectedText.hasBeenTranslated()) textNeedTranslate = true
                                textNeedTranslate =
                                    selectedTextIsNewWord || !selectedText.hasTranslation()
//                                    selectedTextIsNewWord || hasBeenAdded && !selectedText.hasTranslation()
                                Log.d(
                                    "!!!", "oneWordChange: $selectedText" +
                                            "\nselectedTextIsNewWord = $selectedTextIsNewWord" +
                                            "\nhasBeenAdded = $hasBeenAdded" +
                                            "\n!selectedText.hasBeenTranslated() = ${selectedText.hasTranslation()}" +
                                            "\ntextNeedTranslate = $textNeedTranslate"
                                )

//                                if (!selectedTextIsNewWord) hasBeenAdded = true
                                hasBeenAdded = !selectedTextIsNewWord

                                annotationState = AnnotationState.ONE_WORD


//                                annotationState =
//                                    if (
//                                        !selectedTextIsNewWord
////                                        !selectedText.isNewWord(paragraphIndex)
//                                        && focusWord == Word()
//                                        && annotationState == AnnotationState.ONE_WORD
//                                    ) {
//
//                                        // !!!!!
//                                        Log.d("!!!", "oneWordChange: AnnotationState.CLOSE")
//                                        AnnotationState.CLOSE
//                                    } else AnnotationState.ONE_WORD
//
//                                if (selectedTextIsNewWord || hasBeenAdded) textNeedTranslate = true
//
                            } else if (annotationState == AnnotationState.ONE_WORD && focusWord == Word() && !focused) {

                                Log.d(
                                    "!!!!", "oneWordChange: AnnotationState.CLOSE\n" +
                                            "annotationState  = ${annotationState}\n" +
                                            "focusWord = $focusWord\n" +
                                            "focused = $focused"
                                )

                                annoInit()

                                annotationState = AnnotationState.CLOSE
                            }

                            Log.d(
                                "!!!", "oneWordChange: \n" +
                                        "!selectedText.isNewWord(paragraphIndex) = ${
                                            !selectedText.isNewWord(
                                                paragraphIndex
                                            )
                                        }\n" +
                                        "focusWord == Word() = ${focusWord == Word()}\n" +
                                        "annotationState == AnnotationState.ONE_WORD = ${annotationState == AnnotationState.ONE_WORD}"
                            )


//                            Log.d(
//                                "!!!",
//                                "2 ArticlePage: focusWord = $focusWord"
//                            )
                        }

//                        LaunchedEffect(selectedText) {
                        LaunchedEffect(selectedText, focusWord) {
                            if (selectedText.isBlank() && focusWord == Word())
                                annoInit()

                            Log.d("!!!", "oneWordChange() selectedText = $selectedText, focusWord = $focusWord")
                            
                            oneWordChange()
                        }

//                        LaunchedEffect(selectedText) {
//                            oneWordChange()
//                        }
//
//                        LaunchedEffect(focusWord) {
//                            oneWordChange()
//                        }
                        /**  annotation state control */


                        val getElevation = if (MaterialTheme.colors.isLight) 8.dp else 1.dp
                        Card(
                            elevation = getElevation,
                            modifier = Modifier
                                .then(cardViewSizeModifier)
                                .alpha(cardViewAlpha)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .fillMaxWidth()
                                .then(hideKeyboardModifier)
                                .wrapContentHeight(),
                            backgroundColor = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    top = 8.dp,
                                    start = 8.dp,
                                    end = 8.dp
                                )
                            ) {

//                                FlatText(text = paragraphContent.text.content())

//                                fun TextRange.byState(): TextRange {
//                                    return if (paragraphContent.text.isDone()) {
//                                        TextRange(
//                                            start = this.start + 1,
//                                            end = this.end + 1
//                                        )
//                                    } else this
//                                }

                                FlatTextField(
//                                    value = paragraphContent.text.content(),
                                    value = paragraphContent,
//                                    if (paragraphContent.text.isDone()) {
//                                        paragraphContent.copy(
//                                            text = paragraphContent.text.content(),
////                                            text = paragraphContent.text.removeRange(0, 1)
//
//                                            selection = TextRange(
//                                                paragraphContent.selection.start - 1,
//                                                paragraphContent.selection.end - 1
//                                            )
//                                        )
//                                    } else paragraphContent,
                                    onValueChange = {
                                        Log.d("!!!", "onValueChange: $it")
                                        if (it.selection.start != it.selection.end || it.selection == TextRange.Zero)
                                            viewModel.currentContent[paragraphIndex] = it
//                                            paragraphContent.copy(
////                                                selection = if (!paragraphContent.text.isDone()) it.selection
////                                                else TextRange(
////                                                    it.selection.start + 1,
////                                                    it.selection.end + 1
////                                                ),
//                                                selection = it.selection.byState(),
//                                                composition = it.composition
//                                            )
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

                                var isNewWord by remember { mutableStateOf(false) }

                                // button row
                                Row(modifier = hideKeyboardModifier.fillMaxWidth()) {

                                    fun isParagraphDone(): Boolean {
//                                        Log.d("!!!", "isDone: ${paragraphContent.text}")
//                                        return if (paragraphContent.text.isNotEmpty()) {
//                                            paragraphContent.text.isDone()
//                                        } else false
//                                        return paragraphContent.text.isDone()
                                        return viewModel.currentContentTr[paragraphIndex].text.isDone()
                                    }

                                    val transition =
                                        updateTransition(
                                            targetState = isParagraphDone(),
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
                                        isSelected = isParagraphDone()
                                    )

                                    LaunchedEffect(selectedText) {
                                        val id = viewModel.getWordId(selectedText)
                                        isNewWord =
                                            !viewModel.wordListTable[paragraphIndex].contains(id) && selectedText.isNotBlank()
                                    }
                                    AnimatedVisibility(
                                        visible = isNewWord && !justBeenAdded,
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    ) {
                                        ClickableIcon(painter = painterResource(id = R.drawable.add_board)) {

                                            // 清除上一次選重的單字
                                            viewModel.noCurrentWord()

                                            viewModel.addWordListTable(
                                                selectedText,
                                                paragraphIndex
                                            )

                                            justBeenAdded = true
                                        }
                                    }

                                    Spacer(
                                        modifier = Modifier
                                            .weight(1F)
//                                        .fillMaxHeight()
//                                        .clickable {
//                                            annotationState = AnnotationState.CLOSE
//                                            viewModel.currentContent[paragraphIndex] =
//                                                viewModel.currentContent[paragraphIndex].copy(
//                                                    selection = TextRange.Zero)
//                                        }
                                    )

                                    SelectableIcon(
                                        painter = painterResource(id = R.drawable.translation),
                                        isSelected = annotationState == AnnotationState.TRANSLATION,
//                                        selectedColor = ColorDone,
                                        normalColor = Color.White,
                                        modifier = Modifier.focusable()
                                    ) {
                                        annotationState =
                                            if (annotationState == AnnotationState.TRANSLATION) AnnotationState.CLOSE
                                            else AnnotationState.TRANSLATION

//                                        viewModel.currentContent[paragraphIndex] =
//                                            viewModel.currentContent[paragraphIndex].copy(
//                                                selection = TextRange.Zero
//                                            )


//                                    viewModel.translation(paragraph.text)
//                                    viewModel.translateTest(context, paragraph.text)
//                                    translate(context)
                                    }

                                    // do not work
//                            val contentText by remember {
//                                derivedStateOf { paragraphContent.getSelectedText().text }
//                            }


                                    /** ------------------ */
                                    val wordIconState by remember {
                                        derivedStateOf { selectedText.isNotBlank() || viewModel.wordListTable[paragraphIndex].isNotEmpty() }
                                    }
//
////                                    var isWordIconAdd by remember {
////                                        mutableStateOf(false)
////                                    }
//
//                                    val painterAdd = painterResource(id = R.drawable.add_board)
//                                    val painterWords = painterResource(id = R.drawable.word)
//
//                                    var wordIconPainter by remember {
//                                        mutableStateOf(painterWords)
//                                    }
//
//                                    LaunchedEffect(selectedText) {
////                                        Log.d("!!!", "wordIconState")
////                                        wordIconPainter = if (wordIconState) {
////                                            val id = viewModel.getWordId(selectedText)
////                                            if (viewModel.wordListTable[paragraphIndex].contains(id)) painterWords
////                                            else if (annotationState == AnnotationState.WORDS) painterWords
////                                            else if (selectedText.isBlank()) painterWords
////                                            else painterAdd
////                                        } else painterWords
//
////                                        if (wordIconState) {
//
//                                        val id = viewModel.getWordId(selectedText)
//                                        wordIconPainter =
//                                            if (!viewModel.wordListTable[paragraphIndex].contains(id) && selectedText.isNotBlank()) painterAdd
//                                            else painterWords
//
////                                        }
//
//                                        Log.d(
//                                            "!!!",
//                                            "wordIconPainter == painterAdd  ${wordIconPainter == painterAdd}"
//                                        )
//
//                                        Log.d("!!!", "selectedText = $selectedText ")
//                                    }
//
//                                    AnimatedContent(targetState = wordIconPainter) {
//                                        SelectableIcon(
//                                            painter = it,
//                                            enabled = wordIconState,
//                                            isSelected = annotationState == AnnotationState.WORDS,
////                                        selectedColor = ColorDone,
//                                            modifier = Modifier.focusable()
//                                        ) {
//                                            // 更換Icon
//                                            wordIconPainter = painterWords
//
//                                            // 清除上一次選重的單字
//                                            viewModel.noCurrentWord()
//
//                                            // 1.檢測是否選取單字
////                                    val contentText = paragraphContent.getSelectedText().text
//                                            annotationState = if (selectedText.isNotBlank()) {
//
//
//                                                viewModel.addWordListTable(
//                                                    selectedText,
//                                                    paragraphIndex
//                                                )
//
//                                                viewModel.currentContent[paragraphIndex] =
//                                                    viewModel.currentContent[paragraphIndex].copy(
//                                                        selection = TextRange.Zero
//                                                    )
//
//
//                                                // 2.translate並且開啟annotation欄位
//
//                                                AnnotationState.WORDS
//                                            } else {
//                                                // 3.檢測開啟狀態，決定開關annotation欄位
//                                                if (annotationState == AnnotationState.WORDS) AnnotationState.CLOSE
//                                                else AnnotationState.WORDS
//                                            }
//                                        }
//                                    }
                                    /** ------------------ */

                                    SelectableIcon(
                                        painter = painterResource(id = R.drawable.word),
                                        enabled = viewModel.wordListTable[paragraphIndex].isNotEmpty(),
//                                        enabled = wordIconState,
                                        isSelected = annotationState == AnnotationState.WORDS,
//                                        selectedColor = ColorDone,
                                        modifier = Modifier.focusable()
                                    ) {

                                        annotationState =
                                            if (annotationState == AnnotationState.WORDS) AnnotationState.CLOSE
                                            else AnnotationState.WORDS

//                                        viewModel.currentContent[paragraphIndex] =
//                                            viewModel.currentContent[paragraphIndex].copy(
//                                                selection = TextRange.Zero
//                                            )


                                        // 清除上一次選重的單字
                                        viewModel.noCurrentWord()
//
//                                        // 1.檢測是否選取單字
////                                    val contentText = paragraphContent.getSelectedText().text
//                                        annotationState = if (selectedText.isNotBlank()) {
//
//
//                                            viewModel.addWordListTable(
//                                                selectedText,
//                                                paragraphIndex
//                                            )
//
//                                            viewModel.currentContent[paragraphIndex] =
//                                                viewModel.currentContent[paragraphIndex].copy(
//                                                    selection = TextRange.Zero
//                                                )
//
//
//                                            // 2.translate並且開啟annotation欄位
//
//                                            AnnotationState.WORDS
//                                        } else {
//                                            // 3.檢測開啟狀態，決定開關annotation欄位
//                                            if (annotationState == AnnotationState.WORDS) AnnotationState.CLOSE
//                                            else AnnotationState.WORDS
//                                        }

                                    }

                                    AnimatedContent(targetState = openState) {
                                        ClickableIcon(
                                            painter = painterResource(id = getRid(it))
                                        ) {
                                            openState = !openState
                                        }
                                    }
                                }

//                                val color = remember {
//                                    Animatable(Color.White)
//                                }

//                                LaunchedEffect(selectedText, focusWord) {
//
//                                    if (selectedText.isBlank() && focusWord == Word() && annotationState == AnnotationState.ONE_WORD) annotationState =
//                                        AnnotationState.CLOSE
//                                    else if (annotationState == AnnotationState.CLOSE) annotationState = AnnotationState.ONE_WORD
//                                }

//                                LaunchedEffect(isNewWord) {
//                                    if (isNewWord) annotationState = AnnotationState.ONE_WORD
//                                    else if (annotationState == AnnotationState.ONE_WORD && focusWord != Word()) annotationState =
//                                        AnnotationState.CLOSE
//                                }

                                AnimatedContent(targetState = annotationState) { it ->
                                    when (it) {
                                        AnnotationState.TRANSLATION -> {
                                            annoInit()

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
                                                    text = viewModel.currentContentTr[paragraphIndex].text.content(),
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

//                                            LaunchedEffect(key1 = 1) {
//                                                color.animateTo(Color.Yellow)
//                                            }
                                        }


                                        AnnotationState.WORDS -> {
                                            annoInit()

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
                                                            wordList[index].value =
                                                                word.value.copy(chinese = it)
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
                                                            viewModel.wordListTable[paragraphIndex].remove(
                                                                word.value.id
                                                            )
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

//                                            LaunchedEffect(key1 = 1) {
//                                                color.animateTo(Color.White)
//                                            }


//                                    viewModel.currentContentWordList.toList()
//                                        .forEachIndexed { index, snapshotStateList ->
////                                        Log.d("!!! forEach", "$index：${snapshotStateList.toList()}")
//                                        }
                                        }
                                        AnnotationState.CLOSE -> {
//                                            LaunchedEffect(key1 = 1) {
//                                                color.animateTo(Color.White)
//                                            }
                                        }


                                        AnnotationState.ONE_WORD -> {

//                                            var hasBeenAdded by remember {
//                                                mutableStateOf(false)
//                                            }

//                                            val oneWord by remember {
//                                                derivedStateOf {
//                                                    Word(english = if (selectedText.isNotBlank()))
//                                                }
//                                            }
                                            var oneWord by remember {
                                                mutableStateOf(Word(english = selectedText))
                                            }
                                            var translatedText by remember {
                                                mutableStateOf("")
                                            }

//                                            LaunchedEffect(selectedText) {
//
//                                                if (selectedText.isNotBlank()) {
//                                                    oneWord = oneWord.copy(
//                                                        english = selectedText
//                                                    )
//
//                                                    val id = viewModel.getWordId(selectedText)
//                                                    if (viewModel.wordListTable[paragraphIndex].contains(
//                                                            id
//                                                        )
//                                                    ) {
//                                                        oneWord = viewModel.getWordByIdSus(id!!)
//                                                    }
//                                                }
////                                                else if (!viewModel.currentWord.isNullOrBlank()) {
////                                                    oneWord = oneWord.copy(
////                                                        english = viewModel.currentWord!!
////                                                    )
////
////                                                    val id = viewModel.getWordId(viewModel.currentWord!!)
////                                                    if (viewModel.wordListTable[paragraphIndex].contains(
////                                                            id
////                                                        )
////                                                    ) {
////                                                        oneWord = viewModel.getWordByIdSus(id!!)
////                                                    }
////                                                }
//
//                                                Log.d("!!!", "AnnotationState.ONE_WORD")
////                                                val chinese = translateWord(selectedText, context)
//                                                translateWord(oneWord.english, context) {
////                                                    oneWord = oneWord.copy(
////                                                        chinese = it
////                                                    )
//                                                    translatedText = it
//                                                }
////                                                Log.d("!!!", chinese)
//                                            }
//
//                                            LaunchedEffect(viewModel.wordListTable[paragraphIndex].size) {
//                                                val id = viewModel.getWordId(oneWord.english)
//                                                hasBeenAdded =
//                                                    viewModel.wordListTable[paragraphIndex].contains(
//                                                        id
//                                                    )
//
//                                                viewModel.currentWord = oneWord.english
//
//                                                Log.d(
//                                                    "!!!",
//                                                    "AnnotationState.ONE_WORD: hasBeenAdded = $hasBeenAdded"
//                                                )
//                                            }


                                            Column(modifier = Modifier.padding(bottom = 8.dp)) {

                                                AnimatedContent(targetState = hasBeenAdded) { state ->
                                                    if (state) {
                                                        WordComponent2(
                                                            word = oneWord,
                                                            onValueChange = {
                                                                oneWord = oneWord.copy(chinese = it)
                                                            },
                                                            remove = {
                                                                viewModel.wordListTable[paragraphIndex].remove(
                                                                    oneWord.id
                                                                )
                                                            },
                                                            updateWord = {
                                                                viewModel.updateWord(oneWord)
                                                            },
                                                            viewModel = viewModel,
                                                            focusWord = {
                                                                focused = true
                                                                focusWord = it
                                                            },
                                                            needSwipe = !textNeedTranslate
                                                        )
                                                    }
                                                }

                                                AnimatedContent(targetState = textNeedTranslate) {
                                                    if (it) TranslatedWordComponent(translation = translatedText)
                                                }
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
                enter = scaleIn() + fadeIn() + slideIn { fullSize ->
                    IntOffset(
                        0,
                        fullSize.height / 5 * 2
                    )
                },
                exit = scaleOut() + fadeOut() + slideOut { fullSize ->
                    IntOffset(
                        0,
                        fullSize.height / 5 * 2
                    )
                },
                modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .align(Alignment.BottomCenter)
            ) {
                PopupInfo(viewModel.currentNews,
                    progress,
                    { deleteArticleDialog = true }) { navigateToWebPage(it) }
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
                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.error),
                    onClick = {
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