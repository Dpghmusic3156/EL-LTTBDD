package com.example.baitap4_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baitap4_2.model.ForgetPasswordData
import com.example.baitap4_2.ui.screens.ConfirmScreen
import com.example.baitap4_2.ui.screens.ForgetPasswordScreen
import com.example.baitap4_2.ui.screens.ResetPasswordScreen
import com.example.baitap4_2.ui.screens.VerificationScreen
import com.example.baitap4_2.ui.theme.BaiTap4_2Theme

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
    var currentData by remember { mutableStateOf(ForgetPasswordData()) }
    var submittedData by remember { mutableStateOf<ForgetPasswordData?>(null) }

    NavHost(
        navController = navController,
        startDestination = "forget_password"
    ) {
        composable("forget_password") {
            ForgetPasswordScreen(
                submittedData = submittedData,
                onNext = { email ->
                    currentData = currentData.copy(email = email)
                    navController.navigate("verification")
                }
            )
        }

        composable("verification") {
            VerificationScreen(
                email = currentData.email,
                onBack = { navController.popBackStack() },
                onNext = { otp ->
                    currentData = currentData.copy(otp = otp)
                    navController.navigate("reset_password")
                }
            )
        }

        composable("reset_password") {
            ResetPasswordScreen(
                email = currentData.email,
                otp = currentData.otp,
                onBack = { navController.popBackStack() },
                onNext = { password ->
                    currentData = currentData.copy(pass = password)
                    navController.navigate("confirm")
                }
            )
        }

        composable("confirm") {
            ConfirmScreen(
                initialEmail = currentData.email,
                initialOtp = currentData.otp,
                initialPass = currentData.pass,
                onBack = { navController.popBackStack() },
                onSubmit = { finalEmail, finalOtp, finalPass ->
                    submittedData = ForgetPasswordData(
                        email = finalEmail,
                        otp = finalOtp,
                        pass = finalPass
                    )
                    currentData = ForgetPasswordData()
                    navController.navigate("forget_password") {
                        popUpTo("forget_password") { inclusive = true }
                    }
                }
            )
        }
    }
}
