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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.ecosystem.Bateria.RetrofitClient
import com.example.ecosystem.Bateria.LecturaData
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning

class PanelSolarViewModel : ViewModel() {
    var lecturaData by mutableStateOf<LecturaData?>(null)
        private set
    var isLoading by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchUltimaLectura()
    }

    fun fetchUltimaLectura() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.apiService.getUltimaLectura()
                if (response.status == "success") {
                    lecturaData = response.data
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

class PanelSolar : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                PantallaPanelSolar()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPanelSolar(viewModel: PanelSolarViewModel = viewModel()) {
    val lectura = viewModel.lecturaData
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    var encendido by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Panel Solar",
                        fontSize = 22.sp,
                        fontFamily = interBold,
                        color = colorPrimario
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchUltimaLectura() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = colorPrimario
                        )
                    }
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

                // Imagen
                Image(
                    painter = painterResource(id = R.drawable.panel_solar_info),
                    contentDescription = "Panel Solar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sección Estado
                Text(
                    text = "Estado",
                    fontSize = 20.sp,
                    fontFamily = interBold,
                    color = colorPrimario
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = encendido,
                        onCheckedChange = { encendido = it }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (encendido) "Encendido" else "Apagado",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sección Información
                Text(
                    text = "Información",
                    fontSize = 20.sp,
                    fontFamily = interBold,
                    color = colorPrimario
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colorPrimario)
                    }
                } else if (errorMessage != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { viewModel.fetchUltimaLectura() },
                            colors = ButtonDefaults.buttonColors(containerColor = colorPrimario)
                        ) {
                            Text("Reintentar")
                        }
                    }
                } else if (lectura != null) {
                    if (lectura.alertaGenerada == 1) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFDE8E8)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Alerta",
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Alerta: ${lectura.tipoAlerta ?: "Anomalía en el panel"}",
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    fontFamily = interBold
                                )
                            }
                        }
                    }

                    // Fila superior: Cosecha del Día | Ángulos Servo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 1 - Cosecha del Día
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = "Cosecha actual",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${lectura.energiaGeneradaWh ?: 0.0} Wh",
                                    fontSize = 18.sp,
                                    fontFamily = interBold
                                )
                            }
                        }

                        // Card 2 - Ángulos Servo
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = "Ángulos Servo",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = lectura.anguloPanel ?: "N/A",
                                    fontSize = 16.sp,
                                    fontFamily = interBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fila inferior: Voltaje y Corriente | Estado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 3 - Voltaje y Corriente
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFD600)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = "Voltaje y Corriente",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${lectura.voltajeV ?: 0.0}V | ${lectura.corrienteA ?: 0.0}A",
                                    fontSize = 15.sp,
                                    fontFamily = interBold,
                                    color = Color.Black
                                )
                            }
                        }

                        // Card 4 - Estado del modo
                        val modoTexto = when (lectura.idModo) {
                            1 -> "Siguiendo Luz"
                            2 -> "Manual"
                            else -> "Desconocido"
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = "Modo:",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = modoTexto,
                                    fontSize = 16.sp,
                                    fontFamily = interBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Último registro: ${lectura.fechaRegistro ?: "N/A"}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))


                val context = LocalContext.current

                Button(
                    onClick = {

                        val intent = Intent(
                            context,
                            EstadoYMatenimiento::class.java
                        )

                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrimario
                    )
                ) {
                    Text("Ver mantenimiento")
                }
            }
        }
    }
}
