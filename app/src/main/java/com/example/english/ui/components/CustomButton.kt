package com.example.english.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(modifier: Modifier, text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .size(48.dp)
            .then(modifier),

        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            Modifier.padding(vertical = 1.dp, horizontal = 8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                letterSpacing = 0.15.sp,
//                background = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
fun PrototypeButton() {
    Button(
        onClick = {},
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "123",
            Modifier.padding(vertical = 1.dp, horizontal = 8.dp),
            style = TextStyle(
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                background = Color.Transparent
            )
        )
    }
}
