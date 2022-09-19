package com.example.english.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.english.ui.theme.TextBackgroundAlphaLight

//const val TEXT_BACKGROUND_ALPHA = 0.058F

val TextFieldPadding = 16.dp

@Composable
fun FlatTextField(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textLabel: String = "",
    value: TextFieldValue,
    readOnly: Boolean = false,
    isError: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit,
) {
    val normalColor = MaterialTheme.colors.error
    val errorColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)

    val placeholderColor by remember(isError) {
        derivedStateOf {
            if (!isError) {
                errorColor
            } else {
                normalColor
            }
        }
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .then(modifier)
            .clip(RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = placeholderColor,
            backgroundColor =
            if (MaterialTheme.colors.isLight)
                MaterialTheme.colors.onSurface.copy(alpha = TextBackgroundAlphaLight)
            else TextFieldDefaults.textFieldColors().backgroundColor(true).value
//            backgroundColor = TextFieldDefaults.textFieldColors().backgroundColor(enabled = true).value
        ),
        textStyle = Typography().h6,
        maxLines = maxLines,
        shape = RoundedCornerShape(0.dp),
        readOnly = readOnly,
        isError = isError,
        placeholder = @Composable { Text(text = textLabel) },
    )
}

@Composable
fun FlatText(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    text: String,
) {

    SelectionContainer {
        Text(
            text = text,
            Modifier
                .fillMaxWidth()
                .then(modifier)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (MaterialTheme.colors.isLight)
                        MaterialTheme.colors.onSurface.copy(alpha = TextBackgroundAlphaLight)
                    else TextFieldDefaults.textFieldColors().backgroundColor(true).value
                )
                .padding(TextFieldPadding),

            style = Typography().h6
        )
    }
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textLabel: String = "",
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
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
        shape = RoundedCornerShape(0.dp),
    )
}