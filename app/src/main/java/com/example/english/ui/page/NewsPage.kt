package com.example.english.ui.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.ui.components.ClickableIcon
import com.example.english.ui.components.FlatTextField

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NewsArticlePage(viewModel: MainViewModel, title: String, navController: NavController) {

//    val activity = LocalContext.current as Activity
    val context = LocalContext.current

//    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(modifier = Modifier
                    .wrapContentHeight()
                    .height(64.dp)
                    .align(Alignment.CenterVertically)) {
                    ClickableIcon(painter = painterResource(R.drawable.back)) {
                        dispatcher.onBackPressed()
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F))
                    ClickableIcon(painter = painterResource(R.drawable.delete))
                    ClickableIcon(painter = painterResource(R.drawable.lable))
                    ClickableIcon(painter = painterResource(R.drawable.edit))
                }
            }
        },

        modifier = hideKeyboardModifier

    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues.,
            contentPadding = WindowInsets.systemBars.asPaddingValues(),
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 8.dp)
        ) {
            item {
                Text(text = viewModel.currentContent.size.toString())
            }
            itemsIndexed(viewModel.currentContent) { index, paragraph ->

                var openState by remember {
                    mutableStateOf(false)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.background)
//                        .then(hideKeyboardModifier)
                ) {
                    Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {

                        FlatTextField(
                            value = paragraph,
                            onValueChange = { viewModel.currentContent[index] = it },
                            modifier = Modifier.fillMaxWidth()
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

                        Row(modifier = Modifier.fillMaxWidth()) {
                            ClickableIcon(painter = painterResource(id = R.drawable.delete),
                                onClick = { viewModel.currentContent.remove(paragraph) })

                            Spacer(modifier = Modifier
                                .weight(1F)
                                .focusable()
                                .clickable { })

                            ClickableIcon(painter = painterResource(id = R.drawable.translation),
                                onClick = { })
                            ClickableIcon(painter = painterResource(id = R.drawable.word),
                                onClick = { })
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