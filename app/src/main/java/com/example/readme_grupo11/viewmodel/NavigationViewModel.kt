package com.example.navegacionapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.readme_grupo11.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()

    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    suspend fun emitNavigationEvent(event: NavigationEvent) {
        _navigationEvent.emit(event)
    }

    fun handleNavigation(event: NavigationEvent, navController: NavController) {
        when (event) {
            is NavigationEvent.NavigateTo -> {
                navController.navigate(event.route)
            }

            is NavigationEvent.NavigateBack -> {
                navController.popBackStack()
            }

            is NavigationEvent.NavigateAndClear -> {
                navController.navigate(event.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }

            is NavigationEvent.NavigateAndReplace -> {
                navController.navigate(event.route) {
                    popUpTo(navController.currentBackStackEntry?.destination?.id ?: 0) {
                        inclusive = true
                    }
                }
            }
        }
    }
}