package com.example.inventory.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Note
import com.example.inventory.data.NotesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoteEditViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _noteUiState = MutableStateFlow(NoteUiState())
    val noteUiState: StateFlow<NoteUiState> = _noteUiState

    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            notesRepository.getNoteStream(noteId).collect { note ->
                // Convertir Note a NoteDetails
                _noteUiState.value = NoteUiState(noteDetails = note?.toNoteDetails())
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = _noteUiState.value.noteDetails?.copy(title = newTitle)
        )
    }

    fun updateContent(newContent: String) {
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = _noteUiState.value.noteDetails?.copy(content = newContent)
        )
    }

    suspend fun saveNote() {
        _noteUiState.value.noteDetails?.let { noteDetails ->
            // Convertir NoteDetails a Note antes de guardar
            notesRepository.updateNote(noteDetails.toNote())
        }
    }

    fun saveNoteAndNavigate(navigateBack: () -> Unit) {
        viewModelScope.launch {
            saveNote()
            navigateBack()
        }
    }
}
