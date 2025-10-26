package com.example.readme_grupo11.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.readme_grupo11.ui.screens.HomeScreen
import com.example.readme_grupo11.ui.screens.RegistroScreen
import com.example.readme_grupo11.ui.screens.LoginScreen
import com.example.readme_grupo11.ui.screens.RecuperacionScreen

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
        // Pantalla de inicio
        composable(route = AppRoutes.Login.route) {
            LoginScreen(
                onNavigateToRegistro = {
                    navController.navigate(AppRoutes.Registro.route)
                },
                onNavigateToRecuperacion = {
                    navController.navigate(AppRoutes.Recuperacion.route)
                },
                onNavigateToHome = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

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

        // Pantalla de recuperacion de contraseña
        composable(route = AppRoutes.Recuperacion.route) {
            RecuperacionScreen(
                // Volver hacia el login
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}