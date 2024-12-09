package com.example.inventory.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Note
import com.example.inventory.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteEntryViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _noteUiState = MutableStateFlow(NoteUiState())
    val noteUiState: StateFlow<NoteUiState> = _noteUiState

    fun updateTitle(newTitle: String) {
        val currentNoteDetails = _noteUiState.value.noteDetails ?: NoteDetails()
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = currentNoteDetails.copy(title = newTitle),
            isEntryValid = validateInput(newTitle, currentNoteDetails.content)
        )
    }

    fun updateContent(newContent: String) {
        val currentNoteDetails = _noteUiState.value.noteDetails ?: NoteDetails()
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = currentNoteDetails.copy(content = newContent),
            isEntryValid = validateInput(currentNoteDetails.title, newContent)
        )
    }

    fun updateFecha(newFecha: Long) {
        val currentNoteDetails = _noteUiState.value.noteDetails ?: NoteDetails()
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = currentNoteDetails.copy(fecha = newFecha)
        )
    }

    fun updateHora(newHora: Long) {
        val currentNoteDetails = _noteUiState.value.noteDetails ?: NoteDetails()
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = currentNoteDetails.copy(hora = newHora)
        )
    }

    fun updateMultimediaUris(uris: List<String>) {
        val currentNoteDetails = _noteUiState.value.noteDetails ?: NoteDetails()
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = currentNoteDetails.copy(multimediaUris = uris)
        )
    }

    private fun validateInput(title: String, content: String): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }

    fun saveNote() {
        val noteDetails = _noteUiState.value.noteDetails
        if (_noteUiState.value.isEntryValid && noteDetails != null) {
            viewModelScope.launch {
                try {
                    notesRepository.insertNote(noteDetails.toNote())
                } catch (e: Exception) {
                    // Manejo de errores
                    println("Error al guardar la nota: ${e.message}")
                }
            }
        }
    }
}
