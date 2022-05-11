package com.example.english.ui.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.data.word.addWordInList
import com.example.english.data.word.word.room.EmptyWord
import com.example.english.data.word.word.room.Word
import com.example.english.data.word.wordlist.room.EmptyWordList
import com.example.english.data.word.wordlist.stringToItem
import com.example.english.data.word.wordlist.wordListToPage
import com.example.english.ui.components.ClickableIcon
import com.example.english.ui.components.FlatTextField
import kotlinx.coroutines.launch


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

    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    val hideKeyboardModifier = Modifier.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) {
        focusManager.clearFocus()
        keyboardController?.hide()
    }


//    fun hideKeyboard() {
//        focusManager.clearFocus()
//        keyboardController?.hide()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.R)
//    fun hideKeyboardView() {
//        focusManager.clearFocus()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            activity.window.insetsController?.hide(android.view.WindowInsets.Type.ime())
//        }
//    }


    fun getRid(boolean: Boolean): Int =
        if (boolean) R.drawable.arrow_less else R.drawable.arrow_more

    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    fun popBack() {
        viewModel.saveCurrentFile(context)
        navController.popBackStack()
    }

    BackHandler {
        popBack()
        Log.d("!!!", "NewsArticlePage: BackHandler")
    }


    val wordList = viewModel.getCurrentWordList().collectAsState(initial = EmptyWordList.wordList)
    val wordListItemString by remember(wordList) {
        derivedStateOf {
            wordList.value.wordListItemString
        }
    }

//    val wordListForPage by remember(wordListItemString) {
//        derivedStateOf {
//            val wordListItem = stringToItem(wordListItemString)
//            wordListToPage(wordListItem, viewModel.currentContent.size)
//        }
//    }


    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .height(64.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    ClickableIcon(painter = painterResource(R.drawable.back)) {
                        dispatcher.onBackPressed()
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                    )
                    ClickableIcon(painter = painterResource(R.drawable.delete))
                    ClickableIcon(painter = painterResource(R.drawable.lable))
                    ClickableIcon(painter = painterResource(R.drawable.edit))
                }
            }
        },

        modifier = hideKeyboardModifier

    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues.,
            contentPadding = WindowInsets.systemBars.asPaddingValues(),
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            item {
                Text(
                    text = viewModel.currentTitle,
                    style = Typography().h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
            itemsIndexed(viewModel.currentContent) { index, paragraph ->

                var openState by remember {
                    mutableStateOf(false)
                }

                var annotationState by remember {
                    mutableStateOf(AnnotationState.CLOSE)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.background)
//                        .animateItemPlacement(TweenSpec())
//                        .then(hideKeyboardModifier)
                ) {
                    Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {

                        FlatTextField(
                            value = paragraph,
                            onValueChange = { viewModel.currentContent[index] = it },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                        )

                        AnimatedVisibility(visible = openState) {
                            FlatTextField(
                                value = viewModel.currentContentCn[index],
                                onValueChange = { viewModel.currentContentCn[index] = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }

                        val color = remember {
                            Animatable(Color.White)
                        }

                        AnimatedContent(targetState = annotationState) { it ->
                            when (it) {
                                AnnotationState.TRANSLATION -> {
                                    FlatTextField(
                                        value = viewModel.currentContentTr[index],
                                        onValueChange = {
                                            viewModel.currentContentTr[index] = it
                                        },
                                        readOnly = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    )

                                    LaunchedEffect(key1 = 1) {
                                        color.animateTo(Color.Yellow)
                                    }
                                }
                                AnnotationState.WORDS -> {
                                    Text(text = ("word\nstate"))

                                    LazyColumn() {
                                        items(viewModel.currentContentWordList[index].toList()) {
                                            Row {
                                                val word = viewModel.getWordById(it).collectAsState(
                                                    initial = EmptyWord.word
                                                )
                                                Text(
                                                    text = word.value.english,
                                                    modifier = Modifier.weight(1F)
                                                )
                                                Text(
                                                    text = word.value.chinese,
                                                    modifier = Modifier.weight(1F)
                                                )
                                            }
                                        }
                                    }

                                    LaunchedEffect(key1 = 1) {
                                        color.animateTo(Color.White)
                                    }
                                }
                                AnnotationState.CLOSE -> {
                                    LaunchedEffect(key1 = 1) {
                                        color.animateTo(Color.White)
                                    }
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            ClickableIcon(painter = painterResource(id = R.drawable.delete),
                                onClick = {
                                    viewModel.removeCurrentParagraph(index)
                                })

                            Spacer(modifier = Modifier
                                .weight(1F)
                                .focusable()
                                .clickable { })

                            ClickableIcon(painter = painterResource(id = R.drawable.translation),
                                tint = color.value,
                                onClick = {
                                    annotationState =
                                        if (annotationState == AnnotationState.TRANSLATION) AnnotationState.CLOSE
                                        else AnnotationState.TRANSLATION

//                                    viewModel.translation(paragraph.text)
//                                    viewModel.translateTest(context, paragraph.text)
//                                    translate(context)
                                })
                            ClickableIcon(painter = painterResource(id = R.drawable.word),
                                onClick = {

                                    // 1.檢測是否選取單字
                                    val contentText =
                                        viewModel.currentContent[index].getSelectedText().text
                                    if (contentText.isNotBlank()) {
                                        // 2.translate並且開啟annotation欄位

                                        val newWord = Word(english = contentText)

                                        coroutineScope.launch {
                                            addWordInList(
                                                word = newWord,
                                                paragraphIndex = index,
                                                wordList = wordList.value,
                                                wordListItem = stringToItem(wordListItemString),
                                                wordRepository = viewModel.wordRepository,
                                                wordListRepository = viewModel.wordListRepository
                                            )
                                        }
                                        AnnotationState.WORDS
                                    } else {
                                        // 3.檢測開啟狀態，決定開關annotation欄位
                                        annotationState =
                                            if (annotationState == AnnotationState.WORDS) AnnotationState.CLOSE
                                            else AnnotationState.WORDS
                                    }


//                                    annotationState =
//                                        if (annotationState == AnnotationState.WORDS) AnnotationState.CLOSE
//                                        else AnnotationState.WORDS


//                                    viewModel.translation2(paragraph.text)
                                })
                            AnimatedContent(targetState = openState) {
                                ClickableIcon(painter = painterResource(id = getRid(it)),
                                    onClick = {
                                        openState = !openState
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class AnnotationState {
    TRANSLATION, WORDS, CLOSE
}
