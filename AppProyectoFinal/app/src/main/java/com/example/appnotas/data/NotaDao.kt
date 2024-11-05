package com.example.appnotas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Nota)

    @Update
    suspend fun update(item: Nota)

    @Delete
    suspend fun delete(item: Nota)

    @Query("SELECT * from notas WHERE id = :id")
    fun getItem(id: Int): Flow<Nota>

    @Query("SELECT * from notas ORDER BY titulo ASC")
    fun getAllItems(): Flow<List<Nota>>
}