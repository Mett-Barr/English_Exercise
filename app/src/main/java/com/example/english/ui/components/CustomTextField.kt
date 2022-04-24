package com.example.english.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomTextField(

) {
    val context = LocalContext.current
    var text by remember {
        mutableStateOf("1")
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            if (text.contains("\n\n")) {
                Toast.makeText(context, "換行", Toast.LENGTH_SHORT).show()
            }
        },
        Modifier.fillMaxSize()
    )
}