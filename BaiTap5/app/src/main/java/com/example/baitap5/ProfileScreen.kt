package com.example.baitap5

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitap5.data.SessionManager
import com.example.baitap5.data.TaskRepository
import java.util.Calendar

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onLinkGoogleClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val session by SessionManager.session.collectAsState()
    val allTasks by TaskRepository.tasks.collectAsState()
    val errorMessage by TaskRepository.errorMessage.collectAsState()

    val totalTasks = allTasks.size
    val completedCount = allTasks.count { it.done }
    val pendingCount = allTasks.count { !it.done }
    val completionRate = if (totalTasks > 0) (completedCount * 100) / totalTasks else 0

    val statDoneStr = if (errorMessage != null && allTasks.isEmpty()) "–" else "$completedCount"
    val statPendingStr = if (errorMessage != null && allTasks.isEmpty()) "–" else "$pendingCount"
    val statRateStr = if (errorMessage != null && allTasks.isEmpty()) "–" else "$completionRate%"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top App Bar
            ProfileTopBar(onBackClick = onBackClick)

            // User Info Section
            ProfileUserInfo(
                displayName = session.displayName,
                email = session.email,
                isGuest = session.isGuest,
                avatarInitials = session.avatarInitials,
                onLinkGoogleClick = onLinkGoogleClick
            )

            // 3 Quick Stat Cards
            ProfileStatsRow(
                doneCount = statDoneStr,
                pendingCount = statPendingStr,
                rate = statRateStr
            )

            // Weekly Activity Chart Card (7 bars Mon - Sun)
            WeeklyChartCard()

            // Logout Button
            ProfileLogoutButton(
                onClick = {
                    SessionManager.logout()
                    Toast.makeText(context, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
                    onLogoutClick()
                }
            )
        }
    }
}

@Composable
private fun ProfileTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onBackClick() }
                .padding(vertical = 6.dp, horizontal = 4.dp)
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                val path = Path().apply {
                    moveTo(size.width * 0.65f, size.height * 0.15f)
                    lineTo(size.width * 0.3f, size.height * 0.5f)
                    lineTo(size.width * 0.65f, size.height * 0.85f)
                }
                drawPath(
                    path = path,
                    color = Color(0xFF2F5BEA),
                    style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
            Text(
                text = "Quay lại",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF2F5BEA)
            )
        }

        Text(
            text = "Hồ sơ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E)
        )

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun ProfileUserInfo(
    displayName: String,
    email: String,
    isGuest: Boolean,
    avatarInitials: String,
    onLinkGoogleClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Avatar Circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6EDFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarInitials,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F4FD1)
                )
            }

            // Name
            Text(
                text = displayName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1E)
            )

            // Email / Status
            if (!isGuest && email.isNotBlank()) {
                Text(
                    text = email,
                    fontSize = 14.sp,
                    color = Color(0xFF6B6B70)
                )
            }

            // Account status badge
            if (!isGuest) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(Color(0xFFDDF5EA), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.15f, size.height * 0.5f)
                            lineTo(size.width * 0.42f, size.height * 0.78f)
                            lineTo(size.width * 0.88f, size.height * 0.22f)
                        }
                        drawPath(path, color = Color(0xFF0F7A4F), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                    Text(
                        text = "Đã liên kết tài khoản Google",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F7A4F)
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Chưa liên kết tài khoản",
                        fontSize = 13.sp,
                        color = Color(0xFF8E8E93)
                    )
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onLinkGoogleClick() },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE6EDFF)
                    ) {
                        Text(
                            text = "Liên kết Google",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F4FD1),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatsRow(
    doneCount: String,
    pendingCount: String,
    rate: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProfileStatCard(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            value = doneCount,
            label = "Việc đã xong",
            color = Color(0xFF137333)
        )
        ProfileStatCard(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            value = pendingCount,
            label = "Đang chờ",
            color = Color(0xFF2F5BEA)
        )
        ProfileStatCard(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            value = rate,
            label = "Hoàn thành",
            color = Color(0xFFB25E00)
        )
    }
}

@Composable
private fun ProfileStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    color: Color = Color(0xFF1C1C1E)
) {
    Surface(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF6B6B70),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun WeeklyChartCard() {
    val cal = Calendar.getInstance()
    // In Java Calendar, SUNDAY=1, MONDAY=2, ..., SATURDAY=7
    // Vietnamese Mon=0, Tue=1, Wed=2, Thu=3, Fri=4, Sat=5, Sun=6
    val currentDayIndex = when (cal.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        Calendar.SUNDAY -> 6
        else -> 1
    }

    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    val heights = listOf(0.45f, 0.85f, 0.6f, 0.9f, 0.5f, 0.3f, 0.2f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tuần này",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E)
                )
                Text(
                    text = "7 ngày qua",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8E93)
                )
            }

            // 7 bar columns
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                days.forEachIndexed { index, day ->
                    val isToday = index == currentDayIndex
                    val hRatio = heights[index]

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().weight(1f)) {
                            val barW = 12.dp.toPx()
                            val barH = size.height * hRatio
                            val startX = (size.width - barW) / 2
                            val startY = size.height - barH

                            drawRoundRect(
                                color = if (isToday) Color(0xFF2F5BEA) else Color(0xFFE5E5EA),
                                topLeft = Offset(startX, startY),
                                size = Size(barW, barH),
                                cornerRadius = CornerRadius(6.dp.toPx())
                            )
                        }

                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                            color = if (isToday) Color(0xFF2F5BEA) else Color(0xFF8E8E93)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileLogoutButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFFFD0D2), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFF5F5)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Đăng xuất",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFE5484D)
            )
        }
    }
}
