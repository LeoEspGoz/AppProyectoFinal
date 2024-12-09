package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.item.NoteEditScreen
import com.example.inventory.ui.item.NoteEntryScreen
import com.example.inventory.ui.item.NoteDetailsScreen // Asegúrate de importar esta clase
import com.example.inventory.ui.notes.NotesScreen

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    startDestination: String = "notes"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("notes") {
            NotesScreen(
                navigateToNoteEntry = { navController.navigate("note_entry") },
                navigateToNoteDetail = { noteId ->
                    navController.navigate("note_details/$noteId")
                }
            )
        }

        composable(route = "note_entry") {
            NoteEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "note_details/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            NoteDetailsScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() },
                navigateToEditNote = { id -> navController.navigate("edit_note/$id") }
            )
        }

        composable(
            route = "edit_note/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            NoteEditScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
