package com.example.baitap5

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppNotification(
    val id: Int,
    val type: String, // "reminder" | "overdue" | "completed"
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean = false
)

@Composable
fun NotificationScreen(
    onAddTaskClick: () -> Unit = {},
    onNavClick: (String) -> Unit = {}
) {
    var notifications by remember {
        mutableStateOf(
            listOf(
                AppNotification(
                    id = 1,
                    type = "reminder",
                    title = "Nhắc nhở học tập",
                    content = "Sắp đến giờ: Ôn chương 3 – Cấu trúc dữ liệu (14:00).",
                    time = "13:45",
                    isRead = false
                ),
                AppNotification(
                    id = 2,
                    type = "overdue",
                    title = "Việc chưa hoàn thành",
                    content = "Bạn có 1 việc từ hôm qua chưa xong: Đi chợ mua đồ cho tuần.",
                    time = "08:00",
                    isRead = false
                ),
                AppNotification(
                    id = 3,
                    type = "completed",
                    title = "Hoàn thành xuất sắc",
                    content = "Tuyệt vời! Bạn đã hoàn thành việc: Nộp bài tập Lập trình di động.",
                    time = "Hôm qua",
                    isRead = true
                )
            )
        )
    }

    val unreadCount = notifications.count { !it.isRead }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Thông báo",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1C1E),
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = if (unreadCount > 0) "$unreadCount thông báo mới" else "Đã đọc tất cả",
                        fontSize = 14.sp,
                        color = Color(0xFF6B6B70)
                    )
                }

                if (unreadCount > 0) {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                notifications = notifications.map { it.copy(isRead = true) }
                            },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE6EDFF)
                    ) {
                        Text(
                            text = "Đánh dấu đã đọc",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F4FD1),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            if (notifications.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có thông báo nào.",
                            fontSize = 15.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }
            } else {
                notifications.forEach { item ->
                    NotificationCard(
                        notification = item,
                        onClick = {
                            if (!item.isRead) {
                                notifications = notifications.map {
                                    if (it.id == item.id) it.copy(isRead = true) else it
                                }
                            }
                        }
                    )
                }
            }
        }

        // Bottom Navigation Bar
        AppBottomNavigationBar(
            currentRoute = "notifications",
            modifier = Modifier.align(Alignment.BottomCenter),
            onAddClick = onAddTaskClick,
            onNavClick = onNavClick
        )
    }
}

@Composable
fun NotificationCard(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val iconBg = when (notification.type) {
        "reminder" -> Color(0xFFE6EDFF)
        "overdue" -> Color(0xFFFFE5E5)
        "completed" -> Color(0xFFE6F4EA)
        else -> Color(0xFFF2F2F7)
    }
    val iconColor = when (notification.type) {
        "reminder" -> Color(0xFF1F4FD1)
        "overdue" -> Color(0xFFE5484D)
        "completed" -> Color(0xFF137333)
        else -> Color(0xFF6B6B70)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (!notification.isRead) 2.dp else 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (!notification.isRead) Color(0xFFFCFCFE) else Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (notification.type) {
                    "reminder" -> {
                        Canvas(modifier = Modifier.size(20.dp)) {
                            val bellPath = Path().apply {
                                moveTo(size.width * 0.5f, size.height * 0.18f)
                                cubicTo(size.width * 0.3f, size.height * 0.18f, size.width * 0.25f, size.height * 0.5f, size.width * 0.2f, size.height * 0.7f)
                                lineTo(size.width * 0.8f, size.height * 0.7f)
                                cubicTo(size.width * 0.75f, size.height * 0.5f, size.width * 0.7f, size.height * 0.18f, size.width * 0.5f, size.height * 0.18f)
                            }
                            drawPath(bellPath, color = iconColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                        }
                    }
                    "overdue" -> {
                        Text(
                            text = "!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = iconColor
                        )
                    }
                    else -> {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val path = Path().apply {
                                moveTo(size.width * 0.2f, size.height * 0.55f)
                                lineTo(size.width * 0.45f, size.height * 0.8f)
                                lineTo(size.width * 0.85f, size.height * 0.25f)
                            }
                            drawPath(path, color = iconColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                        }
                    }
                }
            }

            // Text
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E)
                    )

                    // Unread blue dot indicator
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF2F5BEA), CircleShape)
                        )
                    }
                }

                Text(
                    text = notification.content,
                    fontSize = 13.sp,
                    fontWeight = if (!notification.isRead) FontWeight.Medium else FontWeight.Normal,
                    color = if (!notification.isRead) Color(0xFF3A3A3C) else Color(0xFF6B6B70),
                    lineHeight = 18.sp
                )

                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    color = Color(0xFF8E8E93)
                )
            }
        }
    }
}
