package com.example.english.ui.components.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.example.english.R
import com.example.english.ui.components.ClickableIcon

@Preview
@Composable
fun PopupInfo() {
    Column(modifier = androidx.compose.ui.Modifier.wrapContentSize()) {
        Text("2022/10/10  Tech")
        Text("start  2022/10/11")
        Text("end 2022/10/11")
        Text("50%")
//        Text("2022/10/10  Tech")
        Row {
            ClickableIcon(painter = painterResource(id = R.drawable.delete))
            ClickableIcon(painter = painterResource(id = R.drawable.link))
        }
    }
}