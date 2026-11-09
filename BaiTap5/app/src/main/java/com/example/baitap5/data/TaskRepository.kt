package com.example.baitap5.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TaskRepository {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Helper: today's date formatted as yyyy-MM-dd
    fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    suspend fun refreshTasks(): Result<List<Task>> {
        _isLoading.value = true
        _errorMessage.value = null
        val result = TaskApiClient.getTasks()
        _isLoading.value = false
        result.onSuccess { fetchedList ->
            // If local list has pending updates or items, merge or replace
            _tasks.value = fetchedList
        }.onFailure {
            _errorMessage.value = it.message
        }
        return result
    }

    suspend fun toggleTaskDone(id: Int): Result<Task> {
        val current = _tasks.value.find { it.id == id } ?: return Result.failure(Exception("Không tìm thấy"))
        val newDone = !current.done
        val updatedTask = current.copy(done = newDone)
        // Optimistic update
        _tasks.value = _tasks.value.map { if (it.id == id) updatedTask else it }

        val patchObj = JSONObject().apply { put("done", newDone) }
        val apiResult = TaskApiClient.updateTask(id, patchObj)
        return if (apiResult.isSuccess) {
            Result.success(updatedTask)
        } else {
            // Revert on failure
            _tasks.value = _tasks.value.map { if (it.id == id) current else it }
            apiResult
        }
    }

    suspend fun addTask(task: Task): Result<Task> {
        _isLoading.value = true
        val apiResult = TaskApiClient.createTask(task)
        _isLoading.value = false
        apiResult.onSuccess { createdTask ->
            // Assign a local unique ID if server returns 0
            val finalTask = if (createdTask.id > 0) createdTask else task.copy(id = (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1)
            _tasks.value = listOf(finalTask) + _tasks.value
        }.onFailure {
            // Fallback optimistic addition so user isn't blocked by mock server
            val localTask = task.copy(id = (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1)
            _tasks.value = listOf(localTask) + _tasks.value
            return Result.success(localTask)
        }
        return apiResult
    }

    suspend fun updateTaskDetails(task: Task): Result<Task> {
        _isLoading.value = true
        val patchObj = JSONObject().apply {
            put("title", task.title)
            put("note", task.note)
            put("category", task.category)
            put("date", task.date)
            put("time", task.time)
            put("priority", task.priority)
            put("remind", task.remind)
            put("done", task.done)
        }
        val apiResult = TaskApiClient.updateTask(task.id, patchObj)
        _isLoading.value = false
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
        return Result.success(task)
    }

    suspend fun updateSubtasks(taskId: Int, newSubtasks: List<Subtask>): Result<Task> {
        val current = _tasks.value.find { it.id == taskId } ?: return Result.failure(Exception("Không tìm thấy"))
        val updatedTask = current.copy(subtasks = newSubtasks)
        _tasks.value = _tasks.value.map { if (it.id == taskId) updatedTask else it }

        val patchObj = JSONObject().apply {
            val arr = JSONArray()
            newSubtasks.forEach { arr.put(it.toJson()) }
            put("subtasks", arr)
        }
        TaskApiClient.updateTask(taskId, patchObj)
        return Result.success(updatedTask)
    }

    suspend fun deleteTask(id: Int): Result<Unit> {
        _isLoading.value = true
        val apiResult = TaskApiClient.deleteTask(id)
        _isLoading.value = false
        _tasks.value = _tasks.value.filterNot { it.id == id }
        return Result.success(Unit)
    }

    fun getTaskById(id: Int): Task? {
        return _tasks.value.find { it.id == id }
    }
}
