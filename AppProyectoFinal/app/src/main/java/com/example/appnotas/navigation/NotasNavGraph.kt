package com.example.appnotas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.appnotas.home.HomeDestination
import com.example.appnotas.home.HomeScreen

@Composable
fun NotaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToNotaEntry = { navController.navigate(NotaEntryDestination.route) },
                navigateToNotaUpdate = {
                    navController.navigate("${NotaDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = NotaEntryDestination.route) {
            NotaEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = NotaDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(NotaDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            NotaDetailsScreen(
                navigateToEditItem = { navController.navigate("${NotaEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = NotaEditDestination.routeWithArgs,
            arguments = listOf(navArgument(NotaEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            NotaEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
