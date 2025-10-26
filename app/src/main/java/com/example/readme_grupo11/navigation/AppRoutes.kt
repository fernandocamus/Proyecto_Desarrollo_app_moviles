package com.example.readme_grupo11.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Registro : AppRoutes("registro")
    object Recuperacion : AppRoutes("recuperacion")
    object Home : AppRoutes("home")
}
