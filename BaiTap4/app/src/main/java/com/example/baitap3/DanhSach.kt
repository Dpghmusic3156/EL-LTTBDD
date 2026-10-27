package com.example.baitap4

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DanhSach(
    onOpen: (Int) -> Unit
) {
    KhungManHinh(title = "UI Components List") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionTitle(title = "Display")
            BaiItemCard(
                title = "Text",
                subtitle = "Displays text",
                onClick = { onOpen(2) }
            )
            BaiItemCard(
                title = "Image",
                subtitle = "Displays an image",
                onClick = { onOpen(3) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            SectionTitle(title = "Input")
            BaiItemCard(
                title = "TextField",
                subtitle = "Input field for text",
                onClick = { onOpen(4) }
            )
            BaiItemCard(
                title = "PasswordField",
                subtitle = "Input field for passwords",
                onClick = { onOpen(6) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            SectionTitle(title = "Layout")
            BaiItemCard(
                title = "Column",
                subtitle = "Arranges elements vertically",
                onClick = { onOpen(7) }
            )
            BaiItemCard(
                title = "Row",
                subtitle = "Arranges elements horizontally",
                onClick = { onOpen(5) }
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
}

@Composable
private fun BaiItemCard(
    title: String,
    subtitle: String,
    backgroundColor: Color = Color(0xFFD6E8FA),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF444444)
            )
        }
    }
}
