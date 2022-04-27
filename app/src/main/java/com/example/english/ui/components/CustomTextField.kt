package com.example.english.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FlatTextField(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textLabel: String = "",
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    val context = LocalContext.current
    var text by remember {
        mutableStateOf("1\n2\n3\n4")
    }
    Card(modifier = Modifier
        .fillMaxWidth()
//        .wrapContentHeight()
//        .padding(8.dp)
        .background(MaterialTheme.colors.background)
        .then(modifier)
        .clip(RoundedCornerShape(8.dp)),
        elevation = 0.dp
    ) {
//        val label = @Composable { Text(textLabel) }
        TextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            maxLines = maxLines,
            shape = RoundedCornerShape(0.dp),
            placeholder = @Composable { Text(text = textLabel) }
        )
    }
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textLabel: String = "",
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit) {
    TextField(value = textFieldValue, onValueChange = onValueChange)
}