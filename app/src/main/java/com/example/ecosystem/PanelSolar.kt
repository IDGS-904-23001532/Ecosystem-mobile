package com.example.ecosystem

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold

class PanelSolar : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                PantallaPanelSolar()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPanelSolar() {

    var encendido by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Panel Solar",
                        fontSize = 22.sp,
                        fontFamily = interBold,
                        color = colorPrimario
                    )
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            item {

                // Imagen
                Image(
                    painter = painterResource(id = R.drawable.panel_solar_info),
                    contentDescription = "Panel Solar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sección Estado
                Text(
                    text = "Estado",
                    fontSize = 20.sp,
                    fontFamily = interBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = encendido,
                        onCheckedChange = { encendido = it }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (encendido) "Encendido" else "Apagado",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sección Información
                Text(
                    text = "Información",
                    fontSize = 20.sp,
                    fontFamily = interBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fila superior: Cosecha del Día | Ángulos Servo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1 - Cosecha del Día
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(
                                text = "Cosecha del Día",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "12.8 Kwh",
                                fontSize = 20.sp,
                                fontFamily = interBold
                            )
                        }
                    }

                    // Card 2 - Ángulos Servo
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(
                                text = "Ángulos Servo",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "H: 92° | V: 45°",
                                fontSize = 16.sp,
                                fontFamily = interBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila inferior: Energía Total Generada | Estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 3 - Energía Total Generada (amarilla como en el mockup)
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFD600)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(
                                text = "Energía Total Generada",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "198 Kwh",
                                fontSize = 20.sp,
                                fontFamily = interBold,
                                color = Color.Black
                            )
                        }
                    }

                    // Card 4 - Estado del modo
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(
                                text = "Estado:",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Siguiendo Luz",
                                fontSize = 16.sp,
                                fontFamily = interBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))


                val context = LocalContext.current

                Button(
                    onClick = {

                        val intent = Intent(
                            context,
                            EstadoYMatenimiento::class.java
                        )

                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrimario
                    )
                ) {
                    Text("Ver mantenimiento")
                }
            }
            }
        }
    }
