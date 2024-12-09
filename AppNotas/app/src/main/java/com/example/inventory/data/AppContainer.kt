package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val notesRepository: NotesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineNotesRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val notesRepository: NotesRepository by lazy {
        OfflineNotesRepository(InventoryDatabase.getDatabase(context).noteDao())
    }
}
