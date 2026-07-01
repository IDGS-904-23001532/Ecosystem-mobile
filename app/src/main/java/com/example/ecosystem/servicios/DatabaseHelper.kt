package com.example.ecosystem.servicios

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object DatabaseHelper {

    private const val BASE_URL = "https://vendingbox.online/debug_db/"

    // --- FUNCIÓN PARA LOGIN ---
    suspend fun loginUser(usuario: String, pass: String): JSONObject {
        return performPostRequest("login.php", mapOf(
            "usuario" to usuario,
            "password" to pass
        ))
    }

    // --- FUNCIÓN PARA REGISTRO ---
    suspend fun registerUser(nombre: String, email: String, usuario: String, pass: String): JSONObject {
        return performPostRequest("registro.php", mapOf(
            "nombre" to nombre,
            "email" to email,
            "usuario" to usuario,
            "password" to pass
        ))
    }

    // --- MOTOR DE CONEXIÓN (POST) ---
    private suspend fun performPostRequest(endpoint: String, params: Map<String, String>): JSONObject {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(BASE_URL + endpoint)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true

                // Construir los parámetros para el PHP (form-urlencoded)
                val postData = params.map {
                    "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value, "UTF-8")}"
                }.joinToString("&")

                // Enviar datos
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(postData)
                writer.flush()

                // Leer respuesta del servidor
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                JSONObject(response) // Convertimos el texto del PHP a un objeto JSON usable en Kotlin

            } catch (e: Exception) {
                e.printStackTrace()
                JSONObject().apply {
                    put("res", "error")
                    put("msg", "Error de conexión: ${e.message}")
                }
            }
        }
    }
}