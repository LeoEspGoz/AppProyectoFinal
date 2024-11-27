package com.example.appnotas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {
    // Operaciones CRUD para la tabla Nota
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNota(item: Nota)

    @Update
    suspend fun updateNota(item: Nota)

    @Delete
    suspend fun deleteNota(item: Nota)

    @Query("SELECT * FROM notas WHERE id = :id")
    fun getNota(id: Int): Flow<Nota>

    @Query("SELECT * FROM notas ORDER BY titulo ASC")
    fun getAllNotas(): Flow<List<Nota>>


}
