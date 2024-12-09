package com.example.inventory.Alarma

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.inventory.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Alarma: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Alarma", "onReceive triggered") // Añadir un log para verificar si se llama al método
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Revisa tus recordatorios pendientes...")
                .setContentText("Revisa tus recordatorios pendientes... $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Esto asegura que la notificación tenga sonido, vibración, etc.
            notificationManager.notify(1, builder.build())
            Log.e("Alarma", "Notification sent") // Log para verificar que la notificación fue enviada
        }
    }

}

data class AlarmItem(
    val alarmTime : LocalDateTime,
    val tiempoMilis:Long,
    val message : String
)

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}


