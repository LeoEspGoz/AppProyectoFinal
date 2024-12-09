package com.example.inventory.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteEntryBody(
    noteUiState: NoteUiState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = noteUiState.noteDetails?.title ?: "",
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = noteUiState.noteDetails?.content ?: "",
            onValueChange = onContentChange,
            label = { Text("Content") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = onSaveClick,
            enabled = noteUiState.noteDetails?.isEntryValid() == true, // Llamando a la función de la clase Note
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Note")
        }
    }
}