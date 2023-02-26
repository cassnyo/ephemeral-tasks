package com.cassnyo.ephemeraltasks.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class ClearTasksAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val clearTasksRequest = OneTimeWorkRequestBuilder<ClearTasksWorker>().build()
        WorkManager.getInstance(context).enqueue(clearTasksRequest)
    }

}