package com.example.baitap4_1

abstract class LibraryItem(
    val id: String,
    open val title: String
) {
    abstract fun getDisplayType(): String
    abstract fun getSummary(): String
}

interface Borrowable {
    fun isAvailable(): Boolean
    fun borrowItem(studentId: String): Boolean
    fun returnItem(): Boolean
    fun getBorrowerId(): String?
}

abstract class Person(
    val id: String,
    open val name: String
) {
    abstract fun getRoleDescription(): String
}

class Book(
    id: String,
    title: String,
    val author: String,
    val category: String = "Giáo trình"
) : LibraryItem(id, title), Borrowable {

    private var borrowerId: String? = null
    private var isBorrowed: Boolean = false

    override fun getDisplayType(): String = "Sách đọc ($category)"

    override fun getSummary(): String = "Tác giả: $author"

    override fun isAvailable(): Boolean = !isBorrowed

    override fun borrowItem(studentId: String): Boolean {
        if (!isBorrowed) {
            isBorrowed = true
            borrowerId = studentId
            return true
        }
        return false
    }

    override fun returnItem(): Boolean {
        if (isBorrowed) {
            isBorrowed = false
            borrowerId = null
            return true
        }
        return false
    }

    override fun getBorrowerId(): String? = borrowerId
}

class ReferenceBook(
    id: String,
    title: String,
    val author: String,
    val locationRoom: String
) : LibraryItem(id, title) {

    override fun getDisplayType(): String = "Sách tham khảo (Tại chỗ)"

    override fun getSummary(): String = "Tác giả: $author • Phòng: $locationRoom"
}

class Student(
    id: String,
    name: String,
    val studentCode: String,
    val department: String = "CNTT"
) : Person(id, name) {

    override fun getRoleDescription(): String = "Sinh viên • MSSV: $studentCode ($department)"
}

class LibraryRepository {
    private val _students = mutableListOf<Student>()
    private val _books = mutableListOf<Book>()
    private val _referenceBooks = mutableListOf<ReferenceBook>()
    private var _selectedStudentId: String = "1"

    init {
        val svA = Student("1", "Nguyen Van A", "SV001", "CNTT")
        val svB = Student("2", "Nguyen Thi B", "SV002", "Kinh tế")
        val svC = Student("3", "Nguyen Van C", "SV003", "Điện tử")

        _students.addAll(listOf(svA, svB, svC))

        val b1 = Book("b1", "Sách 01", "Nguyễn Văn X", "CNTT")
        val b2 = Book("b2", "Sách 02", "Trần Thị Y", "Văn học")
        val b3 = Book("b3", "Sách 03", "Lê Văn Z", "Kỹ thuật")
        val b4 = Book("b4", "Sách 04", "Phạm Văn T", "Kinh tế")

        b1.borrowItem("1")
        b2.borrowItem("1")

        val b5 = Book("b5", "Sách 01", "Nguyễn Văn X", "CNTT")
        b5.borrowItem("2")

        _books.addAll(listOf(b1, b2, b3, b4, b5))

        _referenceBooks.add(ReferenceBook("ref1", "Từ điển Công nghệ Thông tin", "NXB KHKT", "P.302"))
    }

    fun getStudents(): List<Student> = _students.toList()

    fun getBooks(): List<Book> = _books.toList()

    fun getAllLibraryItems(): List<LibraryItem> {
        val all = mutableListOf<LibraryItem>()
        all.addAll(_books)
        all.addAll(_referenceBooks)
        return all
    }

    fun getSelectedStudent(): Student? {
        return _students.find { it.id == _selectedStudentId }
    }

    fun getSelectedStudentId(): String = _selectedStudentId

    fun setSelectedStudentId(id: String) {
        if (_students.any { it.id == id }) {
            _selectedStudentId = id
        }
    }

    fun selectNextStudent() {
        val currentIndex = _students.indexOfFirst { it.id == _selectedStudentId }
        if (currentIndex != -1) {
            val nextIndex = (currentIndex + 1) % _students.size
            _selectedStudentId = _students[nextIndex].id
        }
    }

    fun getBorrowedBooksForStudent(studentId: String): List<Book> {
        return _books.filter { it.getBorrowerId() == studentId }
    }

    fun getAvailableBooks(): List<Book> {
        return _books.filter { it.isAvailable() }
    }

    fun borrowBook(bookId: String, studentId: String): Boolean {
        val book = _books.find { it.id == bookId }
        return book?.borrowItem(studentId) ?: false
    }

    fun returnBook(bookId: String): Boolean {
        val book = _books.find { it.id == bookId }
        return book?.returnItem() ?: false
    }

    fun addBook(title: String, author: String, category: String = "Giáo trình"): Book {
        val newBook = Book(
            id = "b_${System.currentTimeMillis()}",
            title = title,
            author = author,
            category = category
        )
        _books.add(newBook)
        return newBook
    }

    fun addStudent(name: String, studentCode: String, department: String = "CNTT"): Student {
        val newStudent = Student(
            id = "${System.currentTimeMillis()}",
            name = name,
            studentCode = studentCode,
            department = department
        )
        _students.add(newStudent)
        return newStudent
    }
}
