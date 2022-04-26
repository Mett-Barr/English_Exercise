package com.example.english.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.english.MainViewModel
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.OperationButton
import com.example.english.ui.theme.Typography

@Composable
fun InsertPage(viewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {

        Text("News", style = Typography.h4, textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp, bottom = 4.dp))

        // Title
        FlatTextField(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            textFieldValue = viewModel.textFieldValue,
            maxLines = 5,
            textLabel = "Title:"
        ) { viewModel.textFieldValue = it }

        // Content
        FlatTextField(
            textFieldValue = viewModel.textFieldValue,
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .padding(top = 4.dp, bottom = 4.dp),
            textLabel = "Content:"
        ) { viewModel.textFieldValue = it }


        OperationButton()
    }
}

