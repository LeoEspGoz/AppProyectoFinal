package com.example.inventory.ui.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Note
import com.example.inventory.data.NotesRepository
import com.example.inventory.ui.navigation.NoteDetailsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val noteId: Int = checkNotNull(savedStateHandle[NoteDetailsDestination.noteIdArg])

    private val _noteDetails = MutableStateFlow<Note?>(null)
    val noteDetails: StateFlow<Note?> = _noteDetails

    init {
        loadNote()
    }

    // Cargar la nota junto con sus URIs multimedia
    private fun loadNote() {
        viewModelScope.launch {
            notesRepository.getNoteStream(noteId).collect { note ->
                _noteDetails.value = note
            }
        }
    }

    // Eliminar una nota
    fun deleteNote() {
        _noteDetails.value?.let { note ->
            viewModelScope.launch {
                notesRepository.deleteNote(note)
            }
        }
    }
}
