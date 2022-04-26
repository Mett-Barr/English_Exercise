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
    textFieldValue: TextFieldValue,
    maxLines: Int = Int.MAX_VALUE,
    onValueChange: (TextFieldValue) -> Unit
) {
    val context = LocalContext.current
    var text by remember {
        mutableStateOf("1\n2\n3\n4")
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colors.background)
        .then(modifier),
        elevation = 0.dp
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            Modifier.fillMaxWidth().background(Color.Transparent),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            maxLines = maxLines
        )
    }
}