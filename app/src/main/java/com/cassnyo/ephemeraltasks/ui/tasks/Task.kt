package com.cassnyo.ephemeraltasks.ui.tasks

import org.json.JSONArray
import org.json.JSONObject

data class Task(
    val id: String,
    val description: String,
    val completed: Boolean,
)

fun JSONObject.toTask() =
    Task(
        id = getString(TASK_ID),
        description = getString(TASK_DESCRIPTION),
        completed = getBoolean(TASK_COMPLETED),
    )

fun List<Task>.toJsonArray() =
    JSONArray(map { it.toJsonObject() })

fun Task.toJsonObject() =
    JSONObject().apply {
        put(TASK_ID, id)
        put(TASK_DESCRIPTION, description)
        put(TASK_COMPLETED, completed)
    }

private const val TASK_ID = "id"
private const val TASK_DESCRIPTION = "description"
private const val TASK_COMPLETED = "completed"