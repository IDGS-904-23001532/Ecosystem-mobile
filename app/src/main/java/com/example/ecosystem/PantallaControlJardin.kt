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
import com.example.ecosystem.ui.theme.TextoOscuro
import com.example.ecosystem.ui.theme.colorNeutral
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
fun ControlJardin() {

    // Estados de control del jardin con MQTT
    // control-sistema: Habilita/Deshabilita el funcionamiento automático del ESP32
    var sistemaActivo by remember { mutableStateOf(false) }
// Campo: Valor_Humedad_Suelo (decimal) -> Actualizado dinámicamente por el topic "valor-humedad"
    var valorHumedadSuelo by remember { mutableStateOf("34.50 %") }

    // Campo: Bomba_Riego_Activa (tinyint) -> Muestra si el actuador/relevador está encendido (Lógica invertida LOW en ESP32)
    var bombaRiegoActiva by remember { mutableStateOf(false) }

    // Variable local para manejar el UMBRAL_HUMEDAD_MINIMA del ESP32 mediante la interfaz
    var umbralHumedadMinima by remember { mutableStateOf("30%") }
    val opcionesUmbral = listOf("20%", "30%", "40%", "50%")
    var expandedUmbral by remember { mutableStateOf(false) }

    // Campo: Duracion_Riego_Segundos (int) -> Almacena el tiempo del último ciclo completado
    var duracionRiegoSegundos by remember { mutableStateOf("12 segundos") }

    // Campo: Estado_Sincronizacion (tinyint) y Fecha_Captura_Local (timestamp)
    var estadoSincronizacion by remember { mutableStateOf("Sincronizado...") }
    var fechaCapturaLocal by remember { mutableStateOf("2026-07-01 11:42:05") }
    /*
         * INTERCONEXIÓN MQTT EN ANDROID:
         * Al recibir un mensaje en el topic "valor-humedad", actualizas `valorHumedadSuelo`.
         * Cuando el Switch de "Control Sistema" cambie, publicas "1" o "0" en "control-sistema".
         */

    Scaffold(
        topBar = {
            // Barra superior centrada con título
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

                // Imagen de Cabecera adaptada al formato de tu ecosistema
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
                Text(
                    text = "Estado del Sistema",
                    fontSize = 20.sp,
                    fontFamily = interBold,
                    color = colorPrimario
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = sistemaActivo,
                        onCheckedChange = { encendido ->
                            sistemaActivo = encendido
                            if (!encendido) {
                                bombaRiegoActiva = false
                                valorHumedadSuelo = "-- %"
                                duracionRiegoSegundos = "0 seg"
                            } else {
                                // Valores de recuperación simulados
                                valorHumedadSuelo = "34.50 %"
                                duracionRiegoSegundos = "12 seg"
                            }
                            // ACCIÓN MQTT: Publicar en topic "control-sistema" -> si es true "1", si es false "0"
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (sistemaActivo) "Estado Activo" else "Estado de Ahorro",
                        fontSize = 16.sp,
                        color = if (sistemaActivo) Color.Unspecified else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- SECCIÓN 2: CUADRÍCULA DE TARJETAS 2x2 (CAMPOS DB + MQTT) ---
                Text(
                    text = "Dispositivos y Parámetros IoT",
                    fontSize = 20.sp,
                    fontFamily = interBold,
                    color = colorPrimario
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fila Superior: Bomba de Riego (Bomba_Riego_Activa) | Humedad del Suelo (Valor_Humedad_Suelo)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tarjeta 1 - Actuador Relevador (Bomba_Riego_Activa)
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Bomba de Agua", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Switch(
                                checked = bombaRiegoActiva,
                                enabled = sistemaActivo,
                                onCheckedChange = { activa ->
                                    bombaRiegoActiva = activa
                                    // Nota: Modifica el comportamiento o envía un override al pin 2 (ACTUADOR)
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (bombaRiegoActiva) "Regando" else "Apagada",
                                fontSize = 15.sp,
                                fontFamily = interBold,
                                color = if (bombaRiegoActiva) colorPrimario else Color.Unspecified
                            )
                        }
                    }

                    // Tarjeta 2 - Sensor Higrómetro (Valor_Humedad_Suelo / Topic: valor-humedad)
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Humedad Suelo", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = valorHumedadSuelo,
                                fontSize = 22.sp,
                                fontFamily = interBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Valor humedad", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila Inferior: Selector de Umbral Mínimo | Registro de Telemetría (Tarjeta Amarilla)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tarjeta 3 - Configuración de Regla (UMBRAL_HUMEDAD_MINIMA)
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Umbral Mínimo", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Box {
                                OutlinedButton(
                                    onClick = { if (sistemaActivo) expandedUmbral = true },
                                    enabled = sistemaActivo,
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(umbralHumedadMinima, fontSize = 11.sp)
                                }
                                DropdownMenu(
                                    expanded = expandedUmbral,
                                    onDismissRequest = { expandedUmbral = false }
                                ) {
                                    opcionesUmbral.forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                umbralHumedadMinima = opcion
                                                expandedUmbral = false
                                                // Aquí se enviaría el nuevo umbral al ESP32 si expandes tu protocolo MQTT
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Auto-Riego", fontSize = 14.sp, fontFamily = interBold)
                        }
                    }

                    // Tarjeta 4 - Consolidación de Base de Datos (Focalizado en Amarillo 0xFFFFD600)
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD600))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Último Evento", fontSize = 11.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Duración: $duracionRiegoSegundos",
                                fontSize = 16.sp,
                                fontFamily = interBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = fechaCapturaLocal,
                                fontSize = 10.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = estadoSincronizacion,
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