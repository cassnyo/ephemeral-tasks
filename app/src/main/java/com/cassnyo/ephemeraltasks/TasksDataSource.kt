package com.cassnyo.ephemeraltasks

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.cassnyo.ephemeraltasks.extension.iterator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class TasksDataSource(
    context: Context,
) {

    private val sharedPreferences = context.applicationContext.getSharedPreferences(TASKS_FILE, Context.MODE_PRIVATE)

    fun observeTasks(): Flow<List<Task>> = flow {
        emit(sharedPreferences.getTaskList())
    }.onStart {
        emit(emptyList())
    }.distinctUntilChanged()

    fun addTask(taskDescription: String) {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            description = taskDescription,
            completed = false,
        )
        val updatedTasks = sharedPreferences
            .getTaskList()
            .toMutableList()
            .apply { add(newTask) }
        sharedPreferences.putTaskList(updatedTasks)
    }

    fun updateTask(task: Task) {
        val updatedTasks = sharedPreferences
            .getTaskList()
            .toMutableList()
            .apply {
                val index = indexOfFirst { it.id == task.id }
                this[index] = task
            }
        sharedPreferences.putTaskList(updatedTasks)
    }

    private fun SharedPreferences.getTaskList(): List<Task> {
        val jsonTasks = getString(TASKS_KEY, null) ?: return emptyList()
        val jsonArray = JSONArray(jsonTasks)

        val taskList = mutableListOf<Task>()
        for (jsonObject in jsonArray.iterator<JSONObject>()) {
            taskList.add(jsonObject.toTask())
        }

        return taskList
    }

    private fun SharedPreferences.putTaskList(taskList: List<Task>) {
        val jsonTasks = taskList.toJsonArray().toString()
        edit { putString(TASKS_KEY, jsonTasks) }
    }

    companion object {
        private const val TASKS_FILE = "tasks"
        private const val TASKS_KEY = "key_tasks"
    }

}