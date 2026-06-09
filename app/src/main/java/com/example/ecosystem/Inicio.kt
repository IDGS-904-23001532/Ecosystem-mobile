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

class Inicio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                PantallaEstadisticas()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadisticas() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Estadísticas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorPrimario,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Consumo Semanal",
                    fontSize = 20.sp,
                    fontFamily = interBold,
                    color = colorPrimario,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Lunes
                    Barra(65, "L")
                    // Martes
                    Barra(58, "M")
                    // Miércoles
                    Barra(78, "M")
                    // Jueves
                    Barra(70, "J")
                    // Viernes
                    Barra(95, "V")
                    // Sábado
                    Barra(89, "S")
                    // Domingo
                    Barra(75, "D")
                }
            }
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
                            Text("Día", fontWeight = FontWeight.Bold)
                            Text("kWh", fontWeight = FontWeight.Bold)
                            Text("%", fontWeight = FontWeight.Bold)
                        }
                        FilaTabla("Television", "4.2", "65%")
                        FilaTabla("PC", "3.8", "58%")
                        FilaTabla("Horno", "5.1", "78%")
                        FilaTabla("Panel Solar", "4.5", "70%")
                        FilaTabla("Luz", "6.2", "95%")
                        FilaTabla("Lavadora", "5.8", "89%")
                        FilaTabla("Licuadora", "4.9", "75%")

                        Spacer(modifier = Modifier.height(20.dp))

                        val context = LocalContext.current

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
