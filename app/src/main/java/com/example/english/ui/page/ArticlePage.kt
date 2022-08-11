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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.data.word.word.room.EmptyWord
import com.example.english.data.word.word.room.Word
import com.example.english.ui.components.ClickableIcon
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.WordComponent
import com.example.english.ui.theme.ColorDone


enum class AnnotationState {
    TRANSLATION, WORDS, CLOSE
}

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class
)
@Composable
fun NewsArticlePage(viewModel: MainViewModel, title: String, navController: NavController) {
//    val activity = LocalContext.current as Activity
    val context = LocalContext.current

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


    val brush = Brush.verticalGradient(listOf(Color(0f, 0f, 0f, 0f),
        MaterialTheme.colors.surface.copy(alpha = 1f)))



    Scaffold(
        bottomBar = {
//            BottomAppBar {
//                Row(
//                    modifier = Modifier
//                        .wrapContentHeight()
//                        .height(64.dp)
//                        .align(Alignment.CenterVertically)
//                ) {
//                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
//                        ClickableIcon(painter = painterResource(R.drawable.back)) {
//                            dispatcher.onBackPressed()
//                        }
//                    }
//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1F)
//                    )
//                    ClickableIcon(painter = painterResource(R.drawable.delete), enabled = false) {
//                        deleteArticleDialog = true
//                    }
//                    ClickableIcon(painter = painterResource(R.drawable.lable), enabled = false)
//                    ClickableIcon(painter = painterResource(R.drawable.edit), enabled = false)
//                }
//            }
        },

        modifier = hideKeyboardModifier

    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues.,
//            contentPadding = paddingValues,
            contentPadding = PaddingValues(
//                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp,
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 8.dp,
            ),
            modifier = Modifier
                .padding(paddingValues)
//                .padding(horizontal = 8.dp)
        ) {
            item {
                Box {
                    Crossfade(targetState = viewModel.currentImage) {
                        if (it != null) {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(16f / 9f)
                                    .fillMaxWidth()
//                                    .blur(8.dp)
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
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
//                        .background(MaterialTheme.colors.background)
//                        .animateItemPlacement(TweenSpec())
//                        .then(hideKeyboardModifier)
                ) {
                    Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {

                        FlatTextField(
                            value = if (paragraphContent.text.first() == '^') {
                                paragraphContent.copy(text = paragraphContent.text.removeRange(0,
                                    1))
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
                                onValueChange = { viewModel.currentContentCn[paragraphIndex] = it },
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
                            ClickableIcon(painter = painterResource(id = R.drawable.done_broad),
                                onClick = {
//                                    currentParagraphIndex = paragraphIndex
//                                    curr；entParagraphContent = paragraphContent.text
//                                    deleteParagraphDialog = true
                                    viewModel.changeDoneState(paragraphIndex)
                                    Log.d("!!",
                                        "NewsArticlePage: \n${paragraphContent.text}\n${paragraphContent.text.firstOrNull()}")
                                },
                                modifier = Modifier.alpha(doneAlpha),
                                tint = doneColor)

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
                                derivedStateOf { contentText.isNotBlank() || viewModel.wordListTable[paragraphIndex].size != 0 }
                            }
                            ClickableIcon(painter = painterResource(id = R.drawable.word),
                                enabled = wordIconState,
                                modifier = Modifier.focusable(),
                                onClick = {
                                    // 1.檢測是否選取單字
//                                    val contentText = paragraphContent.getSelectedText().text
                                    annotationState = if (contentText.isNotBlank()) {

                                        viewModel.addWordListTable(contentText, paragraphIndex)

                                        viewModel.currentContent[paragraphIndex] =
                                            viewModel.currentContent[paragraphIndex].copy(
                                                selection = TextRange.Zero
                                            )

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
                                    Card(elevation = 4.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .fillMaxWidth()) {
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

                                    LazyColumn(modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .focusable()
                                        .animateContentSize()
                                        .heightIn(max = 300.dp)) {

//                                        items(wordList, key = { it -> it }) { word ->
//                                            WordComponent(word = word,
//                                                onValueChange = {
//                                                    wordList[index].value =
//                                                        word.value.copy(chinese = it)
//                                                },
//                                                remove = {
//                                                    viewModel.wordListTable[index].remove(word.value.id)
//                                                },
//                                                updateWord = { viewModel.updateWord(word.value) },
//                                                updateChinese = {
//                                                    viewModel.updateWord(Word(word.value.id,
//                                                        word.value.english,
//                                                        it))
//                                                })
//                                        }

                                        itemsIndexed(wordList) { index, word ->
                                            WordComponent(word = word,
                                                onValueChange = {
                                                    wordList[index].value =
                                                        word.value.copy(chinese = it)
                                                },
                                                remove = {
                                                    viewModel.wordListTable[paragraphIndex].remove(
                                                        word.value.id)
                                                },
                                                updateWord = { viewModel.updateWord(word.value) })
                                        }
                                    }

//                                    Column(modifier = Modifier
//                                        .padding(bottom = 8.dp)
////                                        .animateContentSize()
//                                        .focusable()) {
//
//                                        wordList.forEachIndexed { index, word ->
//                                            WordComponent(word = word,
//                                                onValueChange = {
//                                                    wordList[index].value =
//                                                        word.value.copy(chinese = it)
//                                                },
//                                                remove = {
//                                                    viewModel.wordListTable[index].remove(word.value.id)
//                                                },
//                                                updateWord = { viewModel.updateWord(word.value) },
//                                                updateChinese = {
//                                                    viewModel.updateWord(Word(word.value.id,
//                                                        word.value.english,
//                                                        it))
//                                                })
//                                        }
//                                    }

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