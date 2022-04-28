package com.example.english.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.SimpleTextField

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsArticlePage(viewModel: MainViewModel) {


    var textFieldValue by remember {
        mutableStateOf(TextFieldValue("test\ntest"))
    }

    var editable by remember { mutableStateOf(true) }

//    Surface(elevation = 4.dp, modifier = Modifier.background(MaterialTheme.colors.surface)) {
//    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Text(text = "Title")
        }
        items(20) {
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.Gray)
//                .padding(8.dp)) {
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                ) {
//                    Spacer(modifier = Modifier.size(20.dp))
//                    Text("123\n234\n345\n")
//
//                    AnimatedVisibility(visible = viewModel.animTest) {
//                        Text(text = "Edit")
//                    }
//                }
//
//                Button(
//                    onClick = { viewModel.animTest = !viewModel.animTest },
//                    modifier = Modifier.align(
//                        Alignment.BottomEnd
//                    )
//                ) {
//                    Text("test")
//                }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.background)
//                    .padding(8.dp)
//                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    FlatTextField(
                        value = viewModel.draftContent,
                        onValueChange = { viewModel.draftContent = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(visible = viewModel.animTest) {
                        FlatTextField(
                            value = viewModel.draftContent,
                            onValueChange = { viewModel.draftContent = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.weight(1F))
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = "Arrow Drop Down"
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = "Arrow Drop Down"
                        )
                        Button(
                            onClick = { viewModel.animTest = !viewModel.animTest },
                        ) {
                            Text("test")
                        }
                    }
                }
            }
        }
    }

}