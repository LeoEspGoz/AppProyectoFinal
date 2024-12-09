package com.example.inventory.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Note
import com.example.inventory.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotesViewModel(notesRepository: NotesRepository) : ViewModel() {
    val notesUiState: StateFlow<NotesUiState> =
        notesRepository.getAllNotesStream().map { notes ->
            NotesUiState(notes)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotesUiState()
        )
}

data class NotesUiState(val notes: List<Note> = listOf())
