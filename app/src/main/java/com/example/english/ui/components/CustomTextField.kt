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
    value: TextFieldValue,
    readOnly: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .then(modifier)
            .clip(RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = Typography().h6,
        maxLines = maxLines,
        shape = RoundedCornerShape(0.dp),
        readOnly = readOnly,
        placeholder = @Composable { Text(text = textLabel) }
    )
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textLabel: String = "",
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .background(Color.Transparent)
            .then(modifier),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp),)
}