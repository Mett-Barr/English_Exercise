package com.example.english.ui.components.test

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OffsetTest() {

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

        LaunchedEffect(key1 = Unit) {
            scope.launch {
                while (true) {
                    delay(500)
                    offset.animateTo(Offset( (0..100).random().toFloat(), (0..100).random().toFloat()))
                }
            }
        }
        Box(modifier = Modifier
            .align(Alignment.Center)
            .size(100.dp)
            .background(Color.Gray)
            .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
        ) {
            Box(modifier = Modifier
                .align(Alignment.Center)
                .size(20.dp)
                .background(Color.Black))
        }
    }
}