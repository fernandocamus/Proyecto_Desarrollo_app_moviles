package com.example.readme_grupo11.navigation

sealed class AppRoutes(val route: String) {
    data object Home : AppRoutes("home_page")
    data object Profile : AppRoutes("profile_page")
    data object Settings : AppRoutes("settings_page")

    companion object {

        fun getStartDestination(): String = Home.route

        fun getAllRoutes(): List<String> = listOf(
            Home.route,
            Profile.route,
            Settings.route
        )
    }
}