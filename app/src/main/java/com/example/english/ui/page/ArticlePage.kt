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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.ui.components.ClickableIcon
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.WordListTable


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

    val coroutineScope = rememberCoroutineScope()

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
        viewModel.saveCurrentFile(context)
        navController.popBackStack()
    }

    BackHandler {
        popBack()
        Log.d("!!!", "NewsArticlePage: BackHandler")
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    var currentParagraphIndex by remember {
        mutableStateOf(0)
    }

    var currentParagraphContent by remember {
        mutableStateOf("")
    }

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
//            contentPadding = paddingValues,
            contentPadding = PaddingValues(
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp,
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 8.dp,
            ),
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
            itemsIndexed(viewModel.currentContent) { paragraphIndex, paragraphContent ->

                var openState by remember {
                    mutableStateOf(false)
                }

                var annotationState by remember {
                    mutableStateOf(AnnotationState.WORDS)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
//                        .background(MaterialTheme.colors.background)
//                        .animateItemPlacement(TweenSpec())
//                        .then(hideKeyboardModifier)
                ) {
                    Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {

                        FlatTextField(
                            value = paragraphContent,
                            onValueChange = { viewModel.currentContent[paragraphIndex] = it },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                        )

                        AnimatedVisibility(visible = openState) {
                            FlatTextField(
                                value = viewModel.currentContentCn[paragraphIndex],
                                onValueChange = { viewModel.currentContentCn[paragraphIndex] = it },
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
                                        value = viewModel.currentContentTr[paragraphIndex],
                                        onValueChange = {
                                            viewModel.currentContentTr[paragraphIndex] = it
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
                                    WordListTable(
                                        list = viewModel.wordListTable[paragraphIndex].toList(),
                                        viewModel = viewModel
                                    )

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

                        Row(modifier = Modifier.fillMaxWidth()) {
                            ClickableIcon(painter = painterResource(id = R.drawable.delete),
                                onClick = {
                                    currentParagraphIndex = paragraphIndex
                                    currentParagraphContent = paragraphContent.text
                                    openDialog = true
                                })

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
                            ClickableIcon(painter = painterResource(id = R.drawable.word),
                                modifier = Modifier.focusable(),
                                onClick = {
                                    // 1.檢測是否選取單字
                                    val contentText =
                                        paragraphContent.getSelectedText().text
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
                    }
                }
            }
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeCurrentParagraph(
                        currentParagraphIndex
                    )
                    openDialog = false
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {openDialog = false}) {
                    Text(text = "Cancel")
                }
            },
            title = {
                    Text(text = "Delete paragraph?")
            },
            text = {
                   Text(text = "Delete paragraph：\"$currentParagraphContent\"", )
            },
            properties = DialogProperties()
        )
    }
}