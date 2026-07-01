package com.example.ecosystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.botonGris
import com.example.ecosystem.ui.theme.colorNeutral
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.colorRojo
import com.example.ecosystem.ui.theme.colorSecundario
import com.example.ecosystem.ui.theme.interBold
import com.example.ecosystem.ui.theme.interSemiBold

class EstadoYMatenimiento : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                PantallaMantenimiento()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMantenimiento() {
    Scaffold(
        topBar = {
            // Barra superior idéntica al formato de Control Jardín
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Estado y Mantenimiento",
                        fontSize = 22.sp,
                        fontFamily = interBold,
                        color = colorPrimario
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // --- TÍTULO PRINCIPAL ---
            Text(
                text = "Estado General del Sistema",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorPrimario
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- ÍCONO GIGANTE VERDE ---
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Todo en orden",
                tint = colorPrimario,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- LISTA DE CHEQUEO ---
            Column(horizontalAlignment = Alignment.Start) {
                ItemChequeo(texto = "Módulos solares", colorIcono = colorPrimario)
                ItemChequeo(texto = "Inversor", colorIcono = colorPrimario)
                ItemChequeo(texto = "Baterías", colorIcono = colorPrimario)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECCIÓN HISTORIAL ---
            Text(
                text = "Historial",
                fontSize = 20.sp,
                color = colorPrimario,
                modifier = Modifier.align(Alignment.Start),
                style = TextStyle(
                    fontFamily = interSemiBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- TARJETA DE HISTORIAL ---
            Card(
                colors = CardDefaults.cardColors(containerColor = colorNeutral),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ItemAlerta(
                        icono = Icons.Filled.Close,
                        colorIcono = colorRojo,
                        texto = "Sobrecarga Detectada- Circuito A [Lavadora]"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemAlerta(
                        icono = Icons.Filled.Warning,
                        colorIcono = colorSecundario,
                        texto = "Batería < 10% - Carga sugerida"
                    )
                }
            }

            // Spacer para empujar el contenido hacia arriba de forma proporcional
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// --- "MINI-FUNCIONES" REUTILIZABLES ---

@Composable
fun ItemChequeo(texto: String, colorIcono: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = colorIcono,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
fun ItemAlerta(icono: ImageVector, colorIcono: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = colorIcono,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = texto,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun CirculoMenu(color: Color) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(color)
    )
}