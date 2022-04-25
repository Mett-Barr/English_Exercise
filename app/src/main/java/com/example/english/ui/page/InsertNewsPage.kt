package com.example.english.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun InsertPage() {
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

        TextField(
            value = title, onValueChange = { title = it }, maxLines = 5, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        TextField(
            value = content, onValueChange = { content = it}, modifier = Modifier
                .weight(1F)
                .fillMaxSize()
        )
    }
}