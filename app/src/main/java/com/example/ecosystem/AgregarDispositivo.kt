package com.example.ecosystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoTarjetaInfo
import com.example.ecosystem.ui.theme.FondoTituloVerde
import com.example.ecosystem.ui.theme.VerdeEco

class AgregarDispositivo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            EcosystemTheme {
                PantallaAgregarDispositivo()
            }
        }
    }
}

@Composable
fun PantallaAgregarDispositivo() {

    var nombre_dispositivo by remember { mutableStateOf("") }
    var icono_seleccionado by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column (
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
                        text = "Agregar dispositivo",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 45.dp)
                    )
                }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Información",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = FondoTarjetaInfo,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = nombre_dispositivo,
                                onValueChange = { nombre_dispositivo = it },
                                label = {
                                    Text(text = "Nombre del dispositivo", color = VerdeEco)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Seccion de buscar dispositivos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Buscar conexión",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Button(
                            onClick = { /* Acción actualizar MQTT */ },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeEco),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Actualizar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tarjeta de dispositivo encontrado
                    Surface(
                        color = FondoTarjetaInfo,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 4.dp
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Foco cocina",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ID: XXXX-XXXX-XXXX",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            Button(
                                onClick = { /* Acción conectar */ },
                                colors = ButtonDefaults.buttonColors(containerColor = VerdeEco),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Conectar", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón Guardar (Abajo)
                    Button(
                        onClick = { /* Guardar configuración */ },
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeEco),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Guardar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }