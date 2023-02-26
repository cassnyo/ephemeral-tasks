package com.cassnyo.ephemeraltasks

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cassnyo.ephemeraltasks.extension.iterator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private const val TASKS_FILE = "tasks"

val Context.tasksDataStore: DataStore<Preferences> by preferencesDataStore(name = TASKS_FILE)

class TasksDataSource(
    context: Context,
) {

    private val dataStore = context.tasksDataStore

    fun observeTasks(): Flow<List<Task>> =
        dataStore
            .observeTaskList()
            .distinctUntilChanged()

    suspend fun addTask(taskDescription: String) = withContext(Dispatchers.IO) {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            description = taskDescription,
            completed = false,
        )

        val updatedTasks = observeTasks()
            .first()
            .toMutableList()
            .apply { add(newTask) }

        dataStore.putTaskList(updatedTasks)
    }

    suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        val updatedTasks = observeTasks()
            .first()
            .toMutableList()
            .apply {
                val index = indexOfFirst { it.id == task.id }
                this[index] = task
            }
        dataStore.putTaskList(updatedTasks)
    }

    suspend fun removeTask(task: Task) = withContext(Dispatchers.IO) {
        val updatedTasks = observeTasks()
            .first()
            .toMutableList()
            .apply {
                removeIf { it.id == task.id }
            }
        dataStore.putTaskList(updatedTasks)
    }

    suspend fun clearTasks(): Unit = withContext(Dispatchers.IO) {
        dataStore.edit { it.clear() }
    }

    private fun DataStore<Preferences>.observeTaskList(): Flow<List<Task>> =
        data.map {
            val jsonTasks = it.get(key = PREFERENCE_TASKS) ?: return@map emptyList<Task>()
            val jsonArray = JSONArray(jsonTasks)

            val taskList = mutableListOf<Task>()
            for (jsonObject in jsonArray.iterator<JSONObject>()) {
                taskList.add(jsonObject.toTask())
            }

            taskList
        }

    private suspend fun DataStore<Preferences>.putTaskList(taskList: List<Task>) {
        val jsonTasks = taskList.toJsonArray().toString()
        edit { it[PREFERENCE_TASKS] = jsonTasks }
    }

    companion object {
        private val PREFERENCE_TASKS = stringPreferencesKey("tasks")
    }

}