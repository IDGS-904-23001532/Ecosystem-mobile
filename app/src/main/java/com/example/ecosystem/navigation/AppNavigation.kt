package com.example.ecosystem.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecosystem.Bateria.PantallaBateria
import com.example.ecosystem.PantallaDispositivos
import com.example.ecosystem.PantallaEstadisticas
import com.example.ecosystem.PantallaPanelSolar
import com.example.ecosystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStack =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        backStack.value?.destination?.route

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(R.drawable.logo_ecosystem),
                            contentDescription = "Logo",
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "EcoSystem",
                            fontWeight = FontWeight.Bold
                        )

                    }

                }

            )

        },

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = currentRoute == "inicio",
                    onClick = {
                        navController.navigate("inicio") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Home, null)
                    },
                    label = {
                        Text("Inicio")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "panel",
                    onClick = {
                        navController.navigate("panel") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.SolarPower, null)
                    },
                    label = {
                        Text("Panel")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "bateria",
                    onClick = {
                        navController.navigate("bateria") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.BatteryChargingFull, null)
                    },
                    label = {
                        Text("Batería")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "dispositivos",
                    onClick = {
                        navController.navigate("dispositivos") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Devices, null)
                    },
                    label = {
                        Text("Dispositivos")
                    }
                )

            }

        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(padding)
        ) {

            composable("inicio") {
                PantallaEstadisticas()
            }

            composable("panel") {
                PantallaPanelSolar()
            }

            composable("bateria") {
                PantallaBateria()
            }

            composable("dispositivos") {
                PantallaDispositivos()
            }

        }

    }

}