package com.example.readme_grupo11.navigation

// Clase para definir todas las rutas de navegacion que puede tener la aplicacion
sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Registro : AppRoutes("registro")
    object Recuperacion : AppRoutes("recuperacion")
    object Home : AppRoutes("home")
    object Camera : AppRoutes("camera")
}
