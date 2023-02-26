package com.cassnyo.ephemeraltasks.data.alarm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cassnyo.ephemeraltasks.data.datasource.TasksDataSource

class ClearTasksWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val tasksDataSource = TasksDataSource(context)
        tasksDataSource.clearTasks()
        return Result.success()
    }

}