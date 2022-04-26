package com.example.english.ui.components

import android.R
import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun OperationButton(clickOK: () -> Unit = {}, clickCancel: () -> Unit = {}) {
    val focusManager = LocalFocusManager.current

    Row(modifier = Modifier.fillMaxWidth()) {

        // cancel Button
        CustomButton(
            text = Resources.getSystem().getString(R.string.cancel),
            modifier = Modifier.weight(1f),
            color = Color.Gray,
        ) {
            focusManager.clearFocus()
            clickCancel.invoke()
        }

        Spacer(modifier = Modifier.size(8.dp))

        // OK Button
        CustomButton(
            text = Resources.getSystem().getString(R.string.ok),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colors.primary
        ) {
            focusManager.clearFocus()
            clickOK.invoke()
        }
    }
}