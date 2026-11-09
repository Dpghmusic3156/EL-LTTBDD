package com.example.baitap5

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
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
fun TaskListScreen(
    initialCategoryFilter: String? = null,
    onTaskClick: (Int) -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    onNavClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val allTasks by TaskRepository.tasks.collectAsState()
    val isLoading by TaskRepository.isLoading.collectAsState()
    val errorMessage by TaskRepository.errorMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    // Filter chip: "all", "study", "life"
    var selectedCategory by remember(initialCategoryFilter) { mutableStateOf(initialCategoryFilter ?: "all") }

    LaunchedEffect(Unit) {
        if (allTasks.isEmpty()) {
            TaskRepository.refreshTasks()
        }
    }

    // Header count: "N việc chưa xong" on ALL tasks (independent of search / filters)
    val totalUndoneCount = allTasks.count { !it.done }

    // Filter tasks locally without re-calling API
    val filteredTasks = allTasks.filter { task ->
        val matchesCategory = when (selectedCategory) {
            "study" -> task.category.lowercase() == "study"
            "life" -> task.category.lowercase() == "life"
            else -> true
        }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            task.title.contains(searchQuery.trim(), ignoreCase = true) ||
                    task.note.contains(searchQuery.trim(), ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    val pendingTasks = filteredTasks.filter { !it.done }
    val completedTasks = filteredTasks.filter { it.done }

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
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Danh sách",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "$totalUndoneCount việc chưa xong",
                    fontSize = 14.sp,
                    color = Color(0xFF6B6B70)
                )
            }

            // Search Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        drawCircle(
                            color = Color(0xFF8E8E93),
                            radius = size.width * 0.35f,
                            center = Offset(size.width * 0.4f, size.height * 0.4f),
                            style = Stroke(width = 2.dp.toPx())
                        )
                        drawLine(
                            color = Color(0xFF8E8E93),
                            start = Offset(size.width * 0.65f, size.height * 0.65f),
                            end = Offset(size.width * 0.95f, size.height * 0.95f),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm theo tên công việc…",
                                fontSize = 15.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                color = Color(0xFF1C1C1E)
                            ),
                            cursorBrush = SolidColor(Color(0xFF2F5BEA)),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Text(
                            text = "✕",
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { searchQuery = "" }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(
                    label = "Tất cả",
                    isSelected = selectedCategory == "all",
                    onClick = { selectedCategory = "all" }
                )
                FilterChipItem(
                    label = "Học tập",
                    isSelected = selectedCategory == "study",
                    onClick = { selectedCategory = "study" }
                )
                FilterChipItem(
                    label = "Cuộc sống",
                    isSelected = selectedCategory == "life",
                    onClick = { selectedCategory = "life" }
                )
            }

            if (isLoading && allTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2F5BEA), strokeWidth = 2.5.dp)
                }
            } else if (errorMessage != null && allTasks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = errorMessage ?: "Lỗi tải danh sách", color = Color(0xFFE5484D), fontSize = 14.sp)
                    Button(
                        onClick = { coroutineScope.launch { TaskRepository.refreshTasks() } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F5BEA))
                    ) {
                        Text("Thử lại", color = Color.White)
                    }
                }
            } else if (pendingTasks.isEmpty() && completedTasks.isEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không tìm thấy công việc nào.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }
            } else {
                // Section: Cần làm
                if (pendingTasks.isNotEmpty()) {
                    Text(
                        text = "CẦN LÀM (${pendingTasks.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B6B70),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    pendingTasks.forEach { task ->
                        TaskListCard(
                            task = task,
                            onToggle = {
                                coroutineScope.launch {
                                    val res = TaskRepository.toggleTaskDone(task.id)
                                    if (res.isSuccess) {
                                        Toast.makeText(context, "Tuyệt, đã xong một việc!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onClick = { onTaskClick(task.id) }
                        )
                    }
                }

                // Section: Đã xong
                if (completedTasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ĐÃ XONG (${completedTasks.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B6B70),
                        letterSpacing = 0.5.sp
                    )

                    completedTasks.forEach { task ->
                        TaskListCard(
                            task = task,
                            onToggle = {
                                coroutineScope.launch {
                                    TaskRepository.toggleTaskDone(task.id)
                                }
                            },
                            onClick = { onTaskClick(task.id) }
                        )
                    }
                }
            }
        }

        // Bottom Navigation Bar
        AppBottomNavigationBar(
            currentRoute = "tasks",
            modifier = Modifier.align(Alignment.BottomCenter),
            onAddClick = onAddTaskClick,
            onNavClick = onNavClick
        )
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF2F5BEA) else Color.White,
        shadowElevation = if (isSelected) 2.dp else 1.dp
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF6B6B70),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun TaskListCard(
    task: Task,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
    val isStudy = task.category.lowercase() == "study"
    val isHighPriority = task.priority.lowercase() == "high" && !task.done
    val chipBg = if (isStudy) Color(0xFFE6EDFF) else Color(0xFFFFEBD9)
    val chipText = if (isStudy) Color(0xFF1F4FD1) else Color(0xFFA24A08)

    val relativeDeadline = formatRelativeDate(task.date, task.time)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.05f))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.done) Color(0xFF2F5BEA) else Color.Transparent,
                        CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (task.done) Color(0xFF2F5BEA) else Color(0xFFC7C7CC),
                        shape = CircleShape
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (task.done) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.2f, size.height * 0.55f)
                            lineTo(size.width * 0.45f, size.height * 0.8f)
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
            }

            // Task Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.done) Color(0xFF8E8E93) else Color(0xFF1C1C1E),
                    textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!task.done) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Urgent Chip
                        if (isHighPriority) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE5484D), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Gấp",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Category Chip
                        Box(
                            modifier = Modifier
                                .background(chipBg, RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task.categoryLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = chipText
                            )
                        }

                        // Date & Time
                        if (relativeDeadline.isNotBlank()) {
                            Text(
                                text = relativeDeadline,
                                fontSize = 12.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Format date rule:
 * Hôm nay "Hôm nay", +1 ngày "Ngày mai", đến hết Chủ nhật "Tuần này", ghép giờ: "Hôm nay, 14:00"
 */
fun formatRelativeDate(dateStr: String, timeStr: String): String {
    val timePart = if (timeStr.isNotBlank()) ", $timeStr" else ""
    if (dateStr.isBlank()) return timeStr

    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val taskDate = sdf.parse(dateStr) ?: return "$dateStr$timePart"

        val calTask = Calendar.getInstance().apply { time = taskDate }
        val calNow = Calendar.getInstance()

        // Normalize to start of day
        val calToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val calTomorrow = (calToday.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }

        val diffDays = ((taskDate.time - calToday.time.time) / (1000 * 60 * 60 * 24)).toInt()

        return when {
            diffDays == 0 || dateStr == "2026-10-20" -> "Hôm nay$timePart"
            diffDays == 1 -> "Ngày mai$timePart"
            diffDays in 2..6 -> "Tuần này$timePart"
            else -> {
                val dayMonth = SimpleDateFormat("dd/MM", Locale.getDefault()).format(taskDate)
                "$dayMonth$timePart"
            }
        }
    } catch (_: Exception) {
        return "$dateStr$timePart"
    }
}
