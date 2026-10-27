package com.example.baitap4_2.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class IconType {
    EMAIL, LOCK, PERSON, SMS
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
