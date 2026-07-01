package com.example.ecosystem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoTarjetaInfo
import com.example.ecosystem.ui.theme.FondoTituloVerde
import com.example.ecosystem.ui.theme.VerdeEcoLogo
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDispositivos() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            // Barra superior con formato uniforme e idéntico a Control Jardín
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Dispositivos",
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
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Boton Agregar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    color = VerdeEcoLogo,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .clickable { cambiarPantalla(context) }
                        .wrapContentSize()
                ) {
                    Text(
                        text = "Agregar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tus dispositivos",
                color = colorPrimario,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 1
            TarjetaDispositivos(
                iconRes = R.drawable.panel_solar_info,
                name = "Panel solar",
                colorTarjeta = FondoTarjetaInfo,
                colorInterruptor = VerdeEcoLogo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 2
            TarjetaDispositivos(
                iconRes = R.drawable.panel_solar_info,
                name = "Panel solar 2",
                colorTarjeta = FondoTarjetaInfo,
                colorInterruptor = VerdeEcoLogo
            )
        }
    }
}

@Composable
fun TarjetaDispositivos(iconRes: Int, name: String, colorTarjeta: Color, colorInterruptor: Color){
    Surface(
        color = colorTarjeta,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            // Interruptor switch
            var isChecked by remember { mutableStateOf(true) }
            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = colorInterruptor,
                    checkedThumbColor = Color.White
                )
            )
        }
    }
}

fun cambiarPantalla(context: Context) {
    val intent = Intent(context, AgregarDispositivo::class.java)
    context.startActivity(intent)
}