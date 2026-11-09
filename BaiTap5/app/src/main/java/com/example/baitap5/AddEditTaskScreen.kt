package com.example.baitap5

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitap5.data.Task
import com.example.baitap5.data.TaskRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddEditTaskScreen(
    taskId: Int? = null,
    onBackClick: () -> Unit = {},
    onSaved: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val allTasks by TaskRepository.tasks.collectAsState()
    val existingTask = remember(taskId, allTasks) {
        taskId?.let { id -> allTasks.find { it.id == id } }
    }

    val isEditMode = existingTask != null

    // Form state
    var title by remember(existingTask) { mutableStateOf(existingTask?.title ?: "") }
    var note by remember(existingTask) { mutableStateOf(existingTask?.note ?: "") }
    var category by remember(existingTask) { mutableStateOf(existingTask?.category ?: "study") } // "study" | "life"

    // Dates
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayCal = Calendar.getInstance()
    val todayStr = sdf.format(todayCal.time)

    val tomorrowCal = (todayCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
    val tomorrowStr = sdf.format(tomorrowCal.time)

    val endOfWeekCal = (todayCal.clone() as Calendar).apply {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        if (before(todayCal)) add(Calendar.WEEK_OF_YEAR, 1)
    }
    val endOfWeekStr = sdf.format(endOfWeekCal.time)

    var selectedDate by remember(existingTask) {
        mutableStateOf(existingTask?.date?.ifBlank { todayStr } ?: todayStr)
    }
    var time by remember(existingTask) {
        mutableStateOf(existingTask?.time?.ifBlank { "19:00" } ?: "19:00")
    }
    var remind by remember(existingTask) {
        mutableStateOf(existingTask?.remind ?: true)
    }
    var priority by remember(existingTask) {
        mutableStateOf(existingTask?.priority ?: "medium") // "low" | "medium" | "high"
    }

    var isSubmitting by remember { mutableStateOf(false) }
    var formError by remember { mutableStateOf<String?>(null) }

    val isTitleValid = title.trim().isNotBlank()

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
                .padding(bottom = 32.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onBackClick() },
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "‹",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C1C1E)
                        )
                    }
                }

                Text(
                    text = if (isEditMode) "Sửa công việc" else "Việc mới",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E)
                )

                Spacer(modifier = Modifier.size(40.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (formError != null) {
                    Text(
                        text = formError ?: "",
                        fontSize = 14.sp,
                        color = Color(0xFFE5484D),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                // 1. Title & Note Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Tên công việc *",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6B6B70)
                            )
                            BasicTextField(
                                value = title,
                                onValueChange = {
                                    title = it
                                    formError = null
                                },
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1C1C1E)
                                ),
                                cursorBrush = SolidColor(Color(0xFF2F5BEA)),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF9F9FB), RoundedCornerShape(12.dp))
                                            .padding(14.dp)
                                    ) {
                                        if (title.isEmpty()) {
                                            Text(
                                                text = "Nhập tên công việc…",
                                                fontSize = 15.sp,
                                                color = Color(0xFF8E8E93)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Ghi chú",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6B6B70)
                            )
                            BasicTextField(
                                value = note,
                                onValueChange = { note = it },
                                textStyle = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color(0xFF1C1C1E)
                                ),
                                cursorBrush = SolidColor(Color(0xFF2F5BEA)),
                                minLines = 3,
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF9F9FB), RoundedCornerShape(12.dp))
                                            .padding(14.dp)
                                    ) {
                                        if (note.isEmpty()) {
                                            Text(
                                                text = "Thêm ghi chú chi tiết nếu có…",
                                                fontSize = 14.sp,
                                                color = Color(0xFF8E8E93)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }
                }

                // 2. Category Selector
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Nhóm",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B6B70)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CategoryOptionButton(
                                title = "Học tập",
                                isSelected = category == "study",
                                activeBg = Color(0xFFE6EDFF),
                                activeText = Color(0xFF1F4FD1),
                                modifier = Modifier.weight(1f),
                                onClick = { category = "study" }
                            )
                            CategoryOptionButton(
                                title = "Cuộc sống",
                                isSelected = category == "life",
                                activeBg = Color(0xFFFFEBD9),
                                activeText = Color(0xFFA24A08),
                                modifier = Modifier.weight(1f),
                                onClick = { category = "life" }
                            )
                        }
                    }
                }

                // 3. Date & Time Selection
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Thời hạn",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B6B70)
                        )

                        // Quick date chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DateChip(
                                label = "Hôm nay",
                                isSelected = selectedDate == todayStr,
                                onClick = { selectedDate = todayStr }
                            )
                            DateChip(
                                label = "Ngày mai",
                                isSelected = selectedDate == tomorrowStr,
                                onClick = { selectedDate = tomorrowStr }
                            )
                            DateChip(
                                label = "Tuần này",
                                isSelected = selectedDate == endOfWeekStr,
                                onClick = { selectedDate = endOfWeekStr }
                            )
                        }

                        // Time row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Giờ hẹn (HH:mm)",
                                fontSize = 14.sp,
                                color = Color(0xFF1C1C1E)
                            )
                            Box(
                                modifier = Modifier
                                    .width(90.dp)
                                    .background(Color(0xFFF2F2F7), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BasicTextField(
                                    value = time,
                                    onValueChange = { if (it.length <= 5) time = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1C1C1E)
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF2F5BEA))
                                )
                            }
                        }
                    }
                }

                // 4. Priority Selector
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Mức ưu tiên",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B6B70)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PriorityOptionButton(
                                label = "Thấp",
                                isSelected = priority == "low",
                                modifier = Modifier.weight(1f),
                                onClick = { priority = "low" }
                            )
                            PriorityOptionButton(
                                label = "Vừa",
                                isSelected = priority == "medium",
                                modifier = Modifier.weight(1f),
                                onClick = { priority = "medium" }
                            )
                            PriorityOptionButton(
                                label = "Cao",
                                isSelected = priority == "high",
                                isHigh = true,
                                modifier = Modifier.weight(1f),
                                onClick = { priority = "high" }
                            )
                        }
                    }
                }

                // 5. Reminder Switch
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Nhắc tôi",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1C1C1E)
                            )
                            Text(
                                text = "Thông báo trước 15 phút",
                                fontSize = 13.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }

                        Switch(
                            checked = remind,
                            onCheckedChange = { remind = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF2F5BEA),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE5E5EA)
                            )
                        )
                    }
                }

                // 6. Save Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(enabled = isTitleValid && !isSubmitting) {
                            isSubmitting = true
                            formError = null
                            coroutineScope.launch {
                                if (isEditMode && existingTask != null) {
                                    val updated = existingTask.copy(
                                        title = title.trim(),
                                        note = note.trim(),
                                        category = category,
                                        date = selectedDate,
                                        time = time.trim().ifBlank { "19:00" },
                                        priority = priority,
                                        remind = remind
                                    )
                                    val res = TaskRepository.updateTaskDetails(updated)
                                    isSubmitting = false
                                    if (res.isSuccess) {
                                        Toast.makeText(context, "Đã lưu thay đổi", Toast.LENGTH_SHORT).show()
                                        onSaved(existingTask.id)
                                    } else {
                                        formError = res.exceptionOrNull()?.message ?: "Lỗi khi lưu"
                                    }
                                } else {
                                    val newTask = Task(
                                        title = title.trim(),
                                        note = note.trim(),
                                        category = category,
                                        date = selectedDate,
                                        time = time.trim().ifBlank { "19:00" },
                                        priority = priority,
                                        remind = remind,
                                        done = false
                                    )
                                    val res = TaskRepository.addTask(newTask)
                                    isSubmitting = false
                                    if (res.isSuccess) {
                                        val createdId = res.getOrNull()?.id ?: 1
                                        Toast.makeText(context, "Đã thêm công việc", Toast.LENGTH_SHORT).show()
                                        onSaved(createdId)
                                    } else {
                                        formError = res.exceptionOrNull()?.message ?: "Lỗi khi thêm"
                                    }
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isTitleValid) Color(0xFF2F5BEA) else Color(0xFFB8C8FF)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Lưu",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryOptionButton(
    title: String,
    isSelected: Boolean,
    activeBg: Color,
    activeText: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) activeBg else Color(0xFFF9F9FB),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E5EA))
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) activeText else Color(0xFF6B6B70)
            )
        }
    }
}

@Composable
private fun PriorityOptionButton(
    label: String,
    isSelected: Boolean,
    isHigh: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val activeBg = if (isHigh) Color(0xFFFFE5E5) else Color(0xFFE6EDFF)
    val activeText = if (isHigh) Color(0xFFE5484D) else Color(0xFF1F4FD1)

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) activeBg else Color(0xFFF9F9FB),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E5EA))
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) activeText else Color(0xFF6B6B70)
            )
        }
    }
}

@Composable
private fun DateChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color(0xFF2F5BEA) else Color(0xFFF2F2F7)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color(0xFF1C1C1E),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}
