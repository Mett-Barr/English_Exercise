package com.example.english.ui.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun TestPage01(list: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(list) {
            if (it != "\n" && it != "\n\n" && it != "\n\n\n" && it != "") {

                Row(modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .background(Color.Gray)) {
                    Card(modifier = Modifier
                        .weight(1F)
//                        .height(IntrinsicSize.Max)
                        .padding(8.dp)) {
                        Text(text = it, modifier = Modifier
                            .fillMaxHeight()
                            .padding(4.dp))
                    }
                    Card(modifier = Modifier
                        .weight(1F)
//                        .height(IntrinsicSize.Max)
                        .padding(8.dp)) {
                        Text(text = randomLine(), modifier = Modifier
                            .fillMaxHeight()
                            .padding(4.dp))
                    }
                }
            }
        }
    }
}

fun randomLine(): String {

    var string = "".apply {
        repeat((4..6).random()) {
            plus("123\n")
//        Log.d("!!!", "randomLine: $it")
        }
//        Log.d("!!!", "randomLine: $this")
    }

    repeat((6..9).random()) {
        string += "$it\n"
    }

    var string2 = "".run {
        repeat((6..10).random()) {
            plus("123\n")
//        Log.d("!!!", "randomLine: $it")
        }
//        Log.d("!!!", "randomLine 1: $this")
    }
    Log.d("!!!", "randomLine 2: $string")

    return string
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TestPast() {
    var text by remember {
        mutableStateOf("123\n234")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        TextField(value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .align(Alignment.Center),
            keyboardActions = KeyboardActions()
        )
    }
}
