
package com.example.inventory.ui.navigation

import com.example.inventory.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object NoteDetailsDestination : NavigationDestination {
    override val route = "note_details/{noteId}"
    override val titleRes: Int = R.string.note_detail_title

    const val noteIdArg = "noteId"

    fun createRoute(noteId: Int): String = "note_details/$noteId"
}
