package com.example.baitap4_2.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitap4_2.model.ForgetPasswordData

@Composable
fun ResultDataCard(data: ForgetPasswordData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = Color(0xFFEBF3FE),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "email",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53935)
            )
            Text(
                text = data.email,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "otp",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53935)
            )
            Text(
                text = data.otp,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "pass",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53935)
            )
            Text(
                text = data.pass,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
            )
        }
    }
}
