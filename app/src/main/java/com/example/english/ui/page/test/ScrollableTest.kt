package com.example.english.ui.page.test

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
//import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue



@Preview
@Composable
fun ScrollableTest() {
    val scrollableState = rememberScrollState()
    val density = LocalDensity.current

    val state = rememberLazyListState()

    fun dp(offset: Int): Dp = with(density) {
        Log.d("!!", "dp: $offset")
        offset.toDp()
    }

    val size by remember {
        derivedStateOf {
            dp(state.firstVisibleItemScrollOffset)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {


        LazyColumn(state = state, content = {

            items(listOf(1,1,1,1,1,1,1,1,1,1,1,1,1,1)) {
                Spacer(modifier = Modifier
                    .padding(50.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(200.dp))

            }

//            repeat(10) {
//
//                item {
//                }
//            }
        })
        Spacer(modifier = Modifier
            .align(Alignment.TopCenter)
            .background(Color.Red)
            .fillMaxWidth()
            .height(
                size
            )
//            .background(Color.Red)

//            .height(300.dp)
        )

//        Column(modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(state = scrollableState)) {
//
//            repeat(30) {
//                Card(modifier = Modifier
//                    .padding(48.dp)
//                    .fillMaxWidth()
//                    .height(200.dp)) {
//
//                }
//            }
//        }

    }

}