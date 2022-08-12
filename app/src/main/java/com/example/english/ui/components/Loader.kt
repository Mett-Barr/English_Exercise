package com.example.english.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Loader() {
    Canvas(modifier = Modifier.size(60.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val length = canvasHeight / 5

        drawCircle(Color.Yellow, center = Offset(canvasWidth / 2, canvasHeight / 2 + length), radius = 4F)
        drawCircle(Color.Yellow, center = Offset(canvasWidth / 2 + length * 0.95f, canvasHeight / 2 + length * 0.3f), radius = 4f)
        drawCircle(Color.Yellow, center = Offset(canvasWidth / 2 + length * 0.58f, canvasHeight / 2 - length * 0.81f), radius = 4f)
        drawCircle(Color.Yellow, center = Offset(canvasWidth / 2 - length * 0.58f, canvasHeight / 2 - length * 0.81f), radius = 4f)
        drawCircle(Color.Yellow, center = Offset(canvasWidth / 2 - length * 0.95f, canvasHeight / 2 + length * 0.3f), radius = 4f)
    }
}