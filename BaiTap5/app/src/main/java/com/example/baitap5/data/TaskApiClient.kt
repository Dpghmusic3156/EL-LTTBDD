package com.example.baitap5.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object TaskApiClient {
    private const val BASE_URL = "https://share.tryapps.store/mock/teach/tasks"
    private const val AUTH_TOKEN = "TuanTruong-uth-student"

    suspend fun getTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        try {
            val url = URL(BASE_URL)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Authorization", "Bearer $AUTH_TOKEN")
                setRequestProperty("Accept", "application/json")
                connectTimeout = 10000
                readTimeout = 10000
            }

            val code = conn.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = reader.readText()
                reader.close()
                val jsonArray = JSONArray(response)
                val list = mutableListOf<Task>()
                for (i in 0 until jsonArray.length()) {
                    list.add(Task.fromJson(jsonArray.getJSONObject(i)))
                }
                Result.success(list)
            } else {
                val errorStream = conn.errorStream ?: conn.inputStream
                val errorMsg = BufferedReader(InputStreamReader(errorStream)).readText()
                val parsedMsg = try {
                    JSONObject(errorMsg).optString("message", errorMsg)
                } catch (_: Exception) {
                    errorMsg
                }
                Result.failure(Exception(if (parsedMsg.isNotBlank()) parsedMsg else "Lỗi $code khi tải công việc"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTask(id: Int): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/$id")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Authorization", "Bearer $AUTH_TOKEN")
                setRequestProperty("Accept", "application/json")
                connectTimeout = 10000
                readTimeout = 10000
            }

            val code = conn.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = reader.readText()
                reader.close()
                Result.success(Task.fromJson(JSONObject(response)))
            } else {
                val errorStream = conn.errorStream ?: conn.inputStream
                val errorMsg = BufferedReader(InputStreamReader(errorStream)).readText()
                val parsedMsg = try {
                    JSONObject(errorMsg).optString("message", "Không tìm thấy công việc.")
                } catch (_: Exception) {
                    "Không tìm thấy công việc."
                }
                Result.failure(Exception(parsedMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTask(task: Task): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val url = URL(BASE_URL)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Authorization", "Bearer $AUTH_TOKEN")
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("Accept", "application/json")
                doOutput = true
                connectTimeout = 10000
                readTimeout = 10000
            }

            val body = task.toJson().apply { remove("id") }.toString()
            OutputStreamWriter(conn.outputStream).use { it.write(body) }

            val code = conn.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = reader.readText()
                reader.close()
                Result.success(Task.fromJson(JSONObject(response)))
            } else {
                val errorStream = conn.errorStream ?: conn.inputStream
                val errorMsg = BufferedReader(InputStreamReader(errorStream)).readText()
                val parsedMsg = try {
                    JSONObject(errorMsg).optString("message", errorMsg)
                } catch (_: Exception) {
                    errorMsg
                }
                Result.failure(Exception(if (parsedMsg.isNotBlank()) parsedMsg else "Lỗi tạo công việc: $code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(id: Int, patchJson: JSONObject): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/$id")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                // HttpURLConnection does not natively support PATCH method on older engines without setRequestMethod reflection or X-HTTP-Method-Override
                // Using reflection for standard PATCH on Android
                try {
                    val targetField = HttpURLConnection::class.java.getDeclaredField("method")
                    targetField.isAccessible = true
                    targetField.set(this, "PATCH")
                } catch (_: Exception) {
                    requestMethod = "POST"
                    setRequestProperty("X-HTTP-Method-Override", "PATCH")
                }
                setRequestProperty("Authorization", "Bearer $AUTH_TOKEN")
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("Accept", "application/json")
                doOutput = true
                connectTimeout = 10000
                readTimeout = 10000
            }

            val body = patchJson.toString()
            OutputStreamWriter(conn.outputStream).use { it.write(body) }

            val code = conn.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = reader.readText()
                reader.close()
                Result.success(Task.fromJson(JSONObject(response)))
            } else {
                val errorStream = conn.errorStream ?: conn.inputStream
                val errorMsg = BufferedReader(InputStreamReader(errorStream)).readText()
                val parsedMsg = try {
                    JSONObject(errorMsg).optString("message", errorMsg)
                } catch (_: Exception) {
                    errorMsg
                }
                Result.failure(Exception(if (parsedMsg.isNotBlank()) parsedMsg else "Lỗi cập nhật công việc: $code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/$id")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "DELETE"
                setRequestProperty("Authorization", "Bearer $AUTH_TOKEN")
                connectTimeout = 10000
                readTimeout = 10000
            }

            val code = conn.responseCode
            if (code in 200..299) {
                Result.success(Unit)
            } else {
                val errorStream = conn.errorStream ?: conn.inputStream
                val errorMsg = BufferedReader(InputStreamReader(errorStream)).readText()
                val parsedMsg = try {
                    JSONObject(errorMsg).optString("message", errorMsg)
                } catch (_: Exception) {
                    errorMsg
                }
                Result.failure(Exception(if (parsedMsg.isNotBlank()) parsedMsg else "Lỗi xóa công việc: $code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
