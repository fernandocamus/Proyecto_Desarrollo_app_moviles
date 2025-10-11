package com.example.readme_grupo11.ui.screens

import android.widget.Button
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import android.R.attr.onClick
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.tuapp.ui.utils.obtenerWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun HomeScreenWS(){
    val windowSize = obtenerWindowSizeClass()

    //definir columnas segun tamanio
    val columns = when (windowSize.widthSizeClass){
        WindowWidthSizeClass.Compact -> 1
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }
    HomeScreen(columns = columns)
}

@Composable
fun HomeScreen(columns:Int){
    var listaItems by remember { mutableStateOf(mutableListOf("Item 1", "Item 2", "Item 3")) }
    var contador by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Text(
            text = "Mi primera pantalla simple",
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            modifier = Modifier
        ){
            items(listaItems){ item ->
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(4.dp)
                ){
                    Text(
                        item,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Button(onClick = {
            /*Funcion onclick*/
            val sigIndice = listaItems.size + 1
            listaItems = (listaItems + "Elemento $sigIndice").toMutableList()

        },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Agregar Item")
        }
    }


}
