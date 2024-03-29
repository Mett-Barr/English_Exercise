package com.example.english.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = LightPrimary,
    primaryVariant = DarkPrimary,
    secondary = DarkPrimary
//    secondary = LightSecondary
//    secondary = DarkSecondary
//    primary = ColorDone,
//    primaryVariant = PrimaryVariant,
//    secondary = Secondary
)

private val LightColorPalette = lightColors(
    primary = DarkPrimary,
    primaryVariant = LightPrimary,
    secondary = LightPrimary
//    secondary = DarkSecondary
//    secondary = LightSecondary
//    primary = PrimaryVariant,
//    primaryVariant = ColorDone,
//    secondary = Secondary

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun EnglishTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = DarkColorPalette
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}