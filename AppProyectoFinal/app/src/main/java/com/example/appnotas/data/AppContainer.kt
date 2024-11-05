package com.example.appnotas.data

import android.content.Context

interface AppContainer {
    val notasRepository : NotasRepository
}
class AppDataContainer(private val context: Context): AppContainer{

    override val notasRepository : NotasRepository by lazy {
        offlineItemsRepository(NotasDataBase.getDatabase(context).notaDao())
    }
}
