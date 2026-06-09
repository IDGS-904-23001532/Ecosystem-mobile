package com.example.ecosystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoTarjetaInfo
import com.example.ecosystem.ui.theme.FondoTituloVerde
import com.example.ecosystem.ui.theme.GrisTextoSecundario
import com.example.ecosystem.ui.theme.GrisTrackBateria
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBateria() {
    Scaffold(

        containerColor = Color.White // Fondo blanco limpio general
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                // Título "Batería" estilo píldora
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f) // No ocupa todo el ancho
                            .background(
                                color = FondoTituloVerde,
                                shape = RoundedCornerShape(30.dp) // Bordes muy redondeados
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
                        .padding(horizontal = 24.dp) // Padding general de la pantalla
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    // Indicador de carga circular
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularBatteryIndicator(percentage = 0.45f)
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    // Sección Información
                    Text(
                        text = "Información",
                        fontSize = 22.sp,
                        fontFamily = interBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Cuadrícula de tarjetas de información
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoCard(
                            label = "Estimar el completado de\ntiempo de carga", // Salto de línea como en mockup
                            value = "10 H",
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            label = "Promedio de carga",
                            value = "40 %",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoCard(
                            label = "Estado",
                            value = "Alimentación Solar\nActiva", // Salto de línea
                            modifier = Modifier.weight(1f),
                            isMediumValue = true
                        )
                        InfoCard(
                            label = "Consumo Neto",
                            value = "0.45",
                            modifier = Modifier.weight(1f)
                        )
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
        modifier = modifier.size(220.dp) // Círculo un poco más grande
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx() // Grosor mucho más fino para igualar la imagen

            // 1. Fondo del círculo (Gris claro)
            drawCircle(
                color = GrisTrackBateria,
                radius = size.minDimension / 2 - strokeWidth / 2,
                style = Stroke(width = strokeWidth)
            )

            // 2. Arco de progreso (Verde)
            drawArc(
                color = colorPrimario,
                startAngle = -90f,
                sweepAngle = progressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            // No agregamos el punto indicador superior porque el mockup actual no lo tiene
        }

        // Texto central
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${(percentage * 100).toInt()}%",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium, // No tan negrita como antes
                color = colorPrimario
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "65% spent",
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
            .height(140.dp), // Altura fija para que todas sean del mismo tamaño
        shape = RoundedCornerShape(20.dp), // Bordes más suaves/redondos
        colors = CardDefaults.cardColors(
            containerColor = FondoTarjetaInfo
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Centrar contenido verticalmente
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Black, // Textos negros en lugar de grises
                fontWeight = FontWeight.SemiBold,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                fontSize = if (isMediumValue) 14.sp else 26.sp, // Diferencia de tamaño para el texto de Estado
                fontFamily = interBold,
                color = Color.Black
            )
        }
    }
}
