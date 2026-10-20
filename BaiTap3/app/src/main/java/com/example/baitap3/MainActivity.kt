package com.example.baitap3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.baitap3.ui.theme.BaiTap3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap3Theme(darkTheme = false, dynamicColor = false) {
                BaiTap3()
            }
        }
        }
    }

@Composable
fun BaiTap3() {
    var currentScreen by rememberSaveable {
        mutableIntStateOf(0) // 0 là WelcomeScreen, 1 là Danhsach
    }

    val goBack = { currentScreen = 1 }

    BackHandler(enabled = currentScreen != 0) {
        if (currentScreen == 1) {
            currentScreen = 0
        } else {
            currentScreen = 1
        }
    }

    when (currentScreen) {
        0 -> WelcomeScreen(
            onReadyClick = { currentScreen = 1 }
        )
        1 -> DanhSach(
            onOpen = { targetScreen -> currentScreen = targetScreen }
        )
        2 -> TextDetail(onBack = goBack)
        3 -> ImageDetail(onBack = goBack)
        4 -> TextFieldDetail(onBack = goBack)
        5 -> RowLayout(onBack = goBack)
        6 -> PasswordFieldDetail(onBack = goBack)
        7 -> ColumnLayout(onBack = goBack)
        else -> WelcomeScreen(
            onReadyClick = { currentScreen = 1 }
        )
    }
}

@Composable
fun WelcomeScreen(
    onReadyClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AsyncImage(
                model = "https://www.jetpackcompose.net/jetpack-compose-logo.png",
                contentDescription = "Jetpack Compose Logo",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Jetpack Compose",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1.2f))

            Button(
                onClick = onReadyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text(
                    text = "I'm ready",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun KhungManHinh(
    title: String,
    onBack: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
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
}