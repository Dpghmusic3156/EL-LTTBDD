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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitap4_2.model.ForgetPasswordData
import com.example.baitap4_2.ui.components.CustomInputField
import com.example.baitap4_2.ui.components.HeaderLogoSection
import com.example.baitap4_2.ui.components.IconType
import com.example.baitap4_2.ui.components.PrimaryButton
import com.example.baitap4_2.ui.components.ResultDataCard

@Composable
fun ForgetPasswordScreen(
    submittedData: ForgetPasswordData?,
    onNext: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderLogoSection(showBackButton = false)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Forget Password?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your Email, we will send you a verification code.",
            fontSize = 14.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Your Email",
            leadingIconType = IconType.EMAIL,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = "Next",
            onClick = { onNext(email) }
        )

        Spacer(modifier = Modifier.weight(1f))

        if (submittedData != null) {
            ResultDataCard(data = submittedData)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
