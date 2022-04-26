package com.example.english.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.ui.components.FlatTextField

@Composable
fun InsertPage(viewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {

        var title by remember {
            mutableStateOf(
                "123\n" +
                        "  234\n" +
                        "  34545"
            )
        }

        var content by remember {
            mutableStateOf("")
        }

//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()) {
//
//        }

        // Title
        FlatTextField(
            textFieldValue = viewModel.textFieldValue,
            maxLines = 5
        ) { viewModel.textFieldValue = it }

        // Content
        FlatTextField(
            textFieldValue = viewModel.textFieldValue, modifier = Modifier
                .fillMaxSize()
                .weight(1F)
        ) { viewModel.textFieldValue = it }

//        TextField(
//            value = title, onValueChange = { title = it }, maxLines = 5, modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//        )
//        TextField(
//            value = content, onValueChange = { content = it}, modifier = Modifier
//                .weight(1F)
//                .fillMaxSize()
//        )
    }
}