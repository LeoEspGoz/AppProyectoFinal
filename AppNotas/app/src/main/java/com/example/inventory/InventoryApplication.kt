package com.example.inventory

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.inventory.data.AppContainer
import com.example.inventory.data.AppDataContainer

class InventoryApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        createNotificationChannel() // Crear el canal de notificaciones
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel_id" // Identificador único del canal
            val channelName = "Canal de Alarmas"
            val channelDescription = "Canal para notificaciones de alarmas programadas"
            val importance = NotificationManager.IMPORTANCE_HIGH // Alta prioridad

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            // Registrar el canal en el sistema
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
