package com.example.baitap4_2

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.baitap4_2.ui.theme.BaiTap4_2Theme

data class ForgetPasswordData(
    val email: String = "",
    val otp: String = "",
    val pass: String = ""
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap4_2Theme(darkTheme = false, dynamicColor = false) {
                BaiTap4_2()
            }
        }
    }
}

@Composable
fun BaiTap4_2() {
    val navController = rememberNavController()
    var submittedData by remember { mutableStateOf<ForgetPasswordData?>(null) }

    NavHost(
        navController = navController,
        startDestination = "forget_password"
    ) {
        composable("forget_password") {
            ForgetPasswordScreen(
                submittedData = submittedData,
                onNext = { email ->
                    val encodedEmail = Uri.encode(email)
                    navController.navigate("verification/$encodedEmail")
                }
            )
        }

        composable(
            route = "verification/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: "")
            VerificationScreen(
                email = email,
                onBack = { navController.popBackStack() },
                onNext = { otp ->
                    val encodedEmail = Uri.encode(email)
                    val encodedOtp = Uri.encode(otp)
                    navController.navigate("reset_password/$encodedEmail/$encodedOtp")
                }
            )
        }

        composable(
            route = "reset_password/{email}/{otp}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("otp") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: "")
            val otp = Uri.decode(backStackEntry.arguments?.getString("otp") ?: "")
            ResetPasswordScreen(
                email = email,
                otp = otp,
                onBack = { navController.popBackStack() },
                onNext = { password ->
                    val encodedEmail = Uri.encode(email)
                    val encodedOtp = Uri.encode(otp)
                    val encodedPass = Uri.encode(password)
                    navController.navigate("confirm/$encodedEmail/$encodedOtp/$encodedPass")
                }
            )
        }

        composable(
            route = "confirm/{email}/{otp}/{pass}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("otp") { type = NavType.StringType },
                navArgument("pass") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val initialEmail = Uri.decode(backStackEntry.arguments?.getString("email") ?: "")
            val initialOtp = Uri.decode(backStackEntry.arguments?.getString("otp") ?: "")
            val initialPass = Uri.decode(backStackEntry.arguments?.getString("pass") ?: "")
            ConfirmScreen(
                initialEmail = initialEmail,
                initialOtp = initialOtp,
                initialPass = initialPass,
                onBack = { navController.popBackStack() },
                onSubmit = { finalEmail, finalOtp, finalPass ->
                    submittedData = ForgetPasswordData(
                        email = finalEmail,
                        otp = finalOtp,
                        pass = finalPass
                    )
                    navController.navigate("forget_password") {
                        popUpTo("forget_password") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun KhungManHinh(
    title: String,
    onBack: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color(0xFF2196F3),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (onBack != null) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = "‹",
                        fontSize = 32.sp,
                        color = Color(0xFF2196F3)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            content = content
        )
    }
}

@Composable
fun HeaderLogoSection(
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp)
    ) {
        if (showBackButton) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEBF3FE))
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                BackArrowIcon(tint = Color(0xFF2F80ED))
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.uth),
                contentDescription = "UTH Logo",
                modifier = Modifier
                    .height(60.dp)
                    .width(100.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "SmartTasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F80ED)
            )
        }
    }
}

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

@Composable
fun VerificationScreen(
    email: String,
    onBack: () -> Unit,
    onNext: (String) -> Unit
) {
    var otpCode by remember { mutableStateOf("") }

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
            text = "Verify Code",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter the code we just sent you on your Registered Email",
            fontSize = 14.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OtpInputField(
            otpCode = otpCode,
            onOtpChange = { otpCode = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Next",
            onClick = { onNext(otpCode) }
        )
    }
}

@Composable
fun OtpInputField(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    boxCount: Int = 5
) {
    BasicTextField(
        value = otpCode,
        onValueChange = {
            if (it.length <= boxCount) {
                onOtpChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until boxCount) {
                    val char = if (i < otpCode.length) otpCode[i].toString() else ""
                    val isFocused = i == otpCode.length || (i == boxCount - 1 && otpCode.length == boxCount)

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(
                                width = if (isFocused) 1.5.dp else 1.dp,
                                color = if (isFocused) Color(0xFF2F80ED) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                }
            }
        }
    )
}

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

enum class IconType {
    EMAIL, LOCK, PERSON, SMS
}

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

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F80ED)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun EmailIcon(tint: Color = Color(0xFF94A3B8)) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val strokePx = 1.8.dp.toPx()

        val path = Path().apply {
            moveTo(w * 0.1f, h * 0.25f)
            lineTo(w * 0.9f, h * 0.25f)
            lineTo(w * 0.9f, h * 0.75f)
            lineTo(w * 0.1f, h * 0.75f)
            close()
        }
        drawPath(path = path, color = tint, style = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round))

        val linePath = Path().apply {
            moveTo(w * 0.1f, h * 0.3f)
            lineTo(w * 0.5f, h * 0.55f)
            lineTo(w * 0.9f, h * 0.3f)
        }
        drawPath(path = linePath, color = tint, style = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun LockIcon(tint: Color = Color(0xFF94A3B8)) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val strokePx = 1.8.dp.toPx()

        val bodyPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = w * 0.2f,
                    top = h * 0.45f,
                    right = w * 0.8f,
                    bottom = h * 0.85f,
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            )
        }
        drawPath(path = bodyPath, color = tint, style = Stroke(width = strokePx))

        val shacklePath = Path().apply {
            moveTo(w * 0.32f, h * 0.45f)
            lineTo(w * 0.32f, h * 0.3f)
            cubicTo(
                w * 0.32f, h * 0.18f,
                w * 0.68f, h * 0.18f,
                w * 0.68f, h * 0.3f
            )
            lineTo(w * 0.68f, h * 0.45f)
        }
        drawPath(path = shacklePath, color = tint, style = Stroke(width = strokePx, cap = StrokeCap.Round))
    }
}

@Composable
fun PersonIcon(tint: Color = Color(0xFF94A3B8)) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val strokePx = 1.8.dp.toPx()

        drawCircle(
            color = tint,
            radius = w * 0.2f,
            center = Offset(w * 0.5f, h * 0.3f),
            style = Stroke(width = strokePx)
        )

        val bodyPath = Path().apply {
            moveTo(w * 0.2f, h * 0.82f)
            cubicTo(
                w * 0.2f, h * 0.6f,
                w * 0.8f, h * 0.6f,
                w * 0.8f, h * 0.82f
            )
        }
        drawPath(path = bodyPath, color = tint, style = Stroke(width = strokePx, cap = StrokeCap.Round))
    }
}

@Composable
fun SmsIcon(tint: Color = Color(0xFF94A3B8)) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val strokePx = 1.8.dp.toPx()

        val bubblePath = Path().apply {
            moveTo(w * 0.15f, h * 0.25f)
            lineTo(w * 0.85f, h * 0.25f)
            cubicTo(w * 0.92f, h * 0.25f, w * 0.95f, h * 0.28f, w * 0.95f, h * 0.35f)
            lineTo(w * 0.95f, h * 0.65f)
            cubicTo(w * 0.95f, h * 0.72f, w * 0.92f, h * 0.75f, w * 0.85f, h * 0.75f)
            lineTo(w * 0.45f, h * 0.75f)
            lineTo(w * 0.25f, h * 0.9f)
            lineTo(w * 0.25f, h * 0.75f)
            lineTo(w * 0.15f, h * 0.75f)
            cubicTo(w * 0.08f, h * 0.75f, w * 0.05f, h * 0.72f, w * 0.05f, h * 0.65f)
            lineTo(w * 0.05f, h * 0.35f)
            cubicTo(w * 0.05f, h * 0.28f, w * 0.08f, h * 0.25f, w * 0.15f, h * 0.25f)
            close()
        }
        drawPath(path = bubblePath, color = tint, style = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun BackArrowIcon(tint: Color = Color(0xFF2F80ED)) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val strokePx = 2.5.dp.toPx()

        val path = Path().apply {
            moveTo(w * 0.6f, h * 0.2f)
            lineTo(w * 0.3f, h * 0.5f)
            lineTo(w * 0.6f, h * 0.8f)
        }
        drawPath(path = path, color = tint, style = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun EyeIcon(visible: Boolean, tint: Color = Color(0xFF94A3B8)) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val strokePx = 1.8.dp.toPx()

        val eyePath = Path().apply {
            moveTo(w * 0.1f, h * 0.5f)
            cubicTo(w * 0.3f, h * 0.25f, w * 0.7f, h * 0.25f, w * 0.9f, h * 0.5f)
            cubicTo(w * 0.7f, h * 0.75f, w * 0.3f, h * 0.75f, w * 0.1f, h * 0.5f)
            close()
        }
        drawPath(path = eyePath, color = tint, style = Stroke(width = strokePx))

        drawCircle(color = tint, radius = w * 0.15f, center = Offset(w * 0.5f, h * 0.5f))

        if (!visible) {
            drawLine(
                color = tint,
                start = Offset(w * 0.15f, h * 0.85f),
                end = Offset(w * 0.85f, h * 0.15f),
                strokeWidth = strokePx,
                cap = StrokeCap.Round
            )
        }
    }
}

