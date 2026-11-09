package com.uthtodo.dpgh

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uthtodo.dpgh.data.Subtask
import com.uthtodo.dpgh.data.Task
import com.uthtodo.dpgh.data.TaskRepository
import kotlinx.coroutines.launch

@Composable
fun TaskDetailScreen(
    taskId: Int,
    onBackClick: () -> Unit = {},
    onEditClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val allTasks by TaskRepository.tasks.collectAsState()
    val task = allTasks.find { it.id == taskId }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var newSubtaskTitle by remember { mutableStateOf("") }
    var editingSubtaskIndex by remember { mutableStateOf<Int?>(null) }
    var editingSubtaskText by remember { mutableStateOf("") }

    if (task == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Không tìm thấy công việc.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B6B70)
                )
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F5BEA))
                ) {
                    Text("Quay lại", color = Color.White)
                }
            }
        }
        return
    }

    val isStudy = task.category.lowercase() == "study"
    val isHighPriority = task.priority.lowercase() == "high"
    val isMediumPriority = task.priority.lowercase() == "medium"

    val subtasks = task.subtasks
    val completedSubtasks = subtasks.count { it.done }
    val totalSubtasks = subtasks.size
    val subtasksProgress = if (totalSubtasks > 0) completedSubtasks.toFloat() / totalSubtasks else 0f

    val relativeDeadline = formatRelativeDate(task.date, task.time)

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
                // Back Button
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

                // Edit Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onEditClick(task.id) },
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE6EDFF)
                ) {
                    Text(
                        text = "Sửa",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F4FD1),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Info Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Chips row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Category chip
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isStudy) Color(0xFFE6EDFF) else Color(0xFFFFEBD9),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = task.categoryLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isStudy) Color(0xFF1F4FD1) else Color(0xFFA24A08)
                                )
                            }

                            // Priority chip
                            val priorityBg = when {
                                isHighPriority -> Color(0xFFFFE5E5)
                                isMediumPriority -> Color(0xFFFFF3D6)
                                else -> Color(0xFFF2F2F7)
                            }
                            val priorityColor = when {
                                isHighPriority -> Color(0xFFE5484D)
                                isMediumPriority -> Color(0xFFB25E00)
                                else -> Color(0xFF6B6B70)
                            }
                            Box(
                                modifier = Modifier
                                    .background(priorityBg, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Ưu tiên ${task.priorityLabel.lowercase()}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = priorityColor
                                )
                            }

                            // Done chip
                            if (task.done) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFE6F4EA), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Đã xong",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF137333)
                                    )
                                }
                            }
                        }

                        // Title
                        Text(
                            text = task.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (task.done) Color(0xFF8E8E93) else Color(0xFF1C1C1E),
                            textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
                            lineHeight = 28.sp
                        )

                        // Deadline & Reminder
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = "Hạn chót", fontSize = 12.sp, color = Color(0xFF8E8E93))
                                Text(
                                    text = if (relativeDeadline.isNotBlank()) relativeDeadline else "Không có",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1C1C1E)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(text = "Nhắc nhở", fontSize = 12.sp, color = Color(0xFF8E8E93))
                                Text(
                                    text = if (task.remind) "Trước 15 phút" else "Tắt",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (task.remind) Color(0xFF2F5BEA) else Color(0xFF8E8E93)
                                )
                            }
                        }

                        // Note section (Hidden if note is empty)
                        if (task.note.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF9F9FB), RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Ghi chú", fontSize = 12.sp, color = Color(0xFF8E8E93))
                                    Text(
                                        text = task.note,
                                        fontSize = 14.sp,
                                        color = Color(0xFF1C1C1E),
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Subtasks Section Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Subtask Header with progress counter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Việc nhỏ",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1C1C1E)
                            )
                            if (totalSubtasks > 0) {
                                Text(
                                    text = "$completedSubtasks/$totalSubtasks",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2F5BEA)
                                )
                            }
                        }

                        // Progress Bar
                        if (totalSubtasks > 0) {
                            LinearProgressIndicator(
                                progress = { subtasksProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFF2F5BEA),
                                trackColor = Color(0xFFE5E5EA)
                            )
                        }

                        // Empty subtasks text
                        if (subtasks.isEmpty()) {
                            Text(
                                text = "Chưa có việc nhỏ nào. Chia nhỏ để dễ bắt đầu hơn.",
                                fontSize = 14.sp,
                                color = Color(0xFF8E8E93),
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        } else {
                            // Subtask List
                            subtasks.forEachIndexed { index, subtask ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Checkbox
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (subtask.done) Color(0xFF2F5BEA) else Color.Transparent,
                                                CircleShape
                                            )
                                            .border(
                                                width = 1.8.dp,
                                                color = if (subtask.done) Color(0xFF2F5BEA) else Color(0xFFC7C7CC),
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                val updated = subtasks.mapIndexed { i, s ->
                                                    if (i == index) s.copy(done = !s.done) else s
                                                }
                                                coroutineScope.launch {
                                                    TaskRepository.updateSubtasks(task.id, updated)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (subtask.done) {
                                            Canvas(modifier = Modifier.size(10.dp)) {
                                                val path = Path().apply {
                                                    moveTo(size.width * 0.2f, size.height * 0.55f)
                                                    lineTo(size.width * 0.45f, size.height * 0.8f)
                                                    lineTo(size.width * 0.85f, size.height * 0.25f)
                                                }
                                                drawPath(
                                                    path = path,
                                                    color = Color.White,
                                                    style = Stroke(
                                                        width = 1.8.dp.toPx(),
                                                        cap = StrokeCap.Round,
                                                        join = StrokeJoin.Round
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    // Subtask Title (Editable or Text)
                                    if (editingSubtaskIndex == index) {
                                        BasicTextField(
                                            value = editingSubtaskText,
                                            onValueChange = { editingSubtaskText = it },
                                            singleLine = true,
                                            textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF1C1C1E)),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "Lưu",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2F5BEA),
                                            modifier = Modifier.clickable {
                                                if (editingSubtaskText.isNotBlank()) {
                                                    val updated = subtasks.mapIndexed { i, s ->
                                                        if (i == index) s.copy(title = editingSubtaskText.trim()) else s
                                                    }
                                                    coroutineScope.launch {
                                                        TaskRepository.updateSubtasks(task.id, updated)
                                                    }
                                                }
                                                editingSubtaskIndex = null
                                            }
                                        )
                                    } else {
                                        Text(
                                            text = subtask.title,
                                            fontSize = 14.sp,
                                            color = if (subtask.done) Color(0xFF8E8E93) else Color(0xFF1C1C1E),
                                            textDecoration = if (subtask.done) TextDecoration.LineThrough else TextDecoration.None,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    editingSubtaskIndex = index
                                                    editingSubtaskText = subtask.title
                                                }
                                        )
                                    }

                                    // Delete subtask button
                                    Text(
                                        text = "✕",
                                        fontSize = 13.sp,
                                        color = Color(0xFFC7C7CC),
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable {
                                                val updated = subtasks.filterIndexed { i, _ -> i != index }
                                                coroutineScope.launch {
                                                    TaskRepository.updateSubtasks(task.id, updated)
                                                }
                                            }
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }

                        // Add Subtask Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9F9FB), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "+", fontSize = 18.sp, color = Color(0xFF2F5BEA), fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.weight(1f)) {
                                if (newSubtaskTitle.isEmpty()) {
                                    Text(text = "Thêm việc nhỏ…", fontSize = 14.sp, color = Color(0xFF8E8E93))
                                }
                                BasicTextField(
                                    value = newSubtaskTitle,
                                    onValueChange = { newSubtaskTitle = it },
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF1C1C1E)),
                                    cursorBrush = SolidColor(Color(0xFF2F5BEA)),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            if (newSubtaskTitle.isNotBlank()) {
                                Text(
                                    text = "Thêm",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2F5BEA),
                                    modifier = Modifier.clickable {
                                        val updated = subtasks + Subtask(title = newSubtaskTitle.trim(), done = false)
                                        coroutineScope.launch {
                                            TaskRepository.updateSubtasks(task.id, updated)
                                        }
                                        newSubtaskTitle = ""
                                    }
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                // 1. Complete / Reopen Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            coroutineScope.launch {
                                val res = TaskRepository.toggleTaskDone(task.id)
                                if (res.isSuccess) {
                                    val isNowDone = res.getOrNull()?.done ?: !task.done
                                    if (isNowDone) {
                                        Toast.makeText(context, "Tuyệt, đã xong một việc!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = if (task.done) Color(0xFFF2F2F7) else Color(0xFF2F5BEA)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (task.done) "Đánh dấu chưa xong" else "Đánh dấu hoàn thành",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (task.done) Color(0xFF1C1C1E) else Color.White
                        )
                    }
                }

                // 2. Delete Task Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFFFD0D2), RoundedCornerShape(16.dp))
                        .clickable { showDeleteDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF5F5)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Xóa công việc",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE5484D)
                        )
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(
                        text = "Xóa việc này?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1C1E)
                    )
                },
                text = {
                    Text(
                        text = "Không thể hoàn tác.",
                        fontSize = 15.sp,
                        color = Color(0xFF6B6B70)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            coroutineScope.launch {
                                val res = TaskRepository.deleteTask(task.id)
                                if (res.isSuccess) {
                                    Toast.makeText(context, "Đã xóa công việc", Toast.LENGTH_SHORT).show()
                                    onBackClick()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5484D))
                    ) {
                        Text("Xóa", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Hủy", color = Color(0xFF6B6B70))
                    }
                }
            )
        }
    }
}
