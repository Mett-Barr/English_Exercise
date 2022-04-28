package com.example.english.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun IconTemplate(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(50))
            .clickable { onClick.invoke() }
            .padding(8.dp)
            .size(24.dp)
            .then(modifier)
    )
}

@Composable
fun IconTemplate(
    painter: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(50))
            .clickable { onClick.invoke() }
            .padding(8.dp)
            .size(24.dp)
            .then(modifier)
    )
}

