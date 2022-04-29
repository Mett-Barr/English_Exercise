package com.example.english.ui.page

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.R
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.IconTemplate

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsArticlePage(viewModel: MainViewModel, title: String) {

    fun getRid(boolean: Boolean): Int = if (boolean) R.drawable.arrow_less else R.drawable.arrow_more

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Text(text = viewModel.currentContent.size.toString())
        }
        itemsIndexed(viewModel.currentContent) { index, paragraphs ->

            var openState by remember {
                mutableStateOf(false)
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
                        value = paragraphs,
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
                        Spacer(modifier = Modifier.weight(1F))

                        IconTemplate(painter = painterResource(id = R.drawable.words),
                            onClick = { })
                        IconTemplate(painter = painterResource(id = R.drawable.edit), onClick = { })
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