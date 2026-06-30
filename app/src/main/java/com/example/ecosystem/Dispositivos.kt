package com.example.ecosystem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoTarjetaInfo
import com.example.ecosystem.ui.theme.FondoTituloVerde
import com.example.ecosystem.ui.theme.VerdeEcoLogo

class Dispositivos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                PantallaDispositivos()
            }
        }
    }
}

@Composable
fun PantallaDispositivos() {
    // Obtenemos el contexto actual de la aplicación para poder usar los Intents
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Titulo
                Surface(
                    color = FondoTituloVerde,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .wrapContentWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Dispositivos",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 45.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Tus dispositivos",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Distribución de las dos tarjetas en paralelo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1ra Card: Hogar / Casa
                TarjetaDispositivo(
                    iconRes = R.drawable.house_icon,
                    name = "Hogar",
                    colorTarjeta = FondoTarjetaInfo,
                    colorBoton = VerdeEcoLogo,
                    modifier = Modifier.weight(1f),
                    onConectarClick = {
                        val intent = Intent(context, HogarActivity::class.java)
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                // 2da Card: Jardín
                TarjetaDispositivo(
                    iconRes = R.drawable.panel_solar_info,
                    name = "Jardín",
                    colorTarjeta = FondoTarjetaInfo,
                    colorBoton = VerdeEcoLogo,
                    modifier = Modifier.weight(1f),
                    onConectarClick = {
                        //val intent = Intent(context, JardinActivity::class.java)
                        //context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaDispositivo(
    iconRes: Int,
    name: String,
    colorTarjeta: Color,
    colorBoton: Color,
    modifier: Modifier = Modifier,
    onConectarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Icono de $name",
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Conectar
            Surface(
                color = colorBoton,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConectarClick() }
            ) {
                Text(
                    text = "Conectar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}