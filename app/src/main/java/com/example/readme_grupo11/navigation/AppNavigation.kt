package com.example.readme_grupo11.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.readme_grupo11.ui.screens.HomeScreen
import com.example.readme_grupo11.ui.screens.RegistroScreen

// Funcion principal para la navegacion en la aplicacion
@Composable
fun AppNavigation() {

    // Creacion de controlador de navegacion
    val navController = rememberNavController()

    // Definicion de host de navegacion con la ruta inicial
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        // Pantalla de Registro
        composable(route = AppRoutes.Registro.route) {
            RegistroScreen(
                // Navegacion hacia atras
                onNavigateBack = {
                    navController.popBackStack()
                },
                // Navegacion hacia login
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Home (Inicio)
        composable(route = AppRoutes.Home.route) {
            HomeScreen(
                // Cerrar sesion y volver a login
                onNavigateToLogin = {
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}