package com.example.baitap4_1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LibraryApp(
    repository: LibraryRepository = remember { LibraryRepository() }
) {
    var activeTab by remember { mutableIntStateOf(0) }
    var selectedStudentId by remember { mutableStateOf(repository.getSelectedStudentId()) }
    var refreshTrigger by remember { mutableIntStateOf(0) }
    var showBorrowDialog by remember { mutableStateOf(false) }
    var showSelectStudentDialog by remember { mutableStateOf(false) }
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }

    val currentStudent = remember(selectedStudentId, refreshTrigger) {
        repository.getSelectedStudent()
    }
    val borrowedBooks = remember(selectedStudentId, refreshTrigger) {
        repository.getBorrowedBooksForStudent(selectedStudentId)
    }

    Scaffold(
        bottomBar = {
            LibraryBottomNavigation(
                activeTab = activeTab,
                onTabSelected = { activeTab = it }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                0 -> LibraryManagementTab(
                    currentStudent = currentStudent,
                    borrowedBooks = borrowedBooks,
                    onChangeStudentClick = {
                        showSelectStudentDialog = true
                    },
                    onReturnBook = { bookId ->
                        repository.returnBook(bookId)
                        refreshTrigger++
                    },
                    onAddBookClick = {
                        showBorrowDialog = true
                    }
                )

                1 -> BookListTab(
                    allBooks = repository.getBooks(),
                    allLibraryItems = repository.getAllLibraryItems(),
                    onAddBookClick = { showAddBookDialog = true }
                )

                2 -> StudentListTab(
                    students = repository.getStudents(),
                    repository = repository,
                    selectedStudentId = selectedStudentId,
                    onSelectStudent = { student ->
                        repository.setSelectedStudentId(student.id)
                        selectedStudentId = student.id
                        activeTab = 0
                        refreshTrigger++
                    },
                    onAddStudentClick = { showAddStudentDialog = true }
                )
            }
        }
    }

    if (showBorrowDialog && currentStudent != null) {
        BorrowBookDialog(
            student = currentStudent,
            availableBooks = repository.getAvailableBooks(),
            onDismiss = { showBorrowDialog = false },
            onBorrow = { bookId ->
                repository.borrowBook(bookId, currentStudent.id)
                refreshTrigger++
                showBorrowDialog = false
            },
            onAddNewAndBorrow = { title, author ->
                val newBook = repository.addBook(title, author)
                repository.borrowBook(newBook.id, currentStudent.id)
                refreshTrigger++
                showBorrowDialog = false
            }
        )
    }

    if (showSelectStudentDialog) {
        SelectStudentDialog(
            students = repository.getStudents(),
            currentSelectedId = selectedStudentId,
            onDismiss = { showSelectStudentDialog = false },
            onSelect = { id ->
                repository.setSelectedStudentId(id)
                selectedStudentId = id
                refreshTrigger++
                showSelectStudentDialog = false
            }
        )
    }

    if (showAddBookDialog) {
        AddBookDialog(
            onDismiss = { showAddBookDialog = false },
            onAdd = { title, author, category ->
                repository.addBook(title, author, category)
                refreshTrigger++
                showAddBookDialog = false
            }
        )
    }

    if (showAddStudentDialog) {
        AddStudentDialog(
            onDismiss = { showAddStudentDialog = false },
            onAdd = { name, code, dept ->
                repository.addStudent(name, code, dept)
                refreshTrigger++
                showAddStudentDialog = false
            }
        )
    }
}

@Composable
fun LibraryManagementTab(
    currentStudent: Student?,
    borrowedBooks: List<Book>,
    onChangeStudentClick: () -> Unit,
    onReturnBook: (String) -> Unit,
    onAddBookClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hệ thống",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Quản lý Thư viện",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sinh viên",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, Color(0xFFC4C4C4), RoundedCornerShape(12.dp)),
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = currentStudent?.name ?: "Chưa chọn sinh viên",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Button(
                    onClick = onChangeStudentClick,
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E5BB8)
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Thay đổi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Danh sách sách",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFFE2E2E2)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (borrowedBooks.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Bạn chưa mượn quyển sách nào",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Nhấn 'Thêm' để bắt đầu hành trình đọc sách!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF4A4A4A),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(borrowedBooks, key = { it.id }) { book ->
                                BorrowedBookCard(
                                    book = book,
                                    onReturnClick = { onReturnBook(book.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddBookClick,
            modifier = Modifier
                .width(180.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E5BB8)
            )
        ) {
            Text(
                text = "Thêm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun BorrowedBookCard(
    book: Book,
    onReturnClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onReturnClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFFB71C1C), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Checked",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = book.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BookListTab(
    allBooks: List<Book>,
    allLibraryItems: List<LibraryItem>,
    onAddBookClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kho Sách Thư viện",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            IconButton(
                onClick = onAddBookClick,
                modifier = Modifier
                    .background(Color(0xFF1E5BB8), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm sách",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allLibraryItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Surface(
                                color = if (item is ReferenceBook) Color(0xFFFFECEB) else Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = item.getDisplayType(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (item is ReferenceBook) Color(0xFFC62828) else Color(0xFF1565C0),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.getSummary(),
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )

                        if (item is Borrowable) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (item.isAvailable()) "Trạng thái: Sẵn sàng mượn" else "Trạng thái: Đã có sinh viên mượn",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (item.isAvailable()) Color(0xFF2E7D32) else Color(0xFFD84315)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentListTab(
    students: List<Student>,
    repository: LibraryRepository,
    selectedStudentId: String,
    onSelectStudent: (Student) -> Unit,
    onAddStudentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Danh sách Sinh viên",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            IconButton(
                onClick = onAddStudentClick,
                modifier = Modifier
                    .background(Color(0xFF1E5BB8), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm SV",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(students) { student ->
                val borrowedCount = repository.getBorrowedBooksForStudent(student.id).size
                val isSelected = student.id == selectedStudentId

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectStudent(student) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFE8F0FE) else Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = student.name,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = student.getRoleDescription(),
                                fontSize = 13.sp,
                                color = Color(0xFF555555)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Đang mượn: $borrowedCount quyển sách",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E5BB8)
                            )
                        }

                        if (isSelected) {
                            Surface(
                                color = Color(0xFF1E5BB8),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Đang chọn",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryBottomNavigation(
    activeTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = "Quản lý",
                isSelected = activeTab == 0,
                onClick = { onTabSelected(0) }
            )
            NavItem(
                icon = Icons.Default.Book,
                label = "DS Sách",
                isSelected = activeTab == 1,
                onClick = { onTabSelected(1) }
            )
            NavItem(
                icon = Icons.Default.Person,
                label = "Sinh viên",
                isSelected = activeTab == 2,
                onClick = { onTabSelected(2) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) Color(0xFF1E5BB8) else Color(0xFF8E8E93)
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = tint
        )
    }
}

@Composable
fun SelectStudentDialog(
    students: List<Student>,
    currentSelectedId: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Chọn sinh viên", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                students.forEach { sv ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(sv.id) },
                        color = if (sv.id == currentSelectedId) Color(0xFFE8F0FE) else Color(0xFFF2F2F7),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = sv.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = sv.studentCode, color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

@Composable
fun BorrowBookDialog(
    student: Student,
    availableBooks: List<Book>,
    onDismiss: () -> Unit,
    onBorrow: (String) -> Unit,
    onAddNewAndBorrow: (String, String) -> Unit
) {
    var newBookTitle by remember { mutableStateOf("") }
    var newBookAuthor by remember { mutableStateOf("") }
    var isCreatingNew by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Thêm sách mượn cho ${student.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!isCreatingNew) {
                    Text(text = "Chọn từ sách sẵn có trong thư viện:", fontSize = 14.sp)
                    if (availableBooks.isEmpty()) {
                        Text(text = "(Hiện không có sách khả dụng nào)", color = Color.Red, fontSize = 13.sp)
                    } else {
                        availableBooks.forEach { book ->
                            Button(
                                onClick = { onBorrow(book.id) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E5BB8))
                            ) {
                                Text("${book.title} - ${book.author}")
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = { isCreatingNew = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Nhập tên sách mới mượn ngay")
                    }
                } else {
                    OutlinedTextField(
                        value = newBookTitle,
                        onValueChange = { newBookTitle = it },
                        label = { Text("Tên sách") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newBookAuthor,
                        onValueChange = { newBookAuthor = it },
                        label = { Text("Tác giả") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            if (isCreatingNew) {
                Button(
                    onClick = {
                        if (newBookTitle.isNotBlank()) {
                            onAddNewAndBorrow(
                                newBookTitle,
                                newBookAuthor.ifBlank { "Tác giả" }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E5BB8))
                ) {
                    Text("Mượn sách này")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Giáo trình") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm sách mới vào Kho Thư viện", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên sách") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Tác giả") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Thể loại / Chuyên ngành") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title, author.ifBlank { "Không rõ tác giả" }, category)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E5BB8))
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var dept by remember { mutableStateOf("CNTT") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Sinh viên mới", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Họ và tên") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Mã sinh viên (MSSV)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dept,
                    onValueChange = { dept = it },
                    label = { Text("Khoa / Lớp") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(name, code.ifBlank { "SV000" }, dept)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E5BB8))
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
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
