package com.example.english.ui.components.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.english.R

//@Preview
@Composable
fun BookMark(modifier: Modifier, color: Color = MaterialTheme.colors.primary) {

    Box(modifier = modifier.requiredSize(30.dp, 48.dp), contentAlignment = Alignment.Center) {
        Spacer(modifier = Modifier
//            .size(48.dp, 96.dp)
            .fillMaxSize()
            .drawBehind {
                val path = Path().also {
                    it.moveTo(0f, 0f)
                    it.lineTo(0f, this.drawContext.size.height * .75f)
                    it.lineTo(
                        this.drawContext.size.width * .5f,
                        this.drawContext.size.height * 1f
                    )
                    it.lineTo(this.drawContext.size.width, this.drawContext.size.height * .75f)
                    it.lineTo(this.drawContext.size.width, 0f)
                    it.close()
                }

                drawPath(
                    path = path,
                    color = color
                )
            })

        Icon(
            painter = painterResource(id = R.drawable.done_broad),
            contentDescription = "done",
            modifier = Modifier.padding(horizontal = 3.dp),
            tint = MaterialTheme.colors.onPrimary
        )
    }

//    Canvas(modifier = Modifier.requiredSize(48.dp, 96.dp)) {
//        val path = Path().also {
//            it.moveTo(0f, 0f)
//            it.lineTo(0f, this.drawContext.size.height * .75f)
//            it.lineTo(this.drawContext.size.width * .5f, this.drawContext.size.height * 1f)
//            it.lineTo(this.drawContext.size.width, this.drawContext.size.height * .75f)
//            it.lineTo(this.drawContext.size.width, 0f)
//            it.close()
//        }
//
//        drawPath(
//            path = path,
//            color = color
//        )
//    }
}

@Composable
fun CanvasTest() {
    Canvas(modifier = Modifier.wrapContentSize()) {
        val path = Path().also {
            it.moveTo(0f, 0f)
            it.lineTo(0f, this.drawContext.size.height * .75f)
            it.lineTo(this.drawContext.size.width * .5f, this.drawContext.size.height * 1f)
            it.lineTo(this.drawContext.size.width, this.drawContext.size.height * .75f)
            it.lineTo(this.drawContext.size.width, 0f)
            it.close()
        }

//        drawPath(
//            path = path,
////            color = color
//        )
    }

}