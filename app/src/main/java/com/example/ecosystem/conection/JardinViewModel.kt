package com.example.ecosystem

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ecosystem.Jardin.MqttConfig
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ecosystem.repository.JardinRepository

class JardinViewModel : ViewModel() {
    // Estados observables por Jetpack Compose
    var sistemaActivo by mutableStateOf(false)
    var valorHumedadSuelo by mutableStateOf("-- %")
    var bombaRiegoActiva by mutableStateOf(false)
    var umbralHumedadMinima by mutableStateOf("30%")
    var duracionRiegoSegundos by mutableStateOf("12 segundos")
    var estadoSincronizacion by mutableStateOf("Desconectado...")
    var fechaCapturaLocal by mutableStateOf("Sin registros")

    private var mqttClient: MqttClient? = null

    // Repository preparado para consumir la API cuando esté disponible.
    private val repository = JardinRepository()

    init {
        conectarBroker()
    }

    /*
    Lógica de conexión y eventos
     */
    private fun conectarBroker() {
        try {
            // Se inyectan las variables desde MqttConfig
            mqttClient = MqttClient(MqttConfig.SERVER_URI, MqttConfig.CLIENT_ID, MemoryPersistence())

            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isAutomaticReconnect = true
                isCleanSession = true
            }

            mqttClient?.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    estadoSincronizacion = "Desconectado (Reintentando)..."
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val payload = message?.toString() ?: return

                    // Manejo de eventos usando los tópicos centralizados
                    when (topic) {
                        MqttConfig.Topics.HUMEDAD -> {
                            valorHumedadSuelo = "$payload %"
                            estadoSincronizacion = "Sincronizado..."
                            actualizarFechaLocal()

                            val humedadNum = payload.toIntOrNull() ?: 0
                            val umbralNum = umbralHumedadMinima.replace("%", "").toIntOrNull() ?: 30
                            if (sistemaActivo) {
                                bombaRiegoActiva = humedadNum < umbralNum
                            }
                        }
                        MqttConfig.Topics.CONTROL_SISTEMA -> {
                            sistemaActivo = payload == "1"
                        }
                        MqttConfig.Topics.ESTADO_BOMBA -> {
                            bombaRiegoActiva = payload == "1"
                        }
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            mqttClient?.connect(options)

            // Suscripción automática utilizando las constantes separadas
            mqttClient?.subscribe(MqttConfig.Topics.HUMEDAD, 1)
            mqttClient?.subscribe(MqttConfig.Topics.CONTROL_SISTEMA, 1)
            mqttClient?.subscribe(MqttConfig.Topics.ESTADO_BOMBA, 1)

            estadoSincronizacion = "Conectado"
        } catch (e: Exception) {
            Log.e("MQTT_ERROR", "Error en conexión: ${e.message}")
            estadoSincronizacion = "Error de red"
        }
    }

    // ==========================================
    // ACCIONES DE LA INTERFAZ (MÉTODOS PÚBLICOS)
    // ==========================================
    fun alternarSistema(activar: Boolean) {
        val payload = if (activar) "1" else "0"
        publicarMensaje(MqttConfig.Topics.CONTROL_SISTEMA, payload)
        sistemaActivo = activar
        if (!activar) {
            bombaRiegoActiva = false
            valorHumedadSuelo = "-- %"
        }
    }

    fun publicarMensaje(topic: String, message: String) {
        try {
            if (mqttClient?.isConnected == true) {
                val mqttMessage = MqttMessage(message.toByteArray()).apply { qos = 1 }
                mqttClient?.publish(topic, mqttMessage)
            }
        } catch (e: Exception) {
            Log.e("MQTT_PUBLISH", "Fallo al enviar mensaje: ${e.message}")
        }
    }

    private fun actualizarFechaLocal() {
        val formatter = SimpleDateFormat("yyyy-MM-2026 HH:mm:ss", Locale.getDefault())
        fechaCapturaLocal = formatter.format(Date())
    }

    /**
     * Aquí se obtendrá la información proveniente de la API del proyecto.
     */
    fun actualizarDatosDesdeApi() {


    }
}