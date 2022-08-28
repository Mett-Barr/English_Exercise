package com.example.english.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.english.R
import com.example.english.ui.theme.Primary
import com.example.english.ui.theme.PrimaryVariant

@Preview
@Composable
fun SegmentedControls() {

    var horizontalBias by remember { mutableStateOf(-1f) }
    val alignment by animateHorizontalAlignmentAsState(horizontalBias)

    Box (modifier = Modifier
        .clickable { horizontalBias *= -1 }
        .wrapContentSize()
        .height(intrinsicSize = IntrinsicSize.Min)) {
        Spacer(modifier = Modifier
            .size(48.dp)
            .background(PrimaryVariant)
            .align(alignment))
        Row {
            ClickableIcon(painter = painterResource(id = R.drawable.card_view))
            ClickableIcon(painter = painterResource(id = R.drawable.article))
//            Icon(painter = painterResource(id = R.drawable.card_view), contentDescription = "card view")
//            Icon(painter = painterResource(id = R.drawable.article), contentDescription = "article")
        }
    }
}

@Composable
fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<Alignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}