package com.example.inventory.Alarma


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.inventory.ui.item.AlarmReceiverNotificacion
import java.time.format.DateTimeFormatter
import android.provider.Settings
import androidx.annotation.RequiresApi
import android.net.Uri
import com.example.inventory.Alarma.AlarmItem
import com.example.inventory.Alarma.MyAlarmReceiver


class AlarmSchedulerImpl(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @RequiresApi(Build.VERSION_CODES.S)
    fun schedule(alarmItem: AlarmItem) {
        // Verificar si se pueden programar alarmas exactas
        if (!alarmManager.canScheduleExactAlarms()) {
            Log.e("AlarmSchedulerImpl", "Exact alarm scheduling is not permitted")
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
            return
        }

        if (alarmItem.tiempoMilis <= System.currentTimeMillis()) {
            Log.e("AlarmSchedulerImpl", "Alarm time is in the past")
            return
        }

        val intent = Intent(context, MyAlarmReceiver::class.java).apply {
            putExtra("title", "Revisa tus recordatorios pendientes...")
            putExtra("message", alarmItem.message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmItem.tiempoMilis,
                pendingIntent
            )
            Log.d("AlarmSchedulerImpl", "Alarm set for: ${alarmItem.alarmTime}")
        } catch (e: SecurityException) {
            Log.e("AlarmSchedulerImpl", "SecurityException: Unable to schedule exact alarm", e)
        }
    }
}
