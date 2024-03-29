package com.example.english.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


//@Composable
//fun ClickableIcon(
//    modifier: Modifier,
//    imageVector: ImageVector,
//    contentDescription: String? = null,
//    clickable: Boolean = true,
//    onClick: () -> Unit = {},
//) {
//    Icon(
//        imageVector = imageVector,
//        contentDescription = contentDescription,
//        modifier = Modifier
//            .padding(4.dp)
//            .clip(RoundedCornerShape(50))
//            .clickable(clickable) { onClick.invoke() }
//            .padding(8.dp)
//            .size(24.dp)
//            .then(modifier)
//    )
//}

@Composable
fun SelectableIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    enabled: Boolean = true,
    isSelected: Boolean = true,
    normalColor: Color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
    selectedColor: Color = MaterialTheme.colors.primary,
//    selectedColor: Color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
//    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: (() -> Unit)? = null,
) {
    val contentAlpha by animateFloatAsState(if (enabled) ContentAlpha.high else ContentAlpha.disabled)


    val backgroundColor by animateColorAsState(targetValue = if (enabled && isSelected) selectedColor else Color.Transparent)

//    val tint by animateColorAsState(
//        targetValue = MaterialTheme.colors.contentColorFor(
//            backgroundColor
//        )
//    )
    val tint by animateColorAsState(targetValue = if (enabled && isSelected) MaterialTheme.colors.background else MaterialTheme.colors.contentColorFor(MaterialTheme.colors.background))
//    val tint by animateColorAsState(targetValue = if (enabled && isSelected) selectedColor else normalColor)


    val clickModifier = if (onClick != null) {
        modifier
            .clickable(
                enabled = enabled,
                indication = rememberRipple(
                    bounded = false,
                    radius = 20.dp
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick.invoke() }
    } else modifier

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = clickModifier
            .alpha(contentAlpha)

            .padding(8.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(4.dp)
//            .padding(12.dp)
            .size(24.dp),
        tint = tint
    )
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
    onClick: (() -> Unit)? = null,
) {
    val contentAlpha by animateFloatAsState(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
//    val contentAlpha by animateFloatAsState(if (enabled) LocalContentAlpha.current else ContentAlpha.disabled)

//    val color = remember {
//        Animatable(Color.Black)
//    }

    // .padding(4.dp).clip(CircleShape)

//    val mutableInteractionSource = MutableInteractionSource()

    val clickModifier = if (onClick != null) {
        modifier
            .clickable(
                enabled = enabled,
                indication = rememberRipple(
                    bounded = false,
                    radius = 20.dp
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick.invoke() }
    } else modifier

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = clickModifier
//            .padding(4.dp)

//            .clip(RoundedCornerShape(50))
//            .clickable(enabled) { onClick.invoke() }
//            .then(clickModifier)
            .alpha(contentAlpha)

            .padding(12.dp)
            .size(24.dp),
//            .then(clickModifier),
        tint = tint
    )
}

