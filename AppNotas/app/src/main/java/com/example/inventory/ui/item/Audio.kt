package com.example.inventory.ui.item

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

@Composable
fun AudioRecorderButton() {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    val mediaRecorder = remember { MediaRecorder() }
    val mediaPlayer = MediaPlayer()

    var audioFiles by remember { mutableStateOf(listOf<File>()) }

    val getNextAudioFile: () -> File = {
        File(context.externalCacheDir, "audio_recording_${audioFiles.size + 1}.3gp")
    }

    val startRecording = {
        val audioFile = getNextAudioFile()
        if (!context.externalCacheDir?.exists()!!) {
            context.externalCacheDir?.mkdirs()
        }
        Log.d("AudioRecorder", "Intentando iniciar la grabación")
        try {
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(audioFile.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                prepare()
                start()
            }
            isRecording = true
            audioFiles = audioFiles + audioFile
            Log.d("AudioRecorder", "Grabación iniciada")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al iniciar la grabación", e)
        }
    }

    val stopRecording = {
        Log.d("AudioRecorder", "Intentando detener la grabación")
        try {
            mediaRecorder.apply {
                stop()
                reset()
            }
            isRecording = false
            Log.d("AudioRecorder", "Grabación detenida")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al detener la grabación", e)
        }
    }
    val startPlaying: (File) -> Unit = { audioFile ->
        if (!audioFile.exists()) {
            Log.e("AudioPlayer", "El archivo no existe: ${audioFile.absolutePath}")

        }

        // Asegúrate de que el MediaPlayer se reinicie antes de configurarlo nuevamente
        try {
            mediaPlayer.reset()  // Reinicia el MediaPlayer
            mediaPlayer.setDataSource(audioFile.absolutePath) // Establece la fuente de datos
            mediaPlayer.prepare() // Prepara el MediaPlayer para la reproducción
            mediaPlayer.start() // Inicia la reproducción
            isPlaying = true

            mediaPlayer.setOnCompletionListener {
                isPlaying = false
            }
        } catch (e: IOException) {
            Log.e("AudioPlayer", "Error al reproducir el audio: ${e.message}", e)
        }
    }


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
        if (isGranted && !isRecording) {
            startRecording()
        } else {
            Toast.makeText(context, "Permiso de grabación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    Column {
        IconButton(onClick = {
            if (hasPermission) {
                if (isRecording) stopRecording() else startRecording()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }) {
            if (isRecording) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Detener Grabación", tint = Color.Red)
            } else {
                Icon(Icons.Filled.Done, contentDescription = "Iniciar Grabación", tint = Color.Black)
            }
        }

        audioFiles.forEachIndexed { index, audioFile ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Button(onClick = { startPlaying(audioFile) }) {
                    Text("Audio ${index + 1}")
                }
                IconButton(onClick = {
                    audioFiles = audioFiles.filter { it != audioFile }
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Audio", tint = Color.Red)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
            mediaRecorder.release()
        }
    }
}
