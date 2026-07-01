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

    // ESTADOS DE LA INTERFAZ (ACTUADORES)
    val sistemaEncendido = remember { mutableStateOf(false) }
    val estadoFoco = remember { mutableStateOf(false) }
    val estadoGaraje = remember { mutableStateOf(false) }
    val estadoVentilador = remember { mutableStateOf(false) }

    // ESTADOS DE TELEMETRÍA (MQTT - Sensores)
    val tempCasa = remember { mutableStateOf("--") }
    val presenciaPIR = remember { mutableStateOf(false) }
    val distanciaCochera = remember { mutableIntStateOf(100) }
    val conexionEstatus = remember { mutableStateOf("Conectando...") }

    // Lógica de alerta ultrasónico (<= 15cm)
    val alertaCochera = distanciaCochera.intValue in 1..15

    // CLIENTE MQTT
    var client by remember { mutableStateOf<Mqtt5AsyncClient?>(null) }

    val host = "96642097c1d94aed9589f163592aae4a.s1.eu.hivemq.cloud"
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

                        // Suscripción a la telemetría del ESP32
                        mqttClient.subscribeWith()
                            .topicFilter("ecosystem/telemetria")
                            .send()

                        // Escucha global de mensajes
                        mqttClient.publishes(MqttGlobalPublishFilter.ALL) { publish ->
                            val topic = publish.topic.toString()
                            if (topic == "ecosystem/telemetria") {
                                val payloadString = String(publish.payloadAsBytes, UTF_8)
                                try {
                                    // Parseo del JSON recibido desde Arduino
                                    val json = JSONObject(payloadString)
                                    tempCasa.value = json.optString("temperatura", "--")
                                    presenciaPIR.value = json.optBoolean("presencia", false)
                                    distanciaCochera.intValue = json.optInt("distancia_cm", 100)
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

                // Estatus de conexión HiveMQ
                Text(
                    text = "HiveMQ: ${conexionEstatus.value}",
                    fontSize = 12.sp,
                    color = if (conexionEstatus.value == "Conectado") Color(0xFF2E7D32) else Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Interruptor Principal", fontSize = 20.sp, fontFamily = interBold)
                Spacer(modifier = Modifier.height(8.dp))

                // Control Maestro
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = sistemaEncendido.value,
                        onCheckedChange = { encendido ->
                            sistemaEncendido.value = encendido
                            if (!encendido) {
                                estadoFoco.value = false
                                estadoGaraje.value = false

                                // Envía comando de apagado general al ESP32
                                client?.publishWith()
                                    ?.topic("ecosystem/comandos")
                                    ?.payload(UTF_8.encode("SISTEMA_APAGADO"))
                                    ?.send()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (sistemaEncendido.value) "Sistema Activo" else "Modo Reposo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Actuadores", fontSize = 20.sp, fontFamily = interBold)
                Spacer(modifier = Modifier.height(8.dp))

                // Tarjeta de Controles
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Foco LED
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Foco LED Principal", fontSize = 16.sp, color = if (sistemaEncendido.value) Color.Black else Color.Gray)
                            Switch(
                                checked = estadoFoco.value,
                                enabled = sistemaEncendido.value,
                                onCheckedChange = { encender ->
                                    estadoFoco.value = encender
                                    val comando = if (encender) "FOCO_ON" else "FOCO_OFF"
                                    client?.publishWith()
                                        ?.topic("ecosystem/comandos")
                                        ?.payload(UTF_8.encode(comando))
                                        ?.send()
                                }
                            )
                        }

                        // 2. Garaje
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Puerta de Garaje", fontSize = 16.sp, color = if (sistemaEncendido.value) Color.Black else Color.Gray)
                            Switch(
                                checked = estadoGaraje.value,
                                enabled = sistemaEncendido.value,
                                onCheckedChange = { abrir ->
                                    estadoGaraje.value = abrir
                                    val comando = if (abrir) "PUERTA_ABRIR" else "PUERTA_CERRAR"
                                    client?.publishWith()
                                        ?.topic("ecosystem/comandos")
                                        ?.payload(UTF_8.encode(comando))
                                        ?.send()
                                }
                            )
                        }

                        // 3. Ventilador (Bloqueado temporalmente)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Ventilador", fontSize = 16.sp, color = Color.LightGray)
                                Text("(Falta módulo relay)", fontSize = 12.sp, color = Color.Red)
                            }
                            Switch(
                                checked = estadoVentilador.value,
                                enabled = false,
                                onCheckedChange = { }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Sensores y Entorno", fontSize = 20.sp, fontFamily = interBold)
                Spacer(modifier = Modifier.height(8.dp))

                // Fila 1: Temperatura y PIR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(modifier = Modifier.weight(1f), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Temperatura", fontSize = 13.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${tempCasa.value} °C", fontSize = 22.sp, fontFamily = interBold)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (presenciaPIR.value) Color(0xFFFF5252) else Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                "Movimiento",
                                fontSize = 13.sp,
                                color = if (presenciaPIR.value) Color.White else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (presenciaPIR.value) "¡DETECTADO!" else "Sin presencia",
                                fontSize = 18.sp,
                                fontFamily = interBold,
                                color = if (presenciaPIR.value) Color.White else Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tarjeta de Alerta de Cochera (Ultrasónico)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (alertaCochera) Color(0xFFD32F2F) else Color(0xFFE8F5E9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Sensor de Cochera",
                                fontSize = 14.sp,
                                color = if (alertaCochera) Color.White else Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (alertaCochera) "¡ALERTA! Auto muy cerca" else "Distancia Segura",
                                fontSize = 18.sp,
                                fontFamily = interBold,
                                color = if (alertaCochera) Color.White else Color(0xFF2E7D32)
                            )
                        }
                        Text(
                            text = "${distanciaCochera.intValue} cm",
                            fontSize = 24.sp,
                            fontFamily = interBold,
                            color = if (alertaCochera) Color.White else Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}