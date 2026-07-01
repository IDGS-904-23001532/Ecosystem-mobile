package com.example.ecosystem.Bateria

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ecosystem.notificacionDetalle

class BateriaWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Llamamos a la API usando tu RetrofitClient existente
            val response = RetrofitClient.apiService.getEstadoBateria()

            if (response.status == "success" && response.data != null) {
                val data = response.data

                // 2. Preparamos los textos de la notificación con los datos reales
                val titulo = "Batería: ${data.porcentajeBateria.toInt()}%"
                val mensajeCorto = "Consumo Neto: ${data.cargaNeta} Wh"
                val mensajeDetalle = """
                    Estado de tu Ecosistema:
                    • Energía Generada: ${data.energiaGenerada} Wh
                    • Energía Consumida: ${data.energiaConsumida} Wh
                    • Tiempo de carga estimado: ${data.tiempoCarga} H
                """.trimIndent()

                // 3. Llamamos a tu función de notificación
                notificacionDetalle(
                    context = applicationContext,
                    idNotificacion = 101, // Un ID único
                    idCanal = "canal_bateria",
                    title = titulo,
                    message = mensajeCorto,
                    messageDetail = mensajeDetalle
                )
            }

            // Indicamos que el trabajo terminó con éxito
            Result.success()

        } catch (e: Exception) {
            // Si hay error (ej. sin internet), le decimos que lo intente más tarde
            Result.retry()
        }
    }
}