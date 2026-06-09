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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SolarPower
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
import com.example.ecosystem.EstadoYMatenimiento
import com.example.ecosystem.Login
import com.example.ecosystem.PantallaEstadisticas
import com.example.ecosystem.PantallaMantenimiento
import com.example.ecosystem.PantallaPanelSolar
import com.example.ecosystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    var pantalla by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_ecosystem),
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
                    selected = pantalla == 0,
                    onClick = { pantalla = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") }
                )

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

                NavigationBarItem(
                    selected = pantalla == 2,
                    onClick = { pantalla = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = "Login"
                        )
                    },
                    label = { Text("Login") }
                )


                NavigationBarItem(
                    selected = pantalla == 3,
                    onClick = { pantalla = 3},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CardTravel,
                            contentDescription = "Matenimiento"
                        )
                    },
                    label =  {Text("Matenimiento")}
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (pantalla) {
                0 -> PantallaEstadisticas()

                1 -> PantallaPanelSolar()

                3 -> PantallaMantenimiento()




            }
        }
    }
}