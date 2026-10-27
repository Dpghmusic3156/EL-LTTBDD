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
import com.example.baitap4_2.ui.components.CustomInputField
import com.example.baitap4_2.ui.components.HeaderLogoSection
import com.example.baitap4_2.ui.components.IconType
import com.example.baitap4_2.ui.components.PrimaryButton

@Composable
fun ConfirmScreen(
    initialEmail: String,
    initialOtp: String,
    initialPass: String,
    onBack: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var email by remember { mutableStateOf(initialEmail) }
    var otp by remember { mutableStateOf(initialOtp) }
    var pass by remember { mutableStateOf(initialPass) }

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
            text = "Confirm",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We are here to help you!",
            fontSize = 14.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        CustomInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            leadingIconType = IconType.PERSON
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomInputField(
            value = otp,
            onValueChange = { otp = it },
            placeholder = "OTP Code",
            leadingIconType = IconType.SMS,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomInputField(
            value = pass,
            onValueChange = { pass = it },
            placeholder = "Password",
            leadingIconType = IconType.LOCK,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Summit",
            onClick = { onSubmit(email, otp, pass) }
        )
    }
}
