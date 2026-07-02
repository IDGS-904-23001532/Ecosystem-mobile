package com.example.ecosystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets.UTF_8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaControlCasa() {

    // ==========================================
    // ESTADOS DE LA INTERFAZ (ACTUADORES)
    // ==========================================
    val sistemaEncendido = remember { mutableStateOf(false) }

    // Accesos (2 Servomotores)
    val puertaPrincipal = remember { mutableStateOf(false) }
    val puertaGaraje = remember { mutableStateOf(false) }

    // Clima
    val estadoVentilador = remember { mutableStateOf(false) } // Bloqueado temporalmente

    // Iluminación (7 Habitaciones)
    // Usamos una lista de pares (Nombre -> Estado) para generar los switches dinámicamente
    val focos = listOf(
        "Sala" to remember { mutableStateOf(false) },
        "Cocina" to remember { mutableStateOf(false) },
        "Lavanderia" to remember { mutableStateOf(false) },
        "Baño" to remember { mutableStateOf(false) },
        "Habitación 1" to remember { mutableStateOf(false) },
        "Habitación 2" to remember { mutableStateOf(false) },
        "Cochera" to remember { mutableStateOf(false) }
    )

    //
    // ESTADOS DE TELEMETRÍA (MQTT - Sensores)
    //
    val tempCasa = remember { mutableStateOf("--") }
    val distanciaCochera = remember { mutableIntStateOf(100) }

    // Seguridad: 4 Sensores PIR
    val pirFrontal = remember { mutableStateOf(false) }
    val pirTrasero = remember { mutableStateOf(false) }
    val pirIzquierdo = remember { mutableStateOf(false) }
    val pirDerecho = remember { mutableStateOf(false) }

    val conexionEstatus = remember { mutableStateOf("Conectando...") }

    // Lógica de alerta ultrasónico (<= 15cm)
    val alertaCochera = distanciaCochera.intValue in 1..15

    //
    // CLIENTE MQTT
    //
    var client by remember { mutableStateOf<Mqtt5AsyncClient?>(null) }

    val host = "2c475eb27a4845ad8904f6e355ee7f6d.s1.eu.hivemq.cloud"
    val port = 8883
    val username = "kevax"
    val password = "Minombre123"

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val mqttClient = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(port)
                .sslWithDefaultConfig()
                .buildAsync()

            mqttClient.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send()
                .whenComplete { ack, ex ->
                    if (ex == null) {
                        conexionEstatus.value = "Conectado"
                        client = mqttClient

                        mqttClient.subscribeWith().topicFilter("ecosystem/telemetria").send()

                        mqttClient.publishes(MqttGlobalPublishFilter.ALL) { publish ->
                            if (publish.topic.toString() == "ecosystem/telemetria") {
                                val payloadString = String(publish.payloadAsBytes, UTF_8)
                                try {
                                    val json = JSONObject(payloadString)
                                    tempCasa.value = json.optString("temperatura", "--")
                                    distanciaCochera.intValue = json.optInt("distancia_cm", 100)

                                    // Lectura de los 4 PIR
                                    pirFrontal.value = json.optBoolean("pir_frontal", false)
                                    pirTrasero.value = json.optBoolean("pir_trasero", false)
                                    pirIzquierdo.value = json.optBoolean("pir_izquierdo", false)
                                    pirDerecho.value = json.optBoolean("pir_derecho", false)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    } else {
                        conexionEstatus.value = "Error de red"
                    }
                }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5), // Fondo ligeramente gris para resaltar las tarjetas blancas
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Control del Hogar", fontSize = 22.sp, fontFamily = interBold, color = colorPrimario) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.ic_house_backgroud),
                    contentDescription = "Fondo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )

                Text(
                    text = "Estado: ${conexionEstatus.value}",
                    fontSize = 12.sp,
                    color = if (conexionEstatus.value == "Conectado") Color(0xFF2E7D32) else Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // ==========================================
                // CONTROL MAESTRO
                // ==========================================
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sistema Maestro", fontSize = 18.sp, fontFamily = interBold)
                        Switch(
                            checked = sistemaEncendido.value,
                            onCheckedChange = { encendido ->
                                sistemaEncendido.value = encendido
                                if (!encendido) {
                                    // Apagar todo
                                    puertaPrincipal.value = false
                                    puertaGaraje.value = false
                                    focos.forEach { it.second.value = false }
                                    client?.publishWith()?.topic("ecosystem/comandos")?.payload(UTF_8.encode("SISTEMA_APAGADO"))?.send()
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //
                // ENTORNO Y ALERTA DE COCHERA
                //
                Text(text = "Monitoreo", fontSize = 18.sp, fontFamily = interBold)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Temperatura
                    Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Temperatura", fontSize = 13.sp, color = Color.Gray)
                            Text("${tempCasa.value} °C", fontSize = 24.sp, fontFamily = interBold)
                        }
                    }

                    // Alerta Ultrasónico
                    Card(
                        modifier = Modifier.weight(1.5f),
                        colors = CardDefaults.cardColors(containerColor = if (alertaCochera) Color(0xFFD32F2F) else Color(0xFFE8F5E9)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Distancia Cochera", fontSize = 13.sp, color = if (alertaCochera) Color.White else Color.DarkGray)
                            Text(
                                text = if (alertaCochera) "¡ALERTA! ${distanciaCochera.intValue} cm" else "${distanciaCochera.intValue} cm (Seguro)",
                                fontSize = 18.sp, fontFamily = interBold,
                                color = if (alertaCochera) Color.White else Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //
                // SEGURIDAD PERIMETRAL (4 PIR)
                //
                Text(text = "Seguridad Perimetral", fontSize = 18.sp, fontFamily = interBold)
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SensorPIRItem("Frontal", pirFrontal.value)
                            SensorPIRItem("Trasera", pirTrasero.value)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SensorPIRItem("Lateral Izq.", pirIzquierdo.value)
                            SensorPIRItem("Lateral Der.", pirDerecho.value)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //
                // ACCESOS (SERVOS)
                //
                Text(text = "Accesos", fontSize = 18.sp, fontFamily = interBold)
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FilaInterruptor("Puerta Principal", puertaPrincipal, sistemaEncendido.value) { estado ->
                            val cmd = if (estado) "PUERTA_PRINCIPAL_ABRIR" else "PUERTA_PRINCIPAL_CERRAR"
                            client?.publishWith()?.topic("ecosystem/comandos")?.payload(UTF_8.encode(cmd))?.send()
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
                        FilaInterruptor("Puerta Garaje", puertaGaraje, sistemaEncendido.value) { estado ->
                            val cmd = if (estado) "PUERTA_GARAJE_ABRIR" else "PUERTA_GARAJE_CERRAR"
                            client?.publishWith()?.topic("ecosystem/comandos")?.payload(UTF_8.encode(cmd))?.send()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //
                // ILUMINACIÓN (7 ZONAS)
                //
                Text(text = "Iluminación Inteligente", fontSize = 18.sp, fontFamily = interBold)
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        focos.forEachIndexed { index, foco ->
                            FilaInterruptor(foco.first, foco.second, sistemaEncendido.value) { estado ->
                                // Convierte "Habitación 1" a "FOCO_HABITACION_1_ON"
                                val nombreFormateado = foco.first.uppercase().replace(" ", "_").replace("Ñ", "N")
                                val cmd = if (estado) "FOCO_${nombreFormateado}_ON" else "FOCO_${nombreFormateado}_OFF"
                                client?.publishWith()?.topic("ecosystem/comandos")?.payload(UTF_8.encode(cmd))?.send()
                            }
                            if (index < focos.size - 1) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFF0F0F0))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //
                // CLIMA
                //
                Text(text = "Climatización", fontSize = 18.sp, fontFamily = interBold)
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Ventilador", fontSize = 16.sp, color = Color.LightGray)
                            Text("(Módulo de potencia requerido)", fontSize = 12.sp, color = Color.Red)
                        }
                        Switch(checked = estadoVentilador.value, enabled = false, onCheckedChange = { })
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

// Componente reutilizable para los sensores PIR
@Composable
fun SensorPIRItem(zona: String, detectado: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(if (detectado) Color.Red else Color.LightGray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = zona, fontSize = 14.sp, color = if (detectado) Color.Black else Color.Gray, fontWeight = if (detectado) FontWeight.Bold else FontWeight.Normal)
    }
}

// Componente reutilizable para las filas de Switches
@Composable
fun FilaInterruptor(nombre: String, estado: MutableState<Boolean>, habilitado: Boolean, onComando: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(nombre, fontSize = 16.sp, color = if (habilitado) Color.Black else Color.Gray)
        Switch(
            checked = estado.value,
            enabled = habilitado,
            onCheckedChange = { nuevoEstado ->
                estado.value = nuevoEstado
                onComando(nuevoEstado)
            }
        )
    }
}