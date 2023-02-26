package com.cassnyo.ephemeraltasks.common

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cassnyo.ephemeraltasks.TasksDataSource

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