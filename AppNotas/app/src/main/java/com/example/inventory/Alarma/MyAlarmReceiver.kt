package com.example.inventory.Alarma

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.inventory.MainActivity
import com.example.inventory.R
import com.example.inventory.ui.item.NotificacionApp


class MyAlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 5
        const val CHANNEL_ID = "alarm_channel_id" // Identificador del canal de notificaciones
    }

    override fun onReceive(context: Context, intent: Intent?) {
        // Obtener los datos del intent
        val title = intent?.getStringExtra("title") ?: "Recordatorio"
        val message = intent?.getStringExtra("message") ?: "Tienes tareas pendientes."

        // Asegurarse de que el canal de notificación está creado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Alarmas",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Canal para notificaciones de alarmas programadas"
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        // Crear y mostrar la notificación
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Cambia este ícono por uno válido
            .setContentTitle(title) // Título de la notificación
            .setContentText(message) // Mensaje de la notificación
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Alta prioridad
            .setAutoCancel(true) // Se elimina al hacer clic
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}

