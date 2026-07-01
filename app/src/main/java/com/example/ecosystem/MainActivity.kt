package com.example.ecosystem

import android.R.attr.lineHeight
import android.content.Intent
import android.graphics.Color
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
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ecosystem.Bateria.BateriaWorker
import com.example.ecosystem.ui.theme.botonGris
import com.example.ecosystem.ui.theme.colorTerciario
import com.example.ecosystem.ui.theme.interRegular
import com.example.ecosystem.ui.theme.interthin
import com.example.ecosystem.Login
import com.example.ecosystem.navigation.AppNavigation
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Crear el canal de notificaciones (Solo se crea una vez, el sistema lo ignora si ya existe)
        crearCanalNotificacion(
            context = this,
            idCanal = "canal_bateria",
            nombre = "Estado de Batería",
            descripcion = "Notificaciones periódicas del estado del sistema solar"
        )

        // 2. Configurar la tarea periódica (Mínimo 15 minutos)
        val bateriaWorkRequest = PeriodicWorkRequestBuilder<BateriaWorker>(
            15, TimeUnit.MINUTES // ¡Recuerda, 15 es el mínimo!
        ).build()

        // 3. Encolar el trabajo asegurando que no se duplique si abren la app varias veces
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "VerificacionBateria",
            ExistingPeriodicWorkPolicy.KEEP, // KEEP evita reiniciar el temporizador si ya está corriendo
            bateriaWorkRequest
        )

        enableEdgeToEdge()
        setContent {
            EcosystemTheme {
                LoginScreen()
               // PantallaControlCasa()
            }
        }
    }
}

// LOGIN CON ANIMACION
@Composable
fun LoginScreen() {

    var sesionIniciada by remember {
        mutableStateOf(false)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    when {

        cargando -> {

            LoadingScreen(
                alFinalizar = {
                    cargando = false
                    sesionIniciada = true
                }
            )
        }

        sesionIniciada -> {

            AppNavigation()
        }

        else -> {

            Login(
                alIniciarSesionCorrectamente = {
                    cargando = true
                }
            )
        }
    }
}