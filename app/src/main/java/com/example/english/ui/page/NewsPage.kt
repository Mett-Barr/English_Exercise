package com.example.english.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.ui.components.FlatTextField

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsArticle(viewModel: MainViewModel) {


    var textFieldValue by remember {
        mutableStateOf(TextFieldValue("test\ntest"))
    }

    var editable by remember { mutableStateOf(true) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(text = "Title")
        }
        items(20) {
            Box(modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(8.dp)) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
//                .animateContentSize()
                ) {
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("123\n234\n345\n")

//                Text(text = "qwe\nwer\nert\n", modifier = Modifier.an)
//                if (viewModel.animTest) {
//                }


                    AnimatedVisibility(visible = viewModel.animTest) {
                        Text(text = "Edit")
                    }
                }


                Button(
                    onClick = { viewModel.animTest = !viewModel.animTest },
                    modifier = Modifier.align(
                        Alignment.BottomEnd
                    )
                ) {
                    Text("test")
                }
            }
        }
    }
}