package com.example.ecosystem.Jardin

object MqttConfig {
    // Parámetros de conexión al servidor de HiveMQ Cloud
    const val SERVER_URI = "ssl://9e8563ef448f4ebfb38f8ca8b7c7ab86.s1.eu.hivemq.cloud:8883"
    const val USERNAME = "Christoph"
    const val PASSWORD = "Lagatita25"

    // Generación dinámica del identificador del cliente de Android
    val CLIENT_ID: String
        get() = "Android_EcoSystem_${System.currentTimeMillis()}"

    // Canales (Topics) MQTT centralizados
    object Topics {
        const val HUMEDAD = "valor-humedad"
        const val CONTROL_SISTEMA = "control-sistema"
        const val ESTADO_BOMBA = "estado-bomba"
    }
}