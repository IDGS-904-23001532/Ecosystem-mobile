package com.example.ecosystem.navigation

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecosystem.BateriaActivity
import com.example.ecosystem.ControlJardin
import com.example.ecosystem.EstadoYMatenimiento
import com.example.ecosystem.Login
import com.example.ecosystem.PantallaBateria
import com.example.ecosystem.PantallaControlJardin
import com.example.ecosystem.PantallaDispositivos
import com.example.ecosystem.PantallaEstadisticas
import com.example.ecosystem.PantallaMantenimiento
import com.example.ecosystem.PantallaPanelSolar
import com.example.ecosystem.R

/**
 * Composable principal de navegación de la aplicación.
 * Gestiona el cambio entre pantallas mediante una Bottom Navigation Bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    // Estado que guarda el índice de la pantalla actual (0 = Inicio, 1 = Panel, etc.)
    var pantalla by remember { mutableStateOf(0) }

    Scaffold(
        // ======================= TOP BAR =======================
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo de la aplicación
                        Image(
                            painter = painterResource(id = R.drawable.logo_ecosystem),
                            contentDescription = "Logo",
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Título de la aplicación
                        Text(
                            text = "EcoSystem",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },

        // ======================= BOTTOM NAVIGATION BAR =======================
        bottomBar = {
            NavigationBar {
                // Ítem 0 - Inicio
                NavigationBarItem(
                    selected = pantalla == 0,                    // Está seleccionado si pantalla = 0
                    onClick = { pantalla = 0 },                  // Cambia a la pantalla 0
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") }
                )

                // Ítem 1 - Panel Solar
                NavigationBarItem(
                    selected = pantalla == 1,
                    onClick = { pantalla = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SolarPower,
                            contentDescription = "Panel Solar"
                        )
                    },
                    label = { Text("Panel") }
                )

                // Ítem 2 - Jardín
                NavigationBarItem(
                    selected = pantalla == 2,
                    onClick = { pantalla = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Yard,
                            contentDescription = "Jardin"
                        )
                    },
                    label = { Text("Jardín") }
                )

                // Ítem 3 - Batería
                NavigationBarItem(
                    selected = pantalla == 3,
                    onClick = { pantalla = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = "Bateria"
                        )
                    },
                    label = { Text("Bateria") }
                )

                // Ítem 4 - Mantenimiento
                NavigationBarItem(
                    selected = pantalla == 4,
                    onClick = { pantalla = 4 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CardTravel,
                            contentDescription = "Matenimiento"
                        )
                    },
                    label = { Text("Matenimiento") }
                )

                // Ítem 5 - Dispositivos
                NavigationBarItem(
                    selected = pantalla == 5,
                    onClick = { pantalla = 5 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = "Dispositivos"
                        )
                    },
                    label = { Text("Dispositivos") }
                )
            }
        }
    ) { paddingValues ->     // paddingValues: espacio ocupado por topBar y bottomBar

        // Contenedor principal del contenido según la pantalla seleccionada
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (pantalla) {
                0 -> PantallaEstadisticas()      // Pantalla de Inicio / Estadísticas
                1 -> PantallaPanelSolar()        // Pantalla del Panel Solar
                2 -> ControlJardin()     // Pantalla de Control del Jardín
                3 -> PantallaBateria()           // Pantalla de Batería
                4 -> PantallaMantenimiento()     // Pantalla de Mantenimiento
                5 -> PantallaDispositivos()      // Pantalla de Dispositivos
            }
        }
    }
}