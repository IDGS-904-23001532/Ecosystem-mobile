package com.example.ecosystem.Jardin

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecosystem.JardinViewModel
import com.example.ecosystem.R
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold

class PantallaControlJardin : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                ControlJardin()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlJardin(viewModel: JardinViewModel = viewModel()) {

    val opcionesUmbral = listOf("20%", "30%", "40%", "50%")
    var expandedUmbral by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Control del Jardín",
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
                Image(
                    painter = painterResource(id = R.drawable.bomba_riego),
                    contentDescription = "Monitoreo del Huerto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- SECCIÓN 1: ESTADO MAESTRO DEL ARDUINO ---
                Text(text = "Estado del Sistema", fontSize = 20.sp, fontFamily = interBold, color = colorPrimario)
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = viewModel.sistemaActivo,
                        onCheckedChange = { encendido ->
                            viewModel.alternarSistema(encendido)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (viewModel.sistemaActivo) "Estado Activo" else "Estado de Ahorro",
                        fontSize = 16.sp,
                        color = if (viewModel.sistemaActivo) Color.Unspecified else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- SECCIÓN 2: CUADRÍCULA DE TARJETAS IoT ---
                Text(text = "Dispositivos y Parámetros IoT", fontSize = 20.sp, fontFamily = interBold, color = colorPrimario)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tarjeta 1 - Bomba de Agua
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Bomba de Agua", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Switch(
                                checked = viewModel.bombaRiegoActiva,
                                enabled = viewModel.sistemaActivo,
                                onCheckedChange = { activa ->
                                    val payload = if (activa) "1" else "0"
                                    viewModel.publicarMensaje("estado-bomba", payload)
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (viewModel.bombaRiegoActiva) "Regando" else "Apagada",
                                fontSize = 15.sp,
                                fontFamily = interBold,
                                color = if (viewModel.bombaRiegoActiva) colorPrimario else Color.Unspecified
                            )
                        }
                    }

                    // Tarjeta 2 - Humedad del Suelo
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Humedad Suelo", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = viewModel.valorHumedadSuelo,
                                fontSize = 22.sp,
                                fontFamily = interBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Valor humedad", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tarjeta 3 - Umbral Mínimo
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Umbral Mínimo", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Box {
                                OutlinedButton(
                                    onClick = { if (viewModel.sistemaActivo) expandedUmbral = true },
                                    enabled = viewModel.sistemaActivo,
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(viewModel.umbralHumedadMinima, fontSize = 11.sp)
                                }
                                DropdownMenu(
                                    expanded = expandedUmbral,
                                    onDismissRequest = { expandedUmbral = false }
                                ) {
                                    opcionesUmbral.forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                viewModel.umbralHumedadMinima = opcion
                                                expandedUmbral = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Auto-Riego", fontSize = 14.sp, fontFamily = interBold)
                        }
                    }

                    // Tarjeta 4 - Consolidación Telemetría (Depurador DB)
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD600))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Último Evento", fontSize = 11.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Duración: ${viewModel.duracionRiegoSegundos}",
                                fontSize = 16.sp,
                                fontFamily = interBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = viewModel.fechaCapturaLocal,
                                fontSize = 10.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = viewModel.estadoSincronizacion,
                                fontSize = 11.sp,
                                color = Color.Black,
                                fontFamily = interBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}