package com.example.ecosystem

import android.os.Bundle
import android.os.PersistableBundle
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.*
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoTarjetaInfo
import com.example.ecosystem.ui.theme.FondoTituloVerde
import com.example.ecosystem.ui.theme.VerdeEcoLogo
import com.example.ecosystem.ui.theme.colorNeutral
import com.example.ecosystem.ui.theme.colorPrimario

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
                        .clickable { /*Accion*/ }
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
                color = Color.Black,
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