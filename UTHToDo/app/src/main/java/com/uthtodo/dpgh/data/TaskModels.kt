package com.uthtodo.dpgh.data

import org.json.JSONArray
import org.json.JSONObject

data class TaskItem(
    val id: Int,
    val title: String,
    val category: String,
    val time: String,
    val isCompleted: Boolean
)

data class Subtask(
    val title: String,
    val done: Boolean = false
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("title", title)
            put("done", done)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): Subtask {
            return Subtask(
                title = json.optString("title", ""),
                done = json.optBoolean("done", false)
            )
        }
    }
}

data class Task(
    val id: Int = 0,
    val title: String,
    val note: String = "",
    val category: String = "study", // "study" | "life"
    val date: String = "",          // "yyyy-MM-dd"
    val time: String = "19:00",     // "HH:mm"
    val priority: String = "medium", // "low" | "medium" | "high"
    val remind: Boolean = true,
    val done: Boolean = false,
    val subtasks: List<Subtask> = emptyList()
) {
    val categoryLabel: String
        get() = when (category.lowercase()) {
            "study" -> "Học tập"
            "life" -> "Cuộc sống"
            else -> category
        }

    val priorityLabel: String
        get() = when (priority.lowercase()) {
            "high" -> "Cao"
            "low" -> "Thấp"
            else -> "Vừa"
        }

    fun toJson(): JSONObject {
        return JSONObject().apply {
            if (id > 0) put("id", id)
            put("title", title)
            put("note", note)
            put("category", category)
            put("date", date)
            put("time", time)
            put("priority", priority)
            put("remind", remind)
            put("done", done)
            val subArray = JSONArray()
            subtasks.forEach { subArray.put(it.toJson()) }
            put("subtasks", subArray)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): Task {
            val subList = mutableListOf<Subtask>()
            val subArray = json.optJSONArray("subtasks")
            if (subArray != null) {
                for (i in 0 until subArray.length()) {
                    val subObj = subArray.optJSONObject(i)
                    if (subObj != null) {
                        subList.add(Subtask.fromJson(subObj))
                    }
                }
            }
            return Task(
                id = json.optInt("id", 0),
                title = json.optString("title", ""),
                note = json.optString("note", ""),
                category = json.optString("category", "study"),
                date = json.optString("date", ""),
                time = json.optString("time", "19:00"),
                priority = json.optString("priority", "medium"),
                remind = json.optBoolean("remind", true),
                done = json.optBoolean("done", false),
                subtasks = subList
            )
        }
    }
}
