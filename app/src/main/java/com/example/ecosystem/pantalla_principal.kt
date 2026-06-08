package com.example.ecosystem

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.interBold
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecosystem.ui.theme.colorNeutral
import com.example.ecosystem.ui.theme.interSemiBold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import com.example.ecosystem.ui.theme.botonGris
import com.example.ecosystem.ui.theme.colorTerciario
import com.example.ecosystem.ui.theme.interRegular
import kotlin.jvm.java

class pantalla_principal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcosystemTheme {

                inicio()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcosystemTheme {

    }
}

@Composable
fun inicio() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            ImagenFondoDecorativa()
            LazyColumn() {
                // La parte superior
                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(colorNeutral.copy(alpha = 0.86F))
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(15.dp)
                                .align(alignment = Alignment.Start),
                            text = "EcoSystem",
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = colorPrimario,
                                fontFamily = interSemiBold
                            )
                        )
                    }
                }
                // La parte de la carta
                item {
                    Spacer(modifier = Modifier.height(340.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(390.dp)
                            .padding(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorNeutral,
                            contentColor = colorTerciario
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Bienvendo a " +
                                        "EcoSystem",
                                fontFamily = interBold,
                                fontSize = 35.sp,
                                lineHeight = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            Text(text = "Monitorea la energía solar en tiempo real y impulsa el consumo sustentanble.",
                                fontFamily = interRegular,
                                fontSize =  18.sp,
                                color = botonGris,
                                textAlign = TextAlign.Center // lo alinie al centro por que se hizo mejor
                            )
                            Spacer(modifier = Modifier.height(58.dp))
                            // El boton
                            Button( modifier = Modifier.height(60.dp).width(300.dp),
                                onClick = {
                                    val intent = Intent(
                                        context,
                                        Inicio::class.java,
                                    )
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorPrimario, // El color de fondo
                                    contentColor = colorNeutral      // El color del texto/icono
                                ))

                            {
                                Text(text = "Empezar Ahora",
                                    style = TextStyle(
                                        fontSize = 16.sp

                                    )
                                )

                                //  aqui hice un pequeño espacio entre el texto y la flecha
                                Spacer(modifier = Modifier.width(8.dp))
                                // Es el icono de flecha
                                Icon(
                                    painter = painterResource(id = R.drawable.flecha_correcta),
                                    contentDescription = "Flecha ir a inicio", // Descripción para accesibilidad
                                    modifier = Modifier.size(14.dp) //  ajustar el tamaño de la flecha aquí
                                )

                                Spacer(modifier = Modifier.height(30.dp))


                            }
                            Text(text = "Unete a la comundad de los hogares sustentables",
                                fontFamily = interRegular,
                                fontSize =  10.sp,
                                textAlign = TextAlign.Center, // lo alinie al centro por que se hizo mejor
                                color = botonGris
                            )
                        }
                    }
                }
            }
        }
    }

// Quiero dormir jajaj
}


// La funcion de la imagen que llamo al inicio()
@Composable
fun ImagenFondoDecorativa() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.panel_solar),
            contentDescription = "Imagen de Fondo",
            contentScale = ContentScale.Crop, // Escala recortando los bordes excedentes
            alpha = 0.9F, // Aplica un canal alfa del 15% para que no opaque los textos
            modifier = Modifier.fillMaxSize()
        )
    }
}