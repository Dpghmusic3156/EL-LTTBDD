package com.example.baitap5.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSession(
    val displayName: String = "Khách",
    val email: String = "",
    val isGuest: Boolean = true,
    val avatarInitials: String = "K"
)

object SessionManager {
    private const val PREFS_NAME = "uth_todo_prefs"
    private const val KEY_NAME = "user_name"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_IS_GUEST = "user_is_guest"
    private const val KEY_HAS_LOGGED_IN = "has_logged_in"

    private lateinit var prefs: SharedPreferences

    private val _session = MutableStateFlow(UserSession())
    val session: StateFlow<UserSession> = _session.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_NAME, "Khách") ?: "Khách"
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        val isGuest = prefs.getBoolean(KEY_IS_GUEST, true)
        _session.value = UserSession(
            displayName = name,
            email = email,
            isGuest = isGuest,
            avatarInitials = computeInitials(name, isGuest)
        )
    }

    fun hasLoggedIn(): Boolean {
        if (!::prefs.isInitialized) return false
        val firebaseUser = com.example.baitap5.FirebaseManager.currentUser()
        if (firebaseUser != null && !firebaseUser.isAnonymous) return true
        return prefs.getBoolean(KEY_HAS_LOGGED_IN, false)
    }

    fun setGoogleUser(name: String, email: String) {
        val safeName = if (name.isNotBlank()) name else (email.substringBefore("@").ifBlank { "Minh Anh" })
        prefs.edit().apply {
            putString(KEY_NAME, safeName)
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_GUEST, false)
            putBoolean(KEY_HAS_LOGGED_IN, true)
            apply()
        }
        _session.value = UserSession(
            displayName = safeName,
            email = email,
            isGuest = false,
            avatarInitials = computeInitials(safeName, false)
        )
    }

    fun setGuestUser() {
        prefs.edit().apply {
            putString(KEY_NAME, "Khách")
            putString(KEY_EMAIL, "")
            putBoolean(KEY_IS_GUEST, true)
            putBoolean(KEY_HAS_LOGGED_IN, true)
            apply()
        }
        _session.value = UserSession(
            displayName = "Khách",
            email = "",
            isGuest = true,
            avatarInitials = "K"
        )
    }

    fun logout() {
        prefs.edit().apply {
            putString(KEY_NAME, "Khách")
            putString(KEY_EMAIL, "")
            putBoolean(KEY_IS_GUEST, true)
            putBoolean(KEY_HAS_LOGGED_IN, false)
            apply()
        }
        _session.value = UserSession(
            displayName = "Khách",
            email = "",
            isGuest = true,
            avatarInitials = "K"
        )
        com.example.baitap5.FirebaseManager.signOut()
    }

    /**
     * Rule: Chữ cái đầu của 2 từ cuối trong tên ("Minh Anh" -> "MA", "Nguyễn Văn A" -> "VA").
     * Khách -> "K".
     */
    private fun computeInitials(name: String, isGuest: Boolean): String {
        if (isGuest || name.isBlank() || name == "Khách") return "K"
        val words = name.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        return when {
            words.size >= 2 -> {
                val last1 = words[words.size - 2].first().uppercase()
                val last2 = words[words.size - 1].first().uppercase()
                "$last1$last2"
            }
            words.size == 1 -> words[0].take(2).uppercase()
            else -> "K"
        }
    }
}
