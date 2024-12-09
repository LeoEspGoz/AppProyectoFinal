package com.example.inventory.ui.item

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.inventory.R
import com.example.inventory.data.Note
import com.example.inventory.state.NotasEstado


class NotificacionWiewModel : ViewModel() {

    var state by mutableStateOf(NotasEstado())
        private set

    @Composable
    fun sendNotification(context: Context, fechaEjecucion: Long) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notificacionn = NotificationCompat.Builder(context, NotificacionApp.CHANNEL_ID)
            .setContentTitle(state.nombre)
            .setContentText("Descripcion")
            .setSmallIcon(R.drawable.notifications_24)
            .setAutoCancel(true)
            .build()

        scheduleNotification(context, notificacionn, fechaEjecucion)
    }

    private fun scheduleNotification(context: Context, notification: Notification, fechaEjecucion: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiverNotificacion::class.java)
        intent.putExtra("EXTRA_NOTIFICATION", notification)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1, // Puedes usar un ID único aquí
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                fechaEjecucion,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                fechaEjecucion,
                pendingIntent
            )
        }

        Log.e("Alarm", "Alarm scheduled for $fechaEjecucion")
    }
}
