package com.example.english.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.data.word.word.room.Word
import kotlin.math.roundToInt

@Composable
fun DictionaryPage(list: List<State<Word>>) {
    LazyColumn() {
        items(list) {
            DictionaryWord(word = it, onValueChange = {}, updateWord = {})
        }
    }
}

@Composable
fun DictionaryWord(
    word: State<Word>,
    onValueChange: (String) -> Unit,
    updateWord: () -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(4.dp)
        ) {
            Text(
                text = word.value.english,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
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
                onValueChange = {
                    onValueChange.invoke(it)
//                        currentWord.value = Word(word.value.id, word.value.english, it)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1F)
                    .padding(horizontal = 8.dp)
                    .onFocusChanged { updateWord.invoke() },
                textStyle = Typography().h6.copy(color = MaterialTheme.colors.onBackground),
                cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
            )
        }
    }
}