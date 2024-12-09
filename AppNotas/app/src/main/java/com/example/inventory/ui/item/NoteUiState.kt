package com.example.inventory.ui.item

data class NoteUiState(
    val noteDetails: NoteDetails? = null, // Asegúrate de que sea nullable
    val isEntryValid: Boolean = false // Esto puede ser calculado si es necesario

)
