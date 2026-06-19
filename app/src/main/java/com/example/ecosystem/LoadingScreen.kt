package com.example.ecosystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue

@Composable
fun LoadingScreen(
    alFinalizar: () -> Unit
) {

    LaunchedEffect(Unit) {

        delay(6000) // tiempo de carga (se ajustara despues para cagrar al usuario)

        alFinalizar()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val composicion by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.loading_ecosystem
            )
        )

        LottieAnimation(
            composition = composicion,
            iterations = LottieConstants.IterateForever
        )

        Text(
            text = "Bienvenido a EcoSystem, (username)...",
            fontSize = 20.sp
        )
    }
}