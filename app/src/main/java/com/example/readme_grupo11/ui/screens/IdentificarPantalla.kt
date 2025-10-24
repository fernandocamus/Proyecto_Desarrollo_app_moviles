package com.example.readme_grupo11.ui.screens

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.readme_grupo11.ui.utils.obtenerWindowSizeClass

@Composable
fun IdentificarPantalla() {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Compact -> HomeScreenMediana()
        WindowWidthSizeClass.Compact -> HomeScreenExpandida()
        else -> HomeScreenCompacta()
    }
}