package com.example.english.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.data.word.word.room.EmptyWord
import com.example.english.data.word.word.room.Word
import com.example.english.data.word.wordlist.WordListTable
import kotlinx.coroutines.flow.Flow

@Composable
fun WordListTable(
    list: SnapshotStateList<Int>,
    getWordById: (Int) -> Flow<Word>,
    updateWord: (Word) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 4.dp)
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            color = MaterialTheme.colors.onBackground
        )

        list.forEachIndexed { index, wordId ->
            val originWord by getWordById(wordId).collectAsState(initial = EmptyWord.word)
            var word by originWord
            var chineseState by remember {
                mutableStateOf(false)
            }

            fun getWord() {
                if (word.value == EmptyWord.word) {
                    chineseState = false
                }
            }

            fun update() {
                updateWord(word.value)
            }


            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Text(
                    text = word.value.english,
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = 8.dp),
                    style = Typography().h6
                )
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colors.onBackground
                )
                BasicTextField(
                    value = word.value.chinese,
                    onValueChange = { word.value = word.value.copy(chinese = it) },
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = 8.dp),
                    textStyle = Typography().h6.copy(color = MaterialTheme.colors.onBackground),
//                    textStyle = TextStyle(),
                    cursorBrush = SolidColor(MaterialTheme.colors.onBackground)
                )
//                Text(
//                    text = word.chinese,
//                    modifier = Modifier
//                        .weight(1F)
//                        .padding(horizontal = 8.dp),
//                    style = Typography().h6
//                )
            }

            if (index != list.lastIndex) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            color = MaterialTheme.colors.onBackground
        )
    }
}