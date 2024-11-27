package com.example.appnotas.data

import kotlinx.coroutines.flow.Flow

class OfflineNotasRepository(private val notaDao: NotaDao) : NotasRepository {
    override fun getAllNotasStream(): Flow<List<Nota>> {
        return notaDao.getAllNotas()
    }

    override fun getNotaStream(id: Int): Flow<Nota?> {
        return notaDao.getNota(id)
    }

    override suspend fun insertNota(nota: Nota)= notaDao.insertNota(nota)

    override suspend fun deleteNota(nota: Nota) = notaDao.deleteNota(nota)

    override suspend fun updateNota(nota: Nota) {
        return notaDao.updateNota(nota)
    }
}