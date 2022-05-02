package com.example.english.ui.page

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.IconTemplate
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsArticlePage(viewModel: MainViewModel, title: String, navController: NavController) {

    val context = LocalContext.current

    fun getRid(boolean: Boolean): Int =
        if (boolean) R.drawable.arrow_less else R.drawable.arrow_more


    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    fun popBack() {
        viewModel.saveCurrentFile(context)
        navController.popBackStack()
    }

    BackHandler() {
        popBack()
    }

    Scaffold(
        bottomBar = {
//            BottomAppBar {
//                Text(text = "BottomAppBar")
//            }
        }

    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues(8.dp),
//            contentPadding = rememberInsetsPaddingValues(
//                insets = LocalWindowInsets.current.systemBars,
//                applyTop = true,
//                applyBottom = true
//            ),
//            contentPadding = it,
//            modifier = Modifier.padding(it)
        ) {
            item {
                Text(text = viewModel.currentContent.size.toString())
            }
            itemsIndexed(viewModel.currentContent) { index, paragraph ->

                var openState by remember {
                    mutableStateOf(false)
                }

                fun removeParagraph() {
                    viewModel.currentContent.remove(paragraph)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.background)
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
                            IconTemplate(painter = painterResource(id = R.drawable.delete),
                                onClick = { removeParagraph() })

                            Spacer(modifier = Modifier.weight(1F))

                            IconTemplate(painter = painterResource(id = R.drawable.translation),
                                onClick = { })
                            IconTemplate(painter = painterResource(id = R.drawable.word), onClick = { })
                            AnimatedContent(targetState = openState) {
                                IconTemplate(painter = painterResource(id = getRid(it)), onClick = {
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