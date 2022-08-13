package com.example.english.ui.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.english.ui.theme.ColorDone
import com.example.english.ui.theme.ColorDone2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

const val NUMBER = 8

@Preview
@Composable
fun Movement() {

    val coroutineScope = rememberCoroutineScope()

    fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

    val randomValue = { (0..255).random() }

    val colorList = remember {
        MutableList(NUMBER) { mutableStateOf(Color.DarkGray) }.toList()
    }

    fun newColors(): Color {
        return when ((1..3).random()) {
            1 -> ColorDone2
            2 -> Color(140,140,140)
            3 -> Color(80, 80, 80)
            else -> ColorDone
        }
    }

    fun randomColor() {
//        colorList.forEach { it.value = Color(randomValue(), randomValue(), randomValue()) }
        colorList.forEach { it.value = newColors() }
    }



    val sizeList = remember {
        MutableList(NUMBER) { mutableStateOf(0.dp) }.toList()
    }


    val randomSize = {
        sizeList.forEach { it.value = (12..24).random().dp }
    }




    val scaleList = remember {
        MutableList(NUMBER) { mutableStateOf(1F)}.toList()
    }

    val randomScale = {
        scaleList.forEach { it.value = Random.nextFloat() / 2 + 0.75f }
    }


    var width by remember {
        mutableStateOf(0)
    }
    var height by remember {
        mutableStateOf(0)
    }

    val state by remember {
        derivedStateOf {
            width != 0 && height != 0
        }
    }

    Box(modifier = Modifier
//        .background(MaterialTheme.colors.background)
//        .background(Color.White)
        .fillMaxSize()
        .onGloballyPositioned {
            width = it.size.width
            height = it.size.height
//            state = true
//            Log.d("!!!", "onGloballyPositioned ")
        }
    ) {

        if (state) {

            val offset = remember {
                MutableList(NUMBER) {
                    Animatable(Offset(width.toFloat() / 2, height.toFloat() / 2),
                        Offset.VectorConverter)
                }.toList()
            }

            fun random() {
                offset.forEach {
                    coroutineScope.launch {
                        delay((0L..200L).random())
                        it.animateTo(
                            Offset(
                                (Random.nextFloat() * 0.5f + 0.25f) * width,
                                (Random.nextFloat() * 0.5f + 0.25f) * height
                            ),
                            spring(Spring.DampingRatioHighBouncy, 5f)
                        )
                    }
                }
            }

            repeat(NUMBER) {

                val animatedSizeIn by animateDpAsState(
                    targetValue = sizeList[it].value,
                    spring(Spring.DampingRatioHighBouncy, Spring.StiffnessVeryLow)
                )

                val animatedColorIn by animateColorAsState(
                    targetValue = colorList[it].value,
                    spring(Spring.DampingRatioHighBouncy, 5f)
                )

                val animatedScale by animateFloatAsState(
                    targetValue = scaleList[it].value,
                    spring(Spring.DampingRatioHighBouncy, Spring.StiffnessVeryLow)
                )




                Spacer(
                    modifier = Modifier
                        .offset {
                            offset[it].value.toIntOffset()
                        }
                        .size(18.dp)
                        .scale(animatedScale)
                        .clip(RoundedCornerShape(100))
                        .background(animatedColorIn)
                )
            }

//        }

            LaunchedEffect(state) {

                coroutineScope.launch {
                    while (true) {
                        delay(300)
                        random()
                        randomColor()
                        randomSize()
                        randomScale()
                    }
                }

            }

        }

    }


//    LaunchedEffect(key1 = Unit) {
//        coroutineScope.launch {
//            while (true) {
//                delay(400)
////                randomOffset()
////                random()
//                randomColor()
//                randomSize()
//            }
//        }
//    }
}
