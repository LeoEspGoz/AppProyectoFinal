package com.example.inventory.ui.item

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore

fun createMediaUri(context: Context, type: String): Uri? {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        // Agregar extensión al nombre del archivo
        val fileExtension = if (type == "image") ".jpg" else ".mp4"
        put(MediaStore.MediaColumns.DISPLAY_NAME, "Media_${System.currentTimeMillis()}$fileExtension")
        put(
            MediaStore.MediaColumns.MIME_TYPE,
            if (type == "image") "image/jpeg" else "video/mp4"
        )
        // Guardar en el directorio correcto
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    return if (type == "image") {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    } else {
        contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}
