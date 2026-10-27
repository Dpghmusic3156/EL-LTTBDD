package com.example.baitap4_2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitap4_2.ui.components.CustomInputField
import com.example.baitap4_2.ui.components.HeaderLogoSection
import com.example.baitap4_2.ui.components.IconType
import com.example.baitap4_2.ui.components.PrimaryButton

@Composable
fun ResetPasswordScreen(
    email: String,
    otp: String,
    onBack: () -> Unit,
    onNext: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderLogoSection(showBackButton = true, onBackClick = onBack)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create new password",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your new password must be different form previously used password",
            fontSize = 14.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        CustomInputField(
            value = password,
            onValueChange = {
                password = it
                if (errorMessage.isNotEmpty()) errorMessage = ""
            },
            placeholder = "Password",
            leadingIconType = IconType.LOCK,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomInputField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                if (errorMessage.isNotEmpty()) errorMessage = ""
            },
            placeholder = "Confirm Password",
            leadingIconType = IconType.LOCK,
            isPassword = true
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                color = Color(0xFFE53935),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Next",
            onClick = {
                when {
                    password.isEmpty() || confirmPassword.isEmpty() -> {
                        errorMessage = "Vui lòng nhập đầy đủ mật khẩu!"
                    }
                    password != confirmPassword -> {
                        errorMessage = "Mật khẩu không trùng khớp!"
                    }
                    else -> {
                        errorMessage = ""
                        onNext(password)
                    }
                }
            }
        )
    }
}
