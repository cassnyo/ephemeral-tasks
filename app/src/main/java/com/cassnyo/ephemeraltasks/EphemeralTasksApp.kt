package com.cassnyo.ephemeraltasks

import android.app.Application
import com.cassnyo.ephemeraltasks.common.AlarmScheduler
import com.cassnyo.ephemeraltasks.common.ClearTasksAlarmScheduler

class EphemeralTasksApp : Application() {

    private val alarmScheduler: AlarmScheduler by lazy { ClearTasksAlarmScheduler(context = applicationContext) }

    override fun onCreate() {
        super.onCreate()
        alarmScheduler.scheduleAlarm()
    }
}