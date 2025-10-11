package com.tuapp.ui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    val activity = LocalContext.current as Activity
    return calculateWindowSizeClass(activity)
}