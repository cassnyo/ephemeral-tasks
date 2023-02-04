package com.cassnyo.ephemeraltasks

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.json.JSONArray
import org.json.JSONObject

class TasksDataSource(
    private val context: Context,
) {

    private val sharedPreferences = context.applicationContext.getSharedPreferences(TASKS_FILE, Context.MODE_PRIVATE)

    fun observeTasks(): Flow<List<Task>> {
        val serializedTasks = sharedPreferences
            .getString(TASKS_KEY, null)

        return if (serializedTasks == null) {
            flowOf(emptyList())
        } else {
            flowOf(deserializeTasks(serializedTasks))
        }
    }

    fun addTask(task: Task) {
        val jsonTask = task.toJSONTask()
        val jsonTasks = sharedPreferences
            .getString(TASKS_KEY, "[]")
            ?.let { json ->
                val array = JSONArray(json)
                array.put(array.length(), jsonTask)
                array.toString()
            }

        sharedPreferences
            .edit {
                putString(TASKS_KEY, jsonTasks)
            }
    }

    fun updateTask(task: Task) {
        // TODO
    }

    private fun Task.toJSONTask() = JSONObject().apply {
        put(TASK_DESCRIPTION_PROPERTY, description)
        put(TASK_COMPLETED_PROPERTY, (if (completed) 1 else 0))
    }

    private fun deserializeTasks(json: String): List<Task> {
        val tasks = mutableListOf<Task>()
        val array = JSONArray(json)
        (0 until array.length()).forEach {index ->
            val jsonTask = array.getJSONObject(index)
            tasks.add(
                Task(
                    jsonTask.getString(TASK_DESCRIPTION_PROPERTY),
                    jsonTask.getInt(TASK_COMPLETED_PROPERTY) == 1
                )
            )
        }

        return tasks
    }

    companion object {
        private const val TASKS_FILE = "tasks"
        private const val TASKS_KEY = "key_tasks"

        private const val TASK_DESCRIPTION_PROPERTY = "description"
        private const val TASK_COMPLETED_PROPERTY = "completed"
    }

}