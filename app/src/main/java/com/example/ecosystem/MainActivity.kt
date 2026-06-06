package com.example.ecosystem

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.colorNeutral
import com.example.ecosystem.ui.theme.colorPrimario
import com.example.ecosystem.ui.theme.colorSecundario
import com.example.ecosystem.ui.theme.interMedium
import com.example.ecosystem.ui.theme.interSemiBold
import com.example.ecosystem.ui.theme.interBold

class MainActivity : ComponentActivity() {
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
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Al ponerlo aquí, el error desaparece
        ) {

        ImagenFondoDecorativa()
        LazyColumn() {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(colorNeutral)
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



            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Título de la tarjeta",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Este es el contenido principal de tu tarjeta utilizando Kotlin.")
                    }
                }
            }
        }
    }
}


}


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