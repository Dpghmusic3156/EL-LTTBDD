package com.example.baitap4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baitap4.ui.theme.BaiTap4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap4Theme(darkTheme = false, dynamicColor = false) {
                BaiTap4()
            }
        }
    }
}

@Composable
fun BaiTap4() {
    val navController = rememberNavController()
    val repository = remember { OnboardingRepository() }

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable("start") {
            WelcomeScreen(
                onReadyClick = {
                    navController.navigate("onboarding/1") {
                        popUpTo("start") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding/1") {
            val step = repository.getStepByNumber(1)!!
            OnboardingScreen(
                step = step,
                onNextClick = {
                    navController.navigate("onboarding/2")
                },
                onSkipClick = {
                    navController.navigate("onboarding/3") {
                        popUpTo("onboarding/1") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding/2") {
            val step = repository.getStepByNumber(2)!!
            OnboardingScreen(
                step = step,
                onNextClick = {
                    navController.navigate("onboarding/3")
                },
                onSkipClick = {
                    navController.navigate("onboarding/3") {
                        popUpTo("onboarding/2") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding/3") {
            val step = repository.getStepByNumber(3)!!
            OnboardingScreen(
                step = step,
                onNextClick = {
                    navController.navigate("start") {
                        popUpTo("onboarding/3") { inclusive = true }
                    }
                },
                onSkipClick = {
                    navController.navigate("start") {
                        popUpTo("onboarding/3") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun WelcomeScreen(
    onReadyClick: () -> Unit
) {
    val animatableProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatableProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = LinearEasing
            )
        )
        onReadyClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2F5BEA))
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable { onReadyClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Surface(
                modifier = Modifier.size(116.dp),
                shape = RoundedCornerShape(27.dp),
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    UthLogoIcon()
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UTH",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = " Todo",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-1).sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Việc học, việc nhà — gọn gàng mỗi ngày",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.28f),
                            shape = RoundedCornerShape(2.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatableProgress.value)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Phiên bản 2.1.0",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun UthLogoIcon() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(Color(0xFF2F5BEA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    val path = Path().apply {
                        moveTo(size.width * 0.2f, size.height * 0.5f)
                        lineTo(size.width * 0.45f, size.height * 0.75f)
                        lineTo(size.width * 0.85f, size.height * 0.25f)
                    }
                    drawPath(
                        path = path,
                        color = Color.White,
                        style = Stroke(
                            width = 2.5.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(10.dp)
                    .background(Color(0xFF2F5BEA), RoundedCornerShape(5.dp))
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                drawCircle(
                    color = Color(0xFF91A7F6),
                    radius = (size.minDimension / 2) - (1.75.dp.toPx()),
                    style = Stroke(width = 3.5.dp.toPx())
                )
            }

            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(10.dp)
                    .background(Color(0xFFB8C8FF), RoundedCornerShape(5.dp))
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
