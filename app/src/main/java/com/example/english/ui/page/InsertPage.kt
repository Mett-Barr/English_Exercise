package com.example.english.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.english.MainViewModel
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

    fun addNews() {
        viewModel.addNews(context)
        StringConverter().stringToListNull(viewModel.draftContent.text)
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

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
            }
        )
    }

    if (openDialog) {

        var radioOption by remember {
            mutableStateOf(Tag.TECH)
        }

        Dialog(onDismissRequest = { openDialog = false }) {
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
                Column(
                    modifier = Modifier
                        .selectableGroup()
                        .padding(horizontal = 4.dp)
                        .padding(top = 16.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Tag", style = Typography.h4, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    )
                    for (it in enumValues<Tag>()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .selectable(
                                    selected = (it == radioOption),
                                    onClick = {
                                        radioOption = it
                                        addNews()
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (it == radioOption),
                                onClick = null // null recommended for accessibility with screenreaders
                            )
                            Text(
                                text = it.tag,
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Tag(val tag: String) {
    TECH("Tech"), APPLE("Apple"), GOOGLE("Google"), META("Meta"),
    AMAZON("Amazon"), NETFLIX("Netflix"), TESLA("Tesla"),
    ELON_MUSK("Elon Musk")
}

