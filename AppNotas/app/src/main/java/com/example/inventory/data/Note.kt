// Note.kt
package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,          // Título de la nota
    val content: String,        // Contenido de la nota
    val fecha: Long = 0L,       // Fecha en formato timestamp
    val hora: Long = 0L,         // Hora en formato timestamp
    val multimediaUris: List<String> // URIs de imágenes, audio, o videos
)

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): Flow<Note?>  // Aquí obtenemos la nota junto con las URIs multimedia

    @Query("SELECT * FROM notes ORDER BY title ASC")
    fun getAllNotes(): Flow<List<Note>>
}