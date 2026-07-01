package com.example.ecosystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.*
import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ButtonDefaults
import com.example.ecosystem.Bateria.RetrofitClient
import com.google.gson.annotations.SerializedName
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

// --- MODELOS DE DATOS ---

data class ConsumoSemanalResponse(
    val status: String,
    val data: List<ConsumoDia>?
)

data class ConsumoDia(
    @SerializedName("dia_nombre") val diaNombre: String,
    @SerializedName("dia_num") val diaNum: Int,
    @SerializedName("consumo_estimado_wh") val consumoEstimadoWh: Double,
    @SerializedName("promedio_corriente_a") val promedioCorrienteA: Double,
    @SerializedName("promedio_potencia_w") val promedioPotenciaW: Double,
    @SerializedName("promedio_voltaje_v") val promedioVoltajeV: Double,
    @SerializedName("total_lecturas") val totalLecturas: Int
)

// --- VIEWMODEL ---

class ConsumoSemanalViewModel : ViewModel() {
    var consumoData by mutableStateOf<List<ConsumoDia>>(emptyList())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchConsumoSemanal()
    }

    fun fetchConsumoSemanal() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.apiService.getConsumoBateriaSemana()
                if (response.status == "success") {
                    consumoData = response.data ?: emptyList()
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

/*
 * Actividad principal que muestra la pantalla de Estadísticas de consumo energético.
 */
class Inicio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo edge-to-edge (pantalla completa)
        setContent {
            EcosystemTheme { //Aplica el tema definido en la aplicación
                PantallaEstadisticas()
            }
        }
    }
}

/*
 * Composables principal que representa la pantalla de Estadísticas.
 * Contiene un gráfico de barras semanal y una tabla detallada de consumo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadisticas(viewModel: ConsumoSemanalViewModel = viewModel()) {
    val consumoData = viewModel.consumoData
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    Scaffold(
        topBar = {
            // Barra superior centrada con título
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Estadísticas",
                        fontSize = 22.sp,
                        fontFamily = interBold,
                        color = colorPrimario
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = colorPrimario)
            } else if (errorMessage != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(text = errorMessage, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchConsumoSemanal() }) {
                        Text("Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Sección: Consumo semanal
                    item {
                        Text(
                            text = "Consumo Semanal",
                            fontSize = 20.sp,
                            fontFamily = interBold,
                            color = colorPrimario,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (consumoData.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay datos de consumo disponibles", color = Color.Gray)
                            }
                        } else {
                            val maxConsumo = consumoData.maxOfOrNull { it.consumoEstimadoWh } ?: 1.0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                consumoData.forEach { day ->
                                    val porcentaje = if (maxConsumo > 0) ((day.consumoEstimadoWh / maxConsumo) * 100).toInt() else 0
                                    val diaAbrev = when (day.diaNombre) {
                                        "Lunes" -> "L"
                                        "Martes" -> "M"
                                        "Miércoles" -> "M"
                                        "Jueves" -> "J"
                                        "Viernes" -> "V"
                                        "Sábado" -> "S"
                                        "Domingo" -> "D"
                                        else -> day.diaNombre.take(1)
                                    }
                                    Barra(porcentaje, diaAbrev)
                                }
                            }
                        }
                    }

                    // Sección: Detalle por día
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                    item {
                        Text(
                            text = "Detalle por Día",
                            fontSize = 20.sp,
                            fontFamily = interBold,
                            color = colorPrimario,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Día", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
                                    Text("Consumo", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text("Potencia", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                                }

                                if (consumoData.isEmpty()) {
                                    Text(
                                        text = "No hay detalles disponibles",
                                        modifier = Modifier.padding(vertical = 16.dp),
                                        color = Color.Gray
                                    )
                                } else {
                                    consumoData.forEach { day ->
                                        val consumoTexto = "%.1f Wh".format(day.consumoEstimadoWh)
                                        val potenciaTexto = "%.1f W".format(day.promedioPotenciaW)
                                        FilaTabla(day.diaNombre, consumoTexto, potenciaTexto)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                val context = LocalContext.current

                                // Botón para navegar a la pantalla del Panel Solar
                                Button(
                                    onClick = {
                                        val intent = Intent(
                                            context,
                                            PanelSolar::class.java
                                        )
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorPrimario
                                    )
                                ) {
                                    Text("Ver Panel Solar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
* Composable que representa una barra individual del gráfico semanal.
*/
@Composable
fun Barra(porcentaje: Int, dia: String) {
    val alturaMaxima = 180.dp
    val alturaBarra = (porcentaje * alturaMaxima.value / 100).dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "$porcentaje%",
            fontSize = 12.sp,
            color = colorPrimario,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(35.dp)
                .height(alturaBarra)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    when {
                        porcentaje > 80 -> colorRojo
                        porcentaje > 60 -> colorNaranja
                        else -> colorPrimario
                    }
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dia,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
/**
 * Composable que representa una fila de la tabla de detalle.
 */
@Composable
fun FilaTabla(dispositivo: String, consumo: String, porcentaje: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dispositivo,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
        Text(
            text = consumo,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = porcentaje,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            color = colorPrimario,
            fontWeight = FontWeight.Bold
        )
    }
}
