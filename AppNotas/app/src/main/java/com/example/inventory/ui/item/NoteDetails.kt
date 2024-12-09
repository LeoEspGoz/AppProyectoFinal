package com.example.inventory.ui.item
import com.example.inventory.data.Note

data class NoteDetails(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val fecha: Long = 0L, // Timestamp para la fecha
    val hora: Long = 0L,  // Timestamp para la hora
    val multimediaUris: List<String> = listOf()
) {
    fun isEntryValid(): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }
}
fun NoteDetails.toNote(): Note = Note(
    id = id,
    title = title,
    content = content,
    fecha = fecha,
    hora = hora,
    multimediaUris = multimediaUris
)

fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
    title = title,
    content = content,
    fecha = fecha,
    hora = hora,
    multimediaUris = multimediaUris
)
