package com.example.baitap6

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PermissionBlobBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.38f, h * 0.08f)
            cubicTo(w * 0.70f, -0.02f, w * 0.96f, h * 0.16f, w * 0.88f, h * 0.48f)
            cubicTo(w * 0.82f, h * 0.78f, w * 0.98f, h * 0.88f, w * 0.65f, h * 0.95f)
            cubicTo(w * 0.35f, h * 1.02f, w * 0.12f, h * 0.90f, w * 0.07f, h * 0.68f)
            cubicTo(w * 0.02f, h * 0.46f, w * 0.08f, h * 0.18f, w * 0.38f, h * 0.08f)
            close()
        }
        drawPath(path, color = Color(0xFFE5EEFF))
    }
}

@Composable
fun LocationGraphic() {
    Box(
        modifier = Modifier.size(240.dp, 200.dp),
        contentAlignment = Alignment.Center
    ) {
        PermissionBlobBackground(modifier = Modifier.fillMaxSize())

        Canvas(modifier = Modifier.size(100.dp, 120.dp)) {
            val cx = size.width / 2f
            val pinTop = 10.dp.toPx()
            val pinRadius = 32.dp.toPx()
            val pinHeadCenterY = pinTop + pinRadius
            val tipY = pinHeadCenterY + 46.dp.toPx()

            // Shadow oval
            val shadowWidth = 74.dp.toPx()
            val shadowHeight = 18.dp.toPx()
            drawOval(
                color = Color(0xFF3A75F8),
                topLeft = Offset(cx - shadowWidth / 2f, tipY - shadowHeight * 0.5f),
                size = Size(shadowWidth, shadowHeight)
            )

            // Pin shape
            val path = Path().apply {
                moveTo(cx, tipY)
                cubicTo(
                    cx - pinRadius * 0.85f, tipY - 18.dp.toPx(),
                    cx - pinRadius, pinHeadCenterY + 12.dp.toPx(),
                    cx - pinRadius, pinHeadCenterY
                )
                arcTo(
                    rect = Rect(
                        cx - pinRadius, pinHeadCenterY - pinRadius,
                        cx + pinRadius, pinHeadCenterY + pinRadius
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                cubicTo(
                    cx + pinRadius, pinHeadCenterY + 12.dp.toPx(),
                    cx + pinRadius * 0.85f, tipY - 18.dp.toPx(),
                    cx, tipY
                )
                close()
            }
            drawPath(path, color = Color(0xFF2B5BF5))

            // White hole
            drawCircle(
                color = Color.White,
                radius = 13.dp.toPx(),
                center = Offset(cx, pinHeadCenterY)
            )
        }
    }
}

@Composable
fun NotificationGraphic() {
    Box(
        modifier = Modifier.size(240.dp, 200.dp),
        contentAlignment = Alignment.Center
    ) {
        PermissionBlobBackground(modifier = Modifier.fillMaxSize())

        Canvas(modifier = Modifier.size(160.dp, 140.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            val waveColor = Color(0xFF2B5BF5)
            val strokeWidth = 3.5.dp.toPx()

            rotate(degrees = -32f, pivot = Offset(cx, cy)) {
                val bellTop = cy - 38.dp.toPx()
                val bellBottom = cy + 20.dp.toPx()
                val bellW = 54.dp.toPx()

                // Left waves (curving outward on the left of the bell)
                drawArc(
                    color = waveColor,
                    startAngle = 140f,
                    sweepAngle = 80f,
                    useCenter = false,
                    topLeft = Offset(cx - 52.dp.toPx(), cy - 26.dp.toPx()),
                    size = Size(46.dp.toPx(), 46.dp.toPx()),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = waveColor,
                    startAngle = 140f,
                    sweepAngle = 80f,
                    useCenter = false,
                    topLeft = Offset(cx - 40.dp.toPx(), cy - 18.dp.toPx()),
                    size = Size(32.dp.toPx(), 32.dp.toPx()),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Right waves (curving outward on the right of the bell)
                drawArc(
                    color = waveColor,
                    startAngle = -40f,
                    sweepAngle = 80f,
                    useCenter = false,
                    topLeft = Offset(cx + 6.dp.toPx(), cy - 26.dp.toPx()),
                    size = Size(46.dp.toPx(), 46.dp.toPx()),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = waveColor,
                    startAngle = -40f,
                    sweepAngle = 80f,
                    useCenter = false,
                    topLeft = Offset(cx + 8.dp.toPx(), cy - 18.dp.toPx()),
                    size = Size(32.dp.toPx(), 32.dp.toPx()),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Clapper
                drawCircle(
                    color = Color(0xFF3875F6),
                    radius = 12.dp.toPx(),
                    center = Offset(cx, bellBottom + 4.dp.toPx())
                )

                // Top loop handle
                drawRoundRect(
                    color = Color(0xFFD6E3FF),
                    topLeft = Offset(cx - 6.dp.toPx(), bellTop - 12.dp.toPx()),
                    size = Size(12.dp.toPx(), 18.dp.toPx()),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )

                // Bell Body
                val bellPath = Path().apply {
                    moveTo(cx - 10.dp.toPx(), bellTop)
                    cubicTo(
                        cx - 10.dp.toPx(), bellTop,
                        cx - 12.dp.toPx(), bellBottom - 20.dp.toPx(),
                        cx - bellW / 2f, bellBottom
                    )
                    lineTo(cx + bellW / 2f, bellBottom)
                    cubicTo(
                        cx + 12.dp.toPx(), bellBottom - 20.dp.toPx(),
                        cx + 10.dp.toPx(), bellTop,
                        cx + 10.dp.toPx(), bellTop
                    )
                    close()
                }
                drawPath(bellPath, color = Color(0xFF5386FC))

                // Bottom rim
                val rimWidth = 60.dp.toPx()
                val rimHeight = 12.dp.toPx()
                drawRoundRect(
                    color = Color(0xFF2555EA),
                    topLeft = Offset(cx - rimWidth / 2f, bellBottom - 2.dp.toPx()),
                    size = Size(rimWidth, rimHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }
        }
    }
}

@Composable
fun CameraGraphic() {
    Box(
        modifier = Modifier.size(240.dp, 200.dp),
        contentAlignment = Alignment.Center
    ) {
        PermissionBlobBackground(modifier = Modifier.fillMaxSize())

        Surface(
            modifier = Modifier.size(86.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF2B5BF5),
            shadowElevation = 4.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.Center)
                )
                Box(
                    modifier = Modifier
                        .padding(top = 13.dp, end = 13.dp)
                        .size(9.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun PermissionLayout(
    illustration: @Composable () -> Unit,
    title: String,
    description: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1.1f))

        illustration()

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = description,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF596375),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1.3f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .clickable { onPrimaryClick() },
            color = Color(0xFF2B5BF5),
            shape = RoundedCornerShape(28.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = primaryButtonText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .clickable { onSkipClick() },
            color = Color.Transparent,
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.5.dp, Color(0xFF2B5BF5))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Skip for now",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2B5BF5),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun LocationPermissionScreen(
    onNext: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        onNext()
    }

    PermissionLayout(
        illustration = { LocationGraphic() },
        title = "Location",
        description = "Allow maps to access your location whiloe you use the app?",
        primaryButtonText = "Allow",
        onPrimaryClick = {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        },
        onSkipClick = onNext
    )
}

@Composable
fun NotificationPermissionScreen(
    onNext: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        onNext()
    }

    PermissionLayout(
        illustration = { NotificationGraphic() },
        title = "Notification",
        description = "Please enable notifications to receive updates and reminders",
        primaryButtonText = "Turn on",
        onPrimaryClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onNext()
            }
        },
        onSkipClick = onNext
    )
}

@Composable
fun CameraPermissionScreen(
    onComplete: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        onComplete()
    }

    PermissionLayout(
        illustration = { CameraGraphic() },
        title = "Camera",
        description = "We need access to your camera to scan QR codes",
        primaryButtonText = "Turn on",
        onPrimaryClick = {
            launcher.launch(Manifest.permission.CAMERA)
        },
        onSkipClick = onComplete
    )
}

@Preview(showBackground = true)
@Composable
fun LocationPermissionPreview() {
    LocationPermissionScreen(onNext = {})
}

@Preview(showBackground = true)
@Composable
fun NotificationPermissionPreview() {
    NotificationPermissionScreen(onNext = {})
}

@Preview(showBackground = true)
@Composable
fun CameraPermissionPreview() {
    CameraPermissionScreen(onComplete = {})
}
