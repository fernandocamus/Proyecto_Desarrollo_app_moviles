package com.example.readme_grupo11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.readme_grupo11.navigation.AppNavigation

// MainActivity de la aplicacion
class MainActivity : ComponentActivity() {
    // Configuracion de la UI con Jetpack Compose y el sistema de navegacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar el modo pantalla completa (Edge to Edge)
        enableEdgeToEdge()

        // Contenido presente
        setContent {

            // Theme utilizado
            MaterialTheme {

                // Contenedor de color de fondo
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //Inicializacion de la navegacion de la aplicacion
                    AppNavigation()
                }
            }
        }
    }
}