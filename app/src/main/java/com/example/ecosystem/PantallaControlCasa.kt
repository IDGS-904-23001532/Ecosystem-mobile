package com.example.ecosystem

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
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaControlCasa() {

    // Estado general del sistema - controla todo lo demás
    val sistemaEncendido = remember { mutableStateOf(false) }

    val seleccionFocos = remember { mutableStateOf("Apagado") }
    val opcionesFocos = listOf("Apagado", "Encendido")
    val expandedFocos = remember { mutableStateOf(false) }

    val seleccionGaraje = remember { mutableStateOf("Cerrado") }
    val expandedGaraje = remember { mutableStateOf(false) }

    val seleccionVentilador = remember { mutableStateOf("Apagado") }
    val opcionesVentilador = listOf("Apagado", "Velocidad 1", "Velocidad 2", "Velocidad 3")
    val expandedVentilador = remember { mutableStateOf(false) }

    // Cuenta dispositivos activos solo si el sistema está encendido
    val dispositivosActivos = if (sistemaEncendido.value) {
        listOf(
            seleccionFocos.value != "Apagado",
            seleccionGaraje.value != "Cerrado",
            seleccionVentilador.value != "Apagado"
        ).count { it }
    } else {
        0
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Control del Hogar",
                        fontSize = 22.sp,
                        fontFamily = interBold,
                        color = colorPrimario
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Image(
                    painter = painterResource(id = R.drawable.ic_house_backgroud),
                    contentDescription = "Control del Hogar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Estado",
                    fontSize = 20.sp,
                    fontFamily = interBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Checkbox de estado general - habilita/deshabilita todo el sistema
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = sistemaEncendido.value,
                        onCheckedChange = { encendido ->
                            sistemaEncendido.value = encendido
                            // Si se apaga el sistema, regresa todos los dispositivos a su estado inicial
                            // TODO: aquí irá la publicación MQTT para apagar todos los dispositivos
                            if (!encendido) {
                                seleccionFocos.value = "Apagado"
                                seleccionGaraje.value = "Cerrado"
                                seleccionVentilador.value = "Apagado"
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (sistemaEncendido.value) "Encendido" else "Apagado",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Dispositivos",
                    fontSize = 20.sp,
                    fontFamily = interBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Focos - deshabilitado si el sistema está apagado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Focos",
                                fontSize = 14.sp,
                                color = if (sistemaEncendido.value) Color.Unspecified else Color.Gray
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = seleccionFocos.value,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Switch(
                                    checked = seleccionFocos.value == "Encendido",
                                    enabled = sistemaEncendido.value,
                                    onCheckedChange = { encendido ->
                                        seleccionFocos.value = if (encendido) "Encendido" else "Apagado"
                                        // TODO: publicar a MQTT topic casa/focos con valor encendido/apagado
                                    }
                                )
                            }
                        }

                        // Puerta del Garaje - deshabilitado si el sistema está apagado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Puerta del Garaje",
                                fontSize = 14.sp,
                                color = if (sistemaEncendido.value) Color.Unspecified else Color.Gray
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = seleccionGaraje.value,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Switch(
                                    checked = seleccionGaraje.value == "Abierto",
                                    enabled = sistemaEncendido.value,
                                    onCheckedChange = { abierto ->
                                        seleccionGaraje.value = if (abierto) "Abierto" else "Cerrado"
                                        // TODO: publicar a MQTT topic casa/garaje con valor abierto/cerrado
                                    }
                                )
                            }
                        }

                        // Ventilador - deshabilitado si el sistema está apagado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ventilador",
                                fontSize = 14.sp,
                                color = if (sistemaEncendido.value) Color.Unspecified else Color.Gray
                            )
                            Box {
                                OutlinedButton(
                                    onClick = {
                                        if (sistemaEncendido.value) expandedVentilador.value = true
                                    },
                                    enabled = sistemaEncendido.value
                                ) {
                                    Text(seleccionVentilador.value, fontSize = 13.sp)
                                }
                                DropdownMenu(
                                    expanded = expandedVentilador.value,
                                    onDismissRequest = { expandedVentilador.value = false }
                                ) {
                                    opcionesVentilador.forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                seleccionVentilador.value = opcion
                                                expandedVentilador.value = false
                                                // TODO: publicar a MQTT topic casa/ventilador con la velocidad seleccionada
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Información",
                    fontSize = 20.sp,
                    fontFamily = interBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Consumo actual",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            // TODO: reemplazar con dato real de MQTT
                            Text(
                                text = "-- kWh",
                                fontSize = 20.sp,
                                fontFamily = interBold
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Mayor consumo",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            // TODO: reemplazar con dato real de MQTT
                            Text(
                                text = "--",
                                fontSize = 16.sp,
                                fontFamily = interBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFD600)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Dispositivos activos",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$dispositivosActivos / 3",
                                fontSize = 20.sp,
                                fontFamily = interBold,
                                color = Color.Black
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Estado red",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            // TODO: reemplazar con estado real de conexión MQTT
                            Text(
                                text = "Esperando...",
                                fontSize = 16.sp,
                                fontFamily = interBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}