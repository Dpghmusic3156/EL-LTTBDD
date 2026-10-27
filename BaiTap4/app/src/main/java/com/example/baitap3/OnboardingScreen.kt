package com.example.baitap4

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    step: OnboardingStep,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp)
            .padding(top = 16.dp, bottom = 40.dp)
    ) {
        HeaderSection(onSkipClick = onSkipClick)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IllustrationGraphic(visualType = step.visualType)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp)
        ) {
            Text(
                text = step.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1E),
                lineHeight = 36.sp,
                letterSpacing = (-0.4).sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = step.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF5C5C62),
                lineHeight = 24.sp
            )
        }

        PageDotsIndicator(
            currentStep = step.stepNumber,
            totalSteps = step.totalSteps
        )

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onNextClick() },
            color = Color(0xFF2F5BEA),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.buttonText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(onSkipClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF2F5BEA)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    SmallHeaderIcon()
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "UTH",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E),
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = " Todo",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2F5BEA),
                    letterSpacing = (-0.3).sp
                )
            }
        }

        Text(
            text = "Bỏ qua",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6B6B70),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onSkipClick() }
                .padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun SmallHeaderIcon() {
    Canvas(modifier = Modifier.size(18.dp)) {
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx(),
            center = Offset(x = 4.dp.toPx(), y = 5.dp.toPx())
        )
        drawLine(
            color = Color.White,
            start = Offset(x = 9.dp.toPx(), y = 5.dp.toPx()),
            end = Offset(x = 15.dp.toPx(), y = 5.dp.toPx()),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.8f),
            radius = 2.5.dp.toPx(),
            center = Offset(x = 4.dp.toPx(), y = 13.dp.toPx()),
            style = Stroke(width = 1.2.dp.toPx())
        )
        drawLine(
            color = Color.White.copy(alpha = 0.8f),
            start = Offset(x = 9.dp.toPx(), y = 13.dp.toPx()),
            end = Offset(x = 13.dp.toPx(), y = 13.dp.toPx()),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun IllustrationGraphic(visualType: OnboardingVisualType) {
    Box(
        modifier = Modifier
            .size(width = 280.dp, height = 300.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 220.dp, height = 260.dp)
                .background(
                    color = Color(0xFF2F5BEA).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(28.dp)
                )
        )

        when (visualType) {
            OnboardingVisualType.CHECKLIST -> GraphicChecklist()
            OnboardingVisualType.CATEGORIES -> GraphicCategories()
            OnboardingVisualType.NOTIFICATION -> GraphicNotification()
        }
    }
}

@Composable
private fun GraphicChecklist() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(Color(0xFF2F5BEA), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(10.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.2f, size.height * 0.5f)
                            lineTo(size.width * 0.45f, size.height * 0.75f)
                            lineTo(size.width * 0.85f, size.height * 0.25f)
                        }
                        drawPath(
                            path = path,
                            color = Color.White,
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(8.dp)
                        .background(Color(0xFFE4E4EA), RoundedCornerShape(4.dp))
                )
            }
        }

        Surface(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(2.dp, Color(0xFFC7C7CC), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(8.dp)
                        .background(Color(0xFFE4E4EA), RoundedCornerShape(4.dp))
                )
            }
        }

        Surface(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(2.dp, Color(0xFFC7C7CC), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(8.dp)
                        .background(Color(0xFFE4E4EA), RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
private fun GraphicCategories() {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE6EDFF), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Học tập",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F4FD1)
                    )
                }
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(8.dp)
                        .background(Color(0xFFE4E4EA), RoundedCornerShape(4.dp))
                )
            }
        }

        Surface(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFEBD9), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Cuộc sống",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA24A08)
                    )
                }
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(8.dp)
                        .background(Color(0xFFE4E4EA), RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
private fun GraphicNotification() {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(84.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(40.dp)) {
                    val bellPath = Path().apply {
                        moveTo(size.width * 0.5f, size.height * 0.15f)
                        cubicTo(
                            size.width * 0.3f, size.height * 0.15f,
                            size.width * 0.25f, size.height * 0.45f,
                            size.width * 0.2f, size.height * 0.65f
                        )
                        lineTo(size.width * 0.8f, size.height * 0.65f)
                        cubicTo(
                            size.width * 0.75f, size.height * 0.45f,
                            size.width * 0.7f, size.height * 0.15f,
                            size.width * 0.5f, size.height * 0.15f
                        )
                    }
                    drawPath(
                        path = bellPath,
                        color = Color(0xFF2F5BEA),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = Color(0xFF2F5BEA),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(size.width * 0.42f, size.height * 0.72f),
                        size = Size(size.width * 0.16f, size.height * 0.16f),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }

        Surface(
            modifier = Modifier.width(180.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ôn bài Toán lúc 19:00",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1C1C1E)
                )
            }
        }
    }
}

@Composable
private fun PageDotsIndicator(
    currentStep: Int,
    totalSteps: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isActive = i == currentStep
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (isActive) 24.dp else 8.dp)
                    .background(
                        color = if (isActive) Color(0xFF2F5BEA) else Color(0xFFD1D1D6),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}
