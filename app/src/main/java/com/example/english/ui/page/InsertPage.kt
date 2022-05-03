package com.example.english.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.english.MainViewModel
import com.example.english.article.defaultText.text
import com.example.english.stringconverter.StringConverter
import com.example.english.ui.components.FlatTextField
import com.example.english.ui.components.OperationButton
import com.example.english.ui.theme.Typography

@Composable
fun InsertPage(viewModel: MainViewModel, navController: NavController) {

    val context = LocalContext.current

    var openDialog by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {

        Text(
            "News",
            style = Typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 4.dp)
        )

        // Title
        FlatTextField(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            value = viewModel.draftTitle,
            maxLines = 5,
            textLabel = "Title:"
        ) { viewModel.draftTitle = it }

        // Content
        FlatTextField(
            value = viewModel.draftContent,
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .padding(top = 4.dp, bottom = 4.dp),
            textLabel = "Content:"
        ) { viewModel.draftContent = it }


        OperationButton(
            clickCancel = {
                navController.popBackStack()
            },
            clickOK = {
                openDialog = true
//                viewModel.addNews(context)
//                StringConverter().stringToListNull(viewModel.draftContent.text)
//                navController.popBackStack()
            }
        )
    }

    if (openDialog) {

//        val radioOptions = listOf("Tech", "Apple", "Google", "Meta","MircoSoft", "Amazon", "Netflix", "Tesla", "Elon Musk")
        val radioOptions = listOf("A", "S", "D")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

        var test = Test.A

        Dialog(onDismissRequest = { openDialog = false }) {
            Column(modifier = Modifier.selectableGroup()) {
//                radioOptions.forEach()
                for (tag in enumValues<Test>()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

enum class Test {
    A, S, D
}

