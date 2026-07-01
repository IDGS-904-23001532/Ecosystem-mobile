package com.example.ecosystem

import android.content.Context
import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosystem.servicios.DatabaseHelper
import com.example.ecosystem.ui.theme.BordeCampo
import com.example.ecosystem.ui.theme.BlancoTranslucido
import com.example.ecosystem.ui.theme.EcosystemTheme
import com.example.ecosystem.ui.theme.FondoLogin
import com.example.ecosystem.ui.theme.GrisPlaceholder
import com.example.ecosystem.ui.theme.TextoOscuro
import com.example.ecosystem.ui.theme.TextoSecundario
import com.example.ecosystem.ui.theme.VerdeEco
import com.example.ecosystem.ui.theme.VerdeEcoLogo
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import kotlinx.coroutines.launch


enum class PantallaLogin {
    BIENVENIDA,
    INICIAR_SESION,
    REGISTRAR
}

@Composable
fun Login(
    alIniciarSesionCorrectamente: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pantallaActual by remember { mutableStateOf(PantallaLogin.BIENVENIDA) }

    Box(modifier = modifier.fillMaxSize()) {
        when (pantallaActual) {
            PantallaLogin.BIENVENIDA -> PantallaBienvenida(
                alEmpezarAhora = { pantallaActual = PantallaLogin.INICIAR_SESION }
            )
            PantallaLogin.INICIAR_SESION -> PantallaInicioSesion(
                alVolver = { pantallaActual = PantallaLogin.BIENVENIDA },
                alIniciarSesion = alIniciarSesionCorrectamente,
                alRegistrarse = { pantallaActual = PantallaLogin.REGISTRAR }
            )
            PantallaLogin.REGISTRAR -> PantallaRegistro(
                alVolver = { pantallaActual = PantallaLogin.INICIAR_SESION },
                alRegistrarse = { pantallaActual = PantallaLogin.INICIAR_SESION }
            )
        }
    }
}

@Composable
fun PantallaBienvenida(
    alEmpezarAhora: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ecosystem),
            contentDescription = "Fondo panel solar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            Color.White.copy(alpha = 0.05f),
                            Color.Black.copy(alpha = 0.18f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(VerdeEco)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "EcoSystem",
                color = VerdeEco,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 34.dp)
                .fillMaxWidth()
                .widthIn(max = 380.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = BlancoTranslucido
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido a\nEcoSystem",
                    textAlign = TextAlign.Center,
                    color = TextoOscuro,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Monitorea tu energía solar en tiempo real y optimiza tu consumo sustentable.",
                    textAlign = TextAlign.Center,
                    color = TextoSecundario,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = alEmpezarAhora,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeEco,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Empezar ahora  →",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Únete a los más de 50,000 hogares sustentables.",
                    color = TextoSecundario,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun PantallaLoginPrincipal(
    alEmpezarAhora: () -> Unit
) { // Esta es la función que arranca en tu archivo de login
    EcosystemTheme {
        // En lugar de llamar a 'PantallaBienvenida()', llamamos directo a tu pantalla:
        inicio(alEmpezarAhora = alEmpezarAhora)
    }
}



@Composable
fun PantallaInicioSesion(
    alVolver: () -> Unit,
    alIniciarSesion: () -> Unit,
    alRegistrarse: () -> Unit
) {

    val context = LocalContext.current
    // Acceso a SharedPreferences we
    val sharedPref = remember { context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE) }

    var correo by remember {
        mutableStateOf(sharedPref.getString("correo", "") ?: "")
    }

    var contrasena by remember {
        mutableStateOf(sharedPref.getString("pass", "") ?: "")
    }

    var recordar by remember {
        mutableStateOf(sharedPref.getBoolean("recordar", false))
    }

    var mostrarContrasena by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(FondoLogin).systemBarsPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        // Logo
        Box(modifier = Modifier.size(70.dp).clip(RoundedCornerShape(20.dp)).background(VerdeEcoLogo), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.logo_ecosystem), contentDescription = null, modifier = Modifier.size(35.dp))
        }
        Text("EcoSystem", color = VerdeEco, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(40.dp))

        // Campos
        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Outlined.Email, null) },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = contrasena, onValueChange = { contrasena = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            trailingIcon = {
                IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                    Icon(if (mostrarContrasena) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, null)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = recordar,
                onCheckedChange = { recordar = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = VerdeEco
                )
            )

            Text(
                text = "Recordar mis datos",
                color = TextoSecundario,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    recordar = !recordar
                }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(context, "Llena los campos", Toast.LENGTH_SHORT).show()
                } else {
                    scope.launch {
                        val res = DatabaseHelper.loginUser(correo, contrasena)
                        if (res.getString("res") == "success") {

                            val editor = sharedPref.edit()

                            if (recordar) {
                                editor.putString("correo", correo)
                                editor.putString("pass", contrasena)
                                editor.putBoolean("recordar", true)
                            } else {
                                editor.clear()
                            }

                            editor.apply()

                            val mensajeBienvenida = res.getString("msg")
                            Toast.makeText(context, mensajeBienvenida, Toast.LENGTH_SHORT).show()
                            alIniciarSesion()
                        } else {
                            Toast.makeText(context, res.getString("msg"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeEco),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Entrar  →", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "¿No tienes cuenta? Regístrate",
            modifier = Modifier.clickable { alRegistrarse() },
            color = VerdeEco,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun PantallaRegistro(
    alVolver: () -> Unit,
    alRegistrarse: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(FondoLogin).systemBarsPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", color = VerdeEco, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre Completo") }, shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = correo, onValueChange = { correo = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Correo") }, shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = confirmarContrasena, onValueChange = { confirmarContrasena = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Confirmar") }, visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(16.dp))

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (contrasena != confirmarContrasena) {
                    Toast.makeText(context, "Las claves no coinciden", Toast.LENGTH_SHORT).show()
                } else if (nombre.isEmpty() || correo.isEmpty()) {
                    Toast.makeText(context, "Llena todo el formulario pls", Toast.LENGTH_SHORT).show()
                } else {
                    scope.launch {
                        val res = DatabaseHelper.registerUser(nombre, correo, correo, contrasena)
                        if (res.getString("res") == "success") {
                            Toast.makeText(context, "¡Listo! Ya puedes entrar", Toast.LENGTH_SHORT).show()
                            alVolver()
                        } else {
                            Toast.makeText(context, res.getString("msg"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeEco),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Registrarse  →", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("¿Ya tienes cuenta? Inicia sesión", modifier = Modifier.clickable { alVolver() }, color = VerdeEco)
    }
}