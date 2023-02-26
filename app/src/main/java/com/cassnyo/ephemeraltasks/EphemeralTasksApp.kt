package com.cassnyo.ephemeraltasks

import android.app.Application
import com.cassnyo.ephemeraltasks.data.alarm.AlarmScheduler
import com.cassnyo.ephemeraltasks.data.alarm.ClearTasksAlarmScheduler

class EphemeralTasksApp : Application() {

    private val alarmScheduler: AlarmScheduler by lazy { ClearTasksAlarmScheduler(context = applicationContext) }

    override fun onCreate() {
        super.onCreate()
        alarmScheduler.scheduleAlarm()
    }
}