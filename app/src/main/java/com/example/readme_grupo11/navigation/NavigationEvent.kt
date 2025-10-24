package com.example.readme_grupo11.navigation

sealed interface NavigationEvent {
    data class NavigateTo(val route: String) : NavigationEvent

    data object NavigateBack : NavigationEvent

    data class NavigateAndClear(val route: String) : NavigationEvent

    data class NavigateAndReplace(val route: String) : NavigationEvent
}