package com.example.english.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun ClickableIcon(
    modifier: Modifier,
    imageVector: ImageVector,
    contentDescription: String? = null,
    clickable: Boolean = true,
    onClick: () -> Unit = {},
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(50))
            .clickable(clickable) { onClick.invoke() }
            .padding(8.dp)
            .size(24.dp)
            .then(modifier)
    )
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {},
) {
    val contentAlpha by animateFloatAsState(if (enabled) LocalContentAlpha.current else ContentAlpha.disabled)

//    val color = remember {
//        Animatable(Color.Black)
//    }

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(4.dp)

            .clip(RoundedCornerShape(50))
            .clickable(enabled) { onClick.invoke() }
            .alpha(contentAlpha)

            .padding(8.dp)
            .size(24.dp)
            .then(modifier),
        tint = tint
    )
}

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,

        ) {

    }
}