package com.example.readme_grupo11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.readme_grupo11.ui.theme.README_Grupo11Theme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.readme_grupo11.ui.screens.Formulario
import com.example.readme_grupo11.ui.screens.Resultante

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            README_Grupo11Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //seba@gmail.com
                    MyApp()

                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "formulario") {
        composable("formulario") { Formulario(navController) }
        composable("resultado/{nombre}/{email}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre")
            val email = backStackEntry.arguments?.getString("email")
            Resultante(nombre, email)
        }
    }
}
