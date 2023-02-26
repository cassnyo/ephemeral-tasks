package com.cassnyo.ephemeraltasks.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class ClearTasksAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val clearTasksRequest = OneTimeWorkRequestBuilder<ClearTasksWorker>().build()
        WorkManager.getInstance(context).enqueue(clearTasksRequest)
    }

}