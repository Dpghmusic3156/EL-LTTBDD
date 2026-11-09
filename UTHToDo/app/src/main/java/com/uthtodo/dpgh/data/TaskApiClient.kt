package com.uthtodo.dpgh.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object TaskApiClient {
    private const val BASE_URL = "https://share.tryapps.store/mock/teach/tasks"
    private const val AUTH_TOKEN = "TuanTruong-uth-student"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun getTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(BASE_URL)
                .header("Authorization", "Bearer $AUTH_TOKEN")
                .header("Accept", "application/json")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val jsonArray = JSONArray(bodyStr)
                    val list = mutableListOf<Task>()
                    for (i in 0 until jsonArray.length()) {
                        list.add(Task.fromJson(jsonArray.getJSONObject(i)))
                    }
                    Result.success(list)
                } else {
                    val errorMsg = try {
                        JSONObject(bodyStr).optString("message", bodyStr)
                    } catch (_: Exception) {
                        bodyStr
                    }
                    Result.failure(Exception(if (errorMsg.isNotBlank()) errorMsg else "Lỗi ${response.code} khi tải công việc"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTask(id: Int): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/$id")
                .header("Authorization", "Bearer $AUTH_TOKEN")
                .header("Accept", "application/json")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    Result.success(Task.fromJson(JSONObject(bodyStr)))
                } else {
                    val errorMsg = try {
                        JSONObject(bodyStr).optString("message", "Không tìm thấy công việc.")
                    } catch (_: Exception) {
                        "Không tìm thấy công việc."
                    }
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTask(task: Task): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val body = task.toJson().apply { remove("id") }.toString()
            val request = Request.Builder()
                .url(BASE_URL)
                .header("Authorization", "Bearer $AUTH_TOKEN")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post(body.toRequestBody(JSON_MEDIA_TYPE))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    Result.success(Task.fromJson(JSONObject(bodyStr)))
                } else {
                    val errorMsg = try {
                        JSONObject(bodyStr).optString("message", bodyStr)
                    } catch (_: Exception) {
                        bodyStr
                    }
                    Result.failure(Exception(if (errorMsg.isNotBlank()) errorMsg else "Lỗi tạo công việc: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(id: Int, patchJson: JSONObject): Result<Task> = withContext(Dispatchers.IO) {
        try {
            val body = patchJson.toString()
            val request = Request.Builder()
                .url("$BASE_URL/$id")
                .header("Authorization", "Bearer $AUTH_TOKEN")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .patch(body.toRequestBody(JSON_MEDIA_TYPE))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    Result.success(Task.fromJson(JSONObject(bodyStr)))
                } else {
                    val errorMsg = try {
                        JSONObject(bodyStr).optString("message", bodyStr)
                    } catch (_: Exception) {
                        bodyStr
                    }
                    Result.failure(Exception(if (errorMsg.isNotBlank()) errorMsg else "Lỗi cập nhật công việc: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/$id")
                .header("Authorization", "Bearer $AUTH_TOKEN")
                .header("Accept", "application/json")
                .delete()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful || response.code == 404) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Lỗi xóa công việc: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
