package com.example.baitap4_2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIconType: IconType,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = placeholder, color = Color(0xFF94A3B8), fontSize = 14.sp)
        },
        leadingIcon = {
            when (leadingIconType) {
                IconType.EMAIL -> EmailIcon()
                IconType.LOCK -> LockIcon()
                IconType.PERSON -> PersonIcon()
                IconType.SMS -> SmsIcon()
            }
        },
        trailingIcon = if (isPassword) {
            {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { passwordVisible = !passwordVisible }
                        .padding(4.dp)
                ) {
                    EyeIcon(visible = passwordVisible)
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            disabledContainerColor = Color(0xFFF8FAFC),
            focusedBorderColor = Color(0xFF2F80ED),
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedTextColor = Color(0xFF1E293B),
            unfocusedTextColor = Color(0xFF1E293B)
        ),
        singleLine = true
    )
}
