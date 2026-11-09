package com.example.baitap5

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.baitap5.data.TaskItem
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage

object FirebaseManager {

    private const val TAG = "FirebaseManager"

    // Services
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    val remoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }
    val crashlytics: FirebaseCrashlytics by lazy { FirebaseCrashlytics.getInstance() }

    private var analytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        analytics = FirebaseAnalytics.getInstance(context)

        // Setup Remote Config
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf("welcome_slogan" to "Việc học, việc nhà — gọn gàng mỗi ngày")
        )
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Remote config fetched: ${remoteConfig.getString("welcome_slogan")}")
                }
            }
    }

    fun getWelcomeSlogan(): String {
        val slogan = remoteConfig.getString("welcome_slogan")
        return if (slogan.isNotBlank()) slogan else "Việc học, việc nhà — gọn gàng mỗi ngày"
    }

    // --- Authentication ---
    fun currentUser(): FirebaseUser? = auth.currentUser

    fun signInWithGoogle(
        idToken: String,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                result.user?.let { user ->
                    logEvent("login_google_success", Bundle().apply {
                        putString("user_id", user.uid)
                    })
                    onSuccess(user)
                } ?: onError("Không tìm thấy thông tin tài khoản.")
            }
            .addOnFailureListener { e ->
                crashlytics.recordException(e)
                onError(e.localizedMessage ?: "Đăng nhập Google thất bại.")
            }
    }

    fun signInAnonymously(
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                result.user?.let { user ->
                    logEvent("login_guest_success", Bundle().apply {
                        putString("user_id", user.uid)
                    })
                    onSuccess(user)
                } ?: onError("Lỗi đăng nhập ẩn danh.")
            }
            .addOnFailureListener { e ->
                crashlytics.recordException(e)
                onError(e.localizedMessage ?: "Đăng nhập ẩn danh thất bại.")
            }
    }

    fun signOut() {
        auth.signOut()
        logEvent("user_signed_out", Bundle())
    }

    // --- Cloud Firestore (Todo Tasks) ---
    private var tasksListenerRegistration: ListenerRegistration? = null

    fun listenToTasks(
        onUpdate: (List<TaskItem>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        val collection = firestore.collection("todos")

        tasksListenerRegistration?.remove()
        tasksListenerRegistration = collection.orderBy("id").addSnapshotListener { snapshot, error ->
            if (error != null) {
                crashlytics.recordException(error)
                onError(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                if (snapshot.isEmpty) {
                    // Seed initial tasks if collection is empty
                    seedDefaultTasks()
                } else {
                    val taskList = snapshot.documents.mapNotNull { doc ->
                        val id = doc.getLong("id")?.toInt() ?: return@mapNotNull null
                        val title = doc.getString("title") ?: ""
                        val category = doc.getString("category") ?: "Học tập"
                        val time = doc.getString("time") ?: "09:00"
                        val isCompleted = doc.getBoolean("isCompleted") ?: false
                        TaskItem(id, title, category, time, isCompleted)
                    }
                    onUpdate(taskList)
                }
            }
        }
    }

    private fun seedDefaultTasks() {
        val defaults = listOf(
            TaskItem(1, "Nộp bài tập Lập trình di động", "Học tập", "09:00", true),
            TaskItem(2, "Ôn chương 3 – Cấu trúc dữ liệu", "Học tập", "14:00", false),
            TaskItem(3, "Đi chợ mua đồ cho tuần", "Cuộc sống", "17:30", false),
            TaskItem(4, "Tập thể dục 30 phút", "Cuộc sống", "06:30", true)
        )
        val collection = firestore.collection("todos")
        defaults.forEach { item ->
            collection.document("task_${item.id}").set(
                mapOf(
                    "id" to item.id,
                    "title" to item.title,
                    "category" to item.category,
                    "time" to item.time,
                    "isCompleted" to item.isCompleted
                )
            )
        }
    }

    fun toggleTask(taskId: Int, currentCompleted: Boolean) {
        firestore.collection("todos").document("task_$taskId")
            .update("isCompleted", !currentCompleted)
            .addOnSuccessListener {
                logEvent("task_status_toggled", Bundle().apply {
                    putInt("task_id", taskId)
                    putBoolean("new_status", !currentCompleted)
                })
            }
            .addOnFailureListener { e ->
                crashlytics.recordException(e)
            }
    }

    fun addTask(title: String, category: String, time: String) {
        val newId = System.currentTimeMillis().toInt()
        val docData = mapOf(
            "id" to newId,
            "title" to title,
            "category" to category,
            "time" to time,
            "isCompleted" to false
        )
        firestore.collection("todos").document("task_$newId")
            .set(docData)
            .addOnSuccessListener {
                logEvent("task_added", Bundle().apply {
                    putString("title", title)
                    putString("category", category)
                })
            }
    }

    // --- Cloud Storage (Avatar Upload) ---
    fun uploadAvatar(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: "anonymous_${System.currentTimeMillis()}"
        val ref = storage.reference.child("avatars/$userId.jpg")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    logEvent("avatar_uploaded", Bundle().apply {
                        putString("user_id", userId)
                    })
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { e ->
                crashlytics.recordException(e)
                onError(e.localizedMessage ?: "Tải ảnh đại diện lên Storage thất bại.")
            }
    }

    // --- Analytics ---
    fun logEvent(name: String, params: Bundle = Bundle()) {
        analytics?.logEvent(name, params)
    }

    fun stopListening() {
        tasksListenerRegistration?.remove()
    }
}
