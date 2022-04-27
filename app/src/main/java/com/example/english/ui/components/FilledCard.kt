package com.example.english.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun FilledCard() {
    Box(modifier = Modifier.size(300.dp)) {
        Box(modifier = Modifier.size(150.dp).align(Alignment.Center)) {

        }
    }
}

@Preview
@Composable
fun FilledCard2() {
    Box(modifier = Modifier.size(300.dp)) {
        Card(modifier = Modifier.size(150.dp).align(Alignment.Center)) {

        }
    }
}