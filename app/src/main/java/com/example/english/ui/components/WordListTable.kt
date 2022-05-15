package com.example.english.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.english.MainViewModel
import com.example.english.data.word.word.room.EmptyWord

@Composable
fun WordListTable(list: List<Int>, viewModel: MainViewModel) {
    Column {
        list.forEach {
            val word = viewModel.getWordById(it).collectAsState(initial = EmptyWord.word)
            Row {
                Text(text = word.value.english, modifier = Modifier.weight(1F))
                Text(text = it.toString(), modifier = Modifier.weight(1F))
            }
        }
    }
}