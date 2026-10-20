package com.example.baitap3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextDetail(
    onBack: () -> Unit
) {
    KhungManHinh(title = "Text Detail", onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // quick Brown
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "The ",
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Text(
                    text = "quick",
                    fontSize = 28.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = " Brown",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB5651D)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // fox j u m p s over
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "fox ",
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Text(
                    text = "j u m p s",
                    fontSize = 28.sp,
                    color = Color.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = " over",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // the lazy dog
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "the",
                    fontSize = 28.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = " lazy",
                    fontSize = 28.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Cursive
                )
                Text(
                    text = " dog.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
