package com.uthtodo.dpgh

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onAddTaskClick: () -> Unit = {},
    onNavClick: (String) -> Unit = {}
) {
    // Settings state
    var themeMode by remember { mutableStateOf("Tự động") } // "Sáng" | "Tối" | "Tự động"
    var selectedColorHex by remember { mutableStateOf("#2F5BEA") }

    var enableReminders by remember { mutableStateOf(true) }
    var enableSound by remember { mutableStateOf(true) }
    var enableDailySummary by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF2F5BEA)

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Cài đặt",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Tùy chỉnh giao diện và thông báo",
                    fontSize = 14.sp,
                    color = Color(0xFF6B6B70)
                )
            }

            // 1. Giao diện Card
            SettingsSectionCard(title = "GIAO DIỆN") {
                // Theme mode selector
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Chế độ hiển thị",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Sáng", "Tối", "Tự động").forEach { mode ->
                            val isSel = themeMode == mode
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { themeMode = mode },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) primaryColor else Color(0xFFF2F2F7)
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = mode,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else Color(0xFF1C1C1E)
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFF2F2F7), thickness = 1.dp)

                // Accent Color Selector
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Màu chủ đạo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(
                            "#2F5BEA" to Color(0xFF2F5BEA),
                            "#34C759" to Color(0xFF34C759),
                            "#FF9500" to Color(0xFFFF9500),
                            "#AF52DE" to Color(0xFFAF52DE),
                            "#FF2D55" to Color(0xFFFF2D55)
                        ).forEach { (hex, col) ->
                            val isSel = selectedColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(col)
                                    .clickable { selectedColorHex = hex }
                                    .border(
                                        width = if (isSel) 3.dp else 0.dp,
                                        color = if (isSel) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSel) {
                                    Text(text = "✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Thông báo Card
            SettingsSectionCard(title = "THÔNG BÁO") {
                // Nhắc việc
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nhắc việc",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1C1C1E)
                        )
                        Text(
                            text = "Nhận nhắc nhở trước hạn chót 15 phút",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                    Switch(
                        checked = enableReminders,
                        onCheckedChange = { enableReminders = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE5E5EA)
                        )
                    )
                }

                HorizontalDivider(color = Color(0xFFF2F2F7), thickness = 1.dp)

                // Âm thanh
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Âm thanh",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1C1C1E)
                        )
                        Text(
                            text = "Phát âm thanh khi có thông báo",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                    Switch(
                        checked = enableSound,
                        onCheckedChange = { enableSound = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE5E5EA)
                        )
                    )
                }

                HorizontalDivider(color = Color(0xFFF2F2F7), thickness = 1.dp)

                // Tổng kết cuối ngày
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tổng kết cuối ngày",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1C1C1E)
                        )
                        Text(
                            text = "Nhắc nhở tổng kết các việc lúc 21:00",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                    Switch(
                        checked = enableDailySummary,
                        onCheckedChange = { enableDailySummary = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE5E5EA)
                        )
                    )
                }
            }

            // 3. Chung Card
            SettingsSectionCard(title = "CHUNG") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ngôn ngữ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E)
                    )
                    Text(
                        text = "Tiếng Việt",
                        fontSize = 14.sp,
                        color = Color(0xFF6B6B70)
                    )
                }

                HorizontalDivider(color = Color(0xFFF2F2F7), thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Giới thiệu",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E)
                    )
                    Text(
                        text = "Phiên bản 1.0",
                        fontSize = 14.sp,
                        color = Color(0xFF6B6B70)
                    )
                }
            }
        }

        // Bottom Navigation Bar
        AppBottomNavigationBar(
            currentRoute = "settings",
            modifier = Modifier.align(Alignment.BottomCenter),
            onAddClick = onAddTaskClick,
            onNavClick = onNavClick
        )
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B6B70),
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}
