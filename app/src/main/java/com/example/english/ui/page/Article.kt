package com.example.english.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Article() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Title test")
        Row(modifier = Modifier.fillMaxSize()) {
//            Text(text = "", modifier = Modifier.fillMaxSize(1f))
//            Text(text = "", modifier = Modifier.fillMaxSize(1f))
            TextField(value = "test", onValueChange = {}, modifier = Modifier.weight(1f))
            TextField(value = "test\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",
                onValueChange = {},
                modifier = Modifier.weight(1f))
        }
    }
}