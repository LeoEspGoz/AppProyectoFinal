// File: NotesScreen.kt
package com.example.inventory.ui.notes

import android.net.Uri
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.inventory.R
import com.example.inventory.data.Note
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.AudioPlayer
import com.example.inventory.ui.item.VideoPlayer
import com.example.inventory.ui.item.getMimeType
import com.example.inventory.ui.navigation.NavigationDestination
import java.text.SimpleDateFormat
import java.util.*

object NotesDestination : NavigationDestination {
    override val route = "notes"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navigateToNoteEntry: () -> Unit,
    navigateToNoteDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val notesUiState by viewModel.notesUiState.collectAsState()
    var isReminderView by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isReminderView) "Recordatorios" else "Notas")
                },
                actions = {
                    // Switch para alternar entre Notas y Recordatorios
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Switch(
                            checked = isReminderView,
                            onCheckedChange = { isReminderView = it }
                        )
                    }

                    // Botón para agregar una nueva nota o recordatorio
                    IconButton(onClick = {
                        navigateToNoteEntry()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar ${if (isReminderView) "Recordatorio" else "Nota"}"
                        )
                    }
                }
            )
        }
    ) { padding ->
        // Filtrar notas o recordatorios según el estado del Switch
        val filteredNotes = if (isReminderView) {
            notesUiState.notes.filter { it.fecha != 0L || it.hora != 0L } // Recordatorios
        } else {
            notesUiState.notes.filter { it.fecha == 0L && it.hora == 0L } // Notas
        }

        NotesList(
            notes = filteredNotes,
            onNoteClick = navigateToNoteDetail,
            modifier = modifier.padding(padding)
        )
    }
}

@Composable
private fun NotesList(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(notes, key = { it.id }) { note ->
            NoteItem(note = note, onClick = { onNoteClick(note.id) })
        }
    }
}


@Composable
fun PlaySavedVideo(videoUri: Uri, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(videoUri)
                        setOnPreparedListener { mediaPlayer ->
                            mediaPlayer.start() // Inicia el video automáticamente
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun NoteItem(
    note: Note,
    onClick: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    // Acceder al Context desde la composable
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = note.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)

            // Mostrar Fecha solo si es válida
            if (note.fecha != 0L) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fecha: ${dateFormatter.format(Date(note.fecha))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Mostrar Hora solo si es válida
            if (note.hora != 0L) {
                Text(
                    text = "Hora: ${timeFormatter.format(Date(note.hora))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar multimedia
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(note.multimediaUris) { uri ->
                    val mimeType = getMimeType(Uri.parse(uri), context)
                    when {
                        mimeType?.startsWith("image/") == true -> {
                            Image(
                                painter = rememberAsyncImagePainter(model = Uri.parse(uri)),
                                contentDescription = "Imagen multimedia",
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(150.dp)
                            )
                        }
                        mimeType?.startsWith("audio/") == true -> {
                            AudioPlayer(audioUri = Uri.parse(uri))
                        }
                        mimeType?.startsWith("video/") == true -> {
                            VideoPlayer(videoUri = Uri.parse(uri), modifier = Modifier.height(200.dp))
                        }
                        else -> {
                            Text("Formato no soportado")
                        }
                    }
                }
            }
        }
    }

    // Muestra el diálogo para reproducir video si un URI está seleccionado
    selectedVideoUri?.let {
        PlaySavedVideo(
            videoUri = it,
            onDismiss = { selectedVideoUri = null }
        )
    }
}