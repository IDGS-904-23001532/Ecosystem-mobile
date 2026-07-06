package com.example.ecosystem.Bateria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoTarjetaInfo
import com.example.ecosystem.ui.theme.FondoTituloVerde
import com.example.ecosystem.ui.theme.GrisTextoSecundario
import com.example.ecosystem.ui.theme.GrisTrackBateria
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- 1. MODELOS DE DATOS Y RETROFIT ---

data class BateriaResponse(
    val status: String,
    val data: BateriaData?
)

data class BateriaData(
    @SerializedName("id_dispositivo") val idDispositivo: Int?,
    @SerializedName("voltaje_v") val voltajeV: Double?,
    @SerializedName("corriente_a") val corrienteA: Double?,
    @SerializedName("potencia_w") val potenciaW: Double?,
    @SerializedName("porcentaje_bateria") val porcentajeBateria: Double?,
    @SerializedName("fecha_registro") val fechaRegistro: String?,
    @SerializedName("tipo_estimacion") val tipoEstimacion: String?,
    @SerializedName("tiempo_estimado_horas") val tiempoEstimadoHoras: Double?
)

data class LecturaResponse(
    val status: String,
    val data: LecturaData?
)

data class LecturaData(
    @SerializedName("id_lectura") val idLectura: Int?,
    @SerializedName("id_dispositivo") val idDispositivo: Int?,
    @SerializedName("id_modo") val idModo: Int?,
    @SerializedName("voltaje_v") val voltajeV: Double?,
    @SerializedName("corriente_a") val corrienteA: Double?,
    @SerializedName("energia_generada_wh") val energiaGeneradaWh: Double?,
    @SerializedName("angulo_panel") val anguloPanel: String?,
    @SerializedName("alerta_generada") val alertaGenerada: Int?,
    @SerializedName("tipo_alerta") val tipoAlerta: String?,
    @SerializedName("fecha_registro") val fechaRegistro: String?
)

interface ApiService {
    // Reemplaza "vendingbox.online" o tu IP local de Flask (ej. "http://192.168.1.X:9000/")
    @GET("api/estado_bateria")
    suspend fun getEstadoBateria(): BateriaResponse

    @GET("api/consumo_bateria_semana")
    suspend fun getConsumoBateriaSemana(): com.example.ecosystem.ConsumoSemanalResponse

    @GET("api/guardar_lectura")
    suspend fun getUltimaLectura(): LecturaResponse
}

object RetrofitClient {
    // Si pruebas en el emulador apuntando a tu compu local, usa "http://10.0.2.2:9000/"
    // Si ya está en tu servidor, pon la URL base real.
    private const val BASE_URL = "https://ecosystem-python-production.up.railway.app//"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// --- 2. VIEWMODEL PARA MANEJAR EL ESTADO ---

class BateriaViewModel : ViewModel() {
    var bateriaData by mutableStateOf<BateriaData?>(null)
        private set
    var isLoading by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchDatosBateria()
    }

    fun fetchDatosBateria() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.apiService.getEstadoBateria()
                if (response.status == "success") {
                    bateriaData = response.data
                } else {
                    errorMessage = "Error en la respuesta del servidor"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

// --- 3. UI ACTUALIZADA ---

class BateriaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                PantallaBateria()
            }
        }
    }
}

@Composable
fun PantallaBateria(viewModel: BateriaViewModel = viewModel()) {
    val data = viewModel.bateriaData
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(
                                color = FondoTituloVerde,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Batería",
                            fontSize = 24.sp,
                            fontFamily = interBold,
                            color = Color.Black
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colorPrimario)
                        }
                    } else if (errorMessage != null) {
                        Box(modifier = Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
                            Text(text = errorMessage, color = Color.Red, textAlign = TextAlign.Center)
                            Button(onClick = { viewModel.fetchDatosBateria() }, modifier = Modifier.padding(top = 16.dp)) {
                                Text("Reintentar")
                            }
                        }
                    } else if (data != null) {
                        // El porcentaje se calcula dividiendo el voltaje actual entre 12.0 V y multiplicando por 100
                        val apiVoltaje = data.voltajeV ?: 0.0
                        val porcentajeBateria = ((apiVoltaje / 12.0) * 100.0).toFloat().coerceIn(0f, 100f)
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularBatteryIndicator(percentage = porcentajeBateria / 100f)
                        }

                        data.fechaRegistro?.let { fecha ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Último registro: $fecha",
                                fontSize = 12.sp,
                                color = GrisTextoSecundario,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = "Información",
                            fontSize = 22.sp,
                            fontFamily = interBold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Mostramos el voltaje real directo de la API
                            val voltaje = data.voltajeV?.let { "$it V" } ?: "N/A"
                            val corriente = data.corrienteA?.let { "$it A" } ?: "N/A"

                            InfoCard(
                                label = "Voltaje",
                                value = voltaje,
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                label = "Corriente",
                                value = corriente,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val potencia = data.potenciaW?.let { "$it W" } ?: "N/A"
                            val tiempoFormateado = data.tiempoEstimadoHoras?.let { "$it H" } ?: "N/A"
                            val tipoEstimacion = data.tipoEstimacion ?: "Tiempo estimado"

                            InfoCard(
                                label = "Potencia",
                                value = potencia,
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                label = tipoEstimacion,
                                value = tiempoFormateado,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun CircularBatteryIndicator(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    val progressAngle = percentage * 360f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(220.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx()

            drawCircle(
                color = GrisTrackBateria,
                radius = size.minDimension / 2 - strokeWidth / 2,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = colorPrimario,
                startAngle = -90f,
                sweepAngle = progressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${(percentage * 100).toInt()}%",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = colorPrimario
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Calculamos el gastado para la etiqueta de abajo
            val gastado = 100 - (percentage * 100).toInt()
            Text(
                text = "$gastado% spent",
                fontSize = 12.sp,
                color = GrisTextoSecundario,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isMediumValue: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = FondoTarjetaInfo
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                fontSize = if (isMediumValue) 14.sp else 26.sp,
                fontFamily = interBold,
                color = Color.Black
            )
        }
    }
}