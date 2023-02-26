package com.cassnyo.ephemeraltasks.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class ClearTasksAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager by lazy { context.getSystemService(AlarmManager::class.java) }

    override fun scheduleAlarm() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = createInexactAlarmIntent(ALARM_REQUEST_CODE)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun createInexactAlarmIntent(alarmRequestCode: Int): PendingIntent {
        val intent = Intent(context, ClearTasksAlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            alarmRequestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val ALARM_REQUEST_CODE = 10101010
    }

}