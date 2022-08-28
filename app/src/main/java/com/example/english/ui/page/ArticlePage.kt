package com.example.english.ui.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.data.word.word.room.EmptyWord
import com.example.english.data.word.word.room.Word
import com.example.english.ui.components.ClickableIcon
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.WordComponent
import com.example.english.ui.components.animateHorizontalAlignmentAsState
import com.example.english.ui.theme.ColorDone
import com.example.english.ui.theme.PrimaryVariant
import com.example.english.ui.theme.TextBackgroundAlphaLight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class AnnotationState {
    TRANSLATION, WORDS, CLOSE
}

enum class ReadMode(val position: Float) {
    CARD_VIEW(-1F), ARTICLE_VIEW(1F)
}

const val ratio = 12f

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NewsArticlePage(viewModel: MainViewModel, title: String, navController: NavController) {
//    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // system bar

    val naviBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    val lazyListState = rememberLazyListState()

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    systemUiController.setStatusBarColor(color = Color.Transparent)

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding().toPx() * 2
    }

    val statusBarAlpha by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset <= statusBarHeight) {
                lazyListState.firstVisibleItemScrollOffset / statusBarHeight / 2
            } else 0.5f
        }
    }

    fun getColor(): Color = Color.Transparent.copy(alpha = statusBarAlpha)
//    val

//    SideEffect {
    // Update all of the system bar colors to be transparent, and use
    // dark icons if we're in light theme

    var boolean by remember { mutableStateOf(false) }

//    val animatedStatusBarColor by animateColorAsState(targetValue = if (boolean) Color.Transparent else Color.Transparent.copy(0.5f))

//    boolean = true
    systemUiController.apply {
        setNavigationBarColor(
            color = Color.Transparent,
//            darkIcons = useDarkIcons
        )
        setStatusBarColor(
//            color = animatedStatusBarColor,
            color = Color.Transparent,
//            color = getColor(),
//            color = Color.Transparent.copy(alpha = statusBarAlpha),
//            darkIcons = useDarkIcons
        )
    }


    // setStatusBarsColor() and setNavigationBarsColor() also exist
//    }

//    LaunchedEffect(key1 = Unit) {
//        systemUiController.setStatusBarColor(color = Color.Transparent.copy(alpha = statusBarAlpha),
//            darkIcons = useDarkIcons)
//    }


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

    Scaffold(
        bottomBar = {
            BottomAppBar(contentPadding = WindowInsets.navigationBars.asPaddingValues()) {
                Row(modifier = Modifier
//                    .wrapContentHeight()
//                    .height(64.dp + naviBarPadding)
//                    .padding(bottom = naviBarPadding)
                    .align(Alignment.CenterVertically)
                ) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        ClickableIcon(painter = painterResource(R.drawable.back)) {
                            dispatcher.onBackPressed()
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                    )
                    ClickableIcon(painter = painterResource(R.drawable.delete), enabled = true) {
                        deleteArticleDialog = true
                    }

                    // change to info icon
//                    ClickableIcon(painter = painterResource(R.drawable.lable), enabled = false)

                    // SegmentedControls
                    var horizontalBias by remember { mutableStateOf(ReadMode.CARD_VIEW) }

                    val transition = updateTransition(targetState = horizontalBias, label = "Segmented Controls")

                    val alignment by animateHorizontalAlignmentAsState(horizontalBias.position)

                    val cardViewTint by transition.animateColor(label = "cardView tint") {
                        when(it) {
                            ReadMode.CARD_VIEW -> PrimaryVariant
                            ReadMode.ARTICLE_VIEW -> Color.White
                        }
                    }

                    val articleView by transition.animateColor(label = "cardView tint") {
                        when(it) {
                            ReadMode.CARD_VIEW -> Color.White
                            ReadMode.ARTICLE_VIEW -> PrimaryVariant
                        }
                    }

//                    Surface(elevation = 8.dp) {
//
//                    }

                    val color = LocalElevationOverlay.current!!.apply(MaterialTheme.colors.surface,LocalAbsoluteElevation.current + 50.dp)

                    Box(modifier = Modifier
//                        .clickable { horizontalBias *= -1 }
                        .clip(CircleShape)
//                        .background(Color.DarkGray)
                        .background(color)
                        .wrapContentSize()
                        .height(intrinsicSize = IntrinsicSize.Min)) {
                        Spacer(modifier = Modifier
                            .padding(6.dp)
                            .clip(CircleShape)
                            .size(36.dp)
                            .background(Color.White)
                            .align(alignment))
                        Row {
                            ClickableIcon(painter = painterResource(id = R.drawable.card_view), tint = cardViewTint) {
                                horizontalBias = ReadMode.CARD_VIEW
                            }
                            ClickableIcon(painter = painterResource(id = R.drawable.article), tint = articleView) {
                                horizontalBias = ReadMode.ARTICLE_VIEW
                            }
                        }
                    }

                    ClickableIcon(painter = painterResource(R.drawable.done_broad), enabled = false)
                }
            }
        },

        modifier = hideKeyboardModifier

    ) { paddingValues ->

        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp
        val imageHeight = screenWidth / 16 * ratio
        val range = screenWidth / 16 * (ratio - 9)

        val imageRatio by remember {
            derivedStateOf {
                if (lazyListState.firstVisibleItemIndex == 0 && dp(lazyListState.firstVisibleItemScrollOffset) < range) {
                    screenWidth / (screenWidth / 16 * ratio - dp(lazyListState.firstVisibleItemScrollOffset))
                } else 16f / 9f
//                if (ratio < 16f / 9f && ratio >= 0f) ratio else 16f / 9f
            }
        }

//        val imageBrushBottom by remember {
//            derivedStateOf {
//                if (lazyListState.firstVisibleItemIndex == 0) {
//                    (imageHeight - dp(lazyListState.firstVisibleItemScrollOffset)) / imageHeight
//                } else 0f
//            }
//        }

//        val imagePoint = imageHeight / 3
//        val imageBrushCenter by remember {
//            derivedStateOf {
//                if (lazyListState.firstVisibleItemIndex == 0) {
//                    (imageHeight - dp(lazyListState.firstVisibleItemScrollOffset) - imagePoint) / imageHeight
//                } else 0f
//            }
//        }

//        val imageBrush by remember {
//            derivedStateOf {
//                Log.d("!!", "imageBrushCenter: $imageBrushCenter    imageBrushBottom: $imageBrushBottom")
//                Brush.verticalGradient(
//                    0f to Color.Transparent,
//                    imageBrushCenter to Color.Transparent,
//                    imageBrushBottom to Color.Black,
//                    1f to Color.Black,
////                    startY = 0f,
////                    endY = 1f
//                )
//            }
//        }

//        fun drawBrush(): Brush =
//            Brush.verticalGradient(
//                0f to Color.Transparent,
//                imageBrushCenter to Color.Transparent,
//                imageBrushBottom to Color.Black,
//                1f to Color.Black,
////                    startY = 0f,
////                    endY = 1f
//            )


        Box {
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
//                Spacer(
//                    modifier = Modifier
////                    .background(imageBrush)
////                    .drawBehind { BrushTest() }
//                        .align(Alignment.BottomCenter)
//                        .background(Color.Black)
//                        .fillMaxWidth()
//                        .height(spacerHeight)
//                )

//                BrushCanvas(imageBrushCenter, imageBrushBottom)
            }

            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
//                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp,
                    bottom = WindowInsets.systemBars.asPaddingValues()
                        .calculateBottomPadding() + 8.dp,
                ),
//                modifier = Modifier.padding(paddingValues),
//                .padding(horizontal = 8.dp),
                state = lazyListState
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

                    var openState by remember {
                        mutableStateOf(false)
                    }

                    var annotationState by remember {
                        mutableStateOf(AnnotationState.CLOSE)
                    }

                    Card(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
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
                        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {

                            FlatTextField(
                                value = if (paragraphContent.text.first() == '^') {
                                    paragraphContent.copy(
                                        text = paragraphContent.text.removeRange(
                                            0,
                                            1
                                        )
                                    )
                                } else paragraphContent,
                                onValueChange = { viewModel.currentContent[paragraphIndex] = it },
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
                                        paragraphContent.text.first() == '^'
                                    } else false
                                }

                                val transition =
                                    updateTransition(targetState = isDone(), label = "transition")

                                val doneColor by transition.animateColor(label = "") {
                                    if (it) ColorDone else LocalContentColor.current
                                }
                                val doneAlpha by transition.animateFloat(label = "") {
                                    if (it) LocalContentAlpha.current else 0.80f
                                }
//                            val doneColor by animateColorAsState(targetValue = if (isDone()) ColorDone else LocalContentColor.current)
                                ClickableIcon(
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
                                    modifier = Modifier.alpha(doneAlpha),
                                    tint = doneColor
                                )

                                Spacer(modifier = Modifier
                                    .weight(1F)
                                    .focusable()
                                    .clickable { })

                                ClickableIcon(painter = painterResource(id = R.drawable.translation),
//                                tint = color.value,
                                    onClick = {
                                        annotationState =
                                            if (annotationState == AnnotationState.TRANSLATION) AnnotationState.CLOSE
                                            else AnnotationState.TRANSLATION

//                                    viewModel.translation(paragraph.text)
//                                    viewModel.translateTest(context, paragraph.text)
//                                    translate(context)
                                    })

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
                                ClickableIcon(painter = painterResource(id = R.drawable.word),
                                    enabled = wordIconState,
                                    modifier = Modifier.focusable(),
                                    onClick = {

                                        // 清除上一次選重的單字
                                        viewModel.noCurrentWord()

                                        // 1.檢測是否選取單字
//                                    val contentText = paragraphContent.getSelectedText().text
                                        annotationState = if (contentText.isNotBlank()) {

//                                        viewModel.curr

                                            viewModel.addWordListTable(contentText, paragraphIndex)

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
                                    })
                                AnimatedContent(targetState = openState) {
                                    ClickableIcon(painter = painterResource(id = getRid(it)),
                                        onClick = {
                                            openState = !openState
                                        })
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
                                                        wordList[paragraphIndex].value =
//                                                    wordList[index].value =
                                                            word.value.copy(chinese = it)
                                                    },
                                                    remove = {
                                                        Log.d(
                                                            "!!",
                                                            viewModel.wordListTable[paragraphIndex].toList()
                                                                .toString() + index.toString()
                                                        )
//                                                    Log.d("!!", viewModel.wordListTable[index].toList().toString())
                                                        viewModel.wordListTable[paragraphIndex].remove(
                                                            word.value.id
                                                        )
//                                                    viewModel.wordListTable[index].remove(word.value.id)
                                                        Log.d(
                                                            "!!",
                                                            viewModel.wordListTable[paragraphIndex].toList()
                                                                .toString()
                                                        )
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
                }
            }

            // StatusBar 遮罩
            Spacer(modifier = Modifier
                .drawBehind { drawRect(getColor()) }
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding())
            )
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