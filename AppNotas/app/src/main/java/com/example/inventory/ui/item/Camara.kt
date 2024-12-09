package com.example.inventory.ui.item

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun CameraButton() {
    var imageBitmaps by remember { mutableStateOf<List<Bitmap>>(listOf()) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val openCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {

            imageBitmaps = imageBitmaps + bitmap
        }
    }

    // Contrato para solicitar permisos
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (isGranted) {
            openCamera.launch(null)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    var uri: Uri by remember { mutableStateOf(Uri.EMPTY) }

    // tomar video INICIO --------------------------------------------------------------------------
    var videoUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var listVideoUri by remember {
        mutableStateOf(listOf<Uri>())
    }
    var showVideo by remember {
        mutableStateOf(false)
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) {
            uri.let {
                listVideoUri = listVideoUri + it // Agrega la Uri a la lista
            }
            videoUri = uri
            showVideo = !showVideo
        }
    }
    if(showVideo){
        DialogShowVideoTake(
            onDismiss = { showVideo = !showVideo },
            videoUri = videoUri
        )
    }


    Column {
        Button(onClick = {
            // Verificar si se tiene el permiso
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                hasCameraPermission = true
                openCamera.launch(null)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Icon(Icons.Filled.AccountBox, contentDescription = "Abrir cámara")
            IconButton(
                onClick = {
                    uri = ComposeFileProvider.getImageUri(context)
                    videoLauncher.launch(uri)
                },
                modifier = Modifier
                    .width(75.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
                    Text(
                        text = "Video",
                        style = typography.bodySmall
                    )
                }
            }
        }

        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(imageBitmaps) { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .clickable {
                            selectedBitmap = bitmap
                            showDeleteDialog = true
                        }
                )
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar imagen") },
                text = { Text("¿Deseas eliminar esta imagen?") },
                confirmButton = {
                    Button(onClick = {
                        showDeleteDialog = false
                        selectedBitmap?.let { bitmap ->
                            imageBitmaps = imageBitmaps.filter { it != bitmap }
                        }
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}