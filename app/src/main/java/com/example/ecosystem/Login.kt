package com.example.ecosystem

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.lint.kotlin.metadata.Visibility
import com.example.ecosystem.R
import com.example.ecosystem.ui.theme.BordeCampo
import com.example.ecosystem.ui.theme.BlancoTranslucido
import com.example.ecosystem.ui.theme.FondoLogin
import com.example.ecosystem.ui.theme.GrisPlaceholder
import com.example.ecosystem.ui.theme.TextoOscuro
import com.example.ecosystem.ui.theme.TextoSecundario
import com.example.ecosystem.ui.theme.VerdeEco
import com.example.ecosystem.ui.theme.VerdeEcoLogo
import com.example.ecosystem.ui.theme.TextoOscuro

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
                alEmpezarAhora = {
                    pantallaActual = PantallaLogin.INICIAR_SESION
                }
            )

            PantallaLogin.INICIAR_SESION -> PantallaInicioSesion(
                alVolver = {
                    pantallaActual = PantallaLogin.BIENVENIDA
                },
                alIniciarSesion = alIniciarSesionCorrectamente,
                alRegistrarse = {
                    pantallaActual = PantallaLogin.REGISTRAR
                }
            )

            PantallaLogin.REGISTRAR -> PantallaRegistro(
                alVolver = {
                    pantallaActual = PantallaLogin.INICIAR_SESION
                },
                alRegistrarse = {
                    pantallaActual = PantallaLogin.INICIAR_SESION
                }
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
fun PantallaInicioSesion(
    alVolver: () -> Unit,
    alIniciarSesion: () -> Unit,
    alRegistrarse: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLogin)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(VerdeEcoLogo),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_ecosystem),
                contentDescription = "Logo EcoSystem",
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "EcoSystem",
            color = VerdeEco,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Gestión energética inteligente",
            color = TextoSecundario,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
        ) {
            Text(
                text = "Iniciar sesión",
                color = TextoOscuro,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bienvenido de nuevo a tu centro de control solar.",
                color = TextoSecundario,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Correo electrónico",
                color = TextoOscuro,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "ejemplo@correo.com",
                        color = GrisPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Correo",
                        tint = GrisPlaceholder
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordeCampo,
                    unfocusedBorderColor = BordeCampo,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = VerdeEco
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contraseña",
                    color = TextoOscuro,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = VerdeEco,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = GrisPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Contraseña",
                        tint = GrisPlaceholder
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                        Icon(
                            imageVector = if (mostrarContrasena) {
                                Icons.Outlined.Visibility
                            } else {
                                Icons.Outlined.VisibilityOff
                            },
                            contentDescription = "Mostrar contraseña",
                            tint = GrisPlaceholder
                        )
                    }
                },
                visualTransformation = if (mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordeCampo,
                    unfocusedBorderColor = BordeCampo,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = VerdeEco
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = alIniciarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeEco,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Entrar  →",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = TextoSecundario
                        )
                    ) {
                        append("¿No tienes cuenta? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = VerdeEco,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Regístrate")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alRegistrarse() },
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = alVolver,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Volver",
                    color = VerdeEco
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
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
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarConfirmarContrasena by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLogin)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(VerdeEcoLogo),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_ecosystem),
                contentDescription = "Logo EcoSystem",
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "EcoSystem",
            color = VerdeEco,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Gestión energética inteligente",
            color = TextoSecundario,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
        ) {
            Text(
                text = "Crear cuenta",
                color = TextoOscuro,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Regístrate para comenzar a monitorear tu energía solar.",
                color = TextoSecundario,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Nombre completo",
                color = TextoOscuro,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "Karla Martínez",
                        color = GrisPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Nombre",
                        tint = GrisPlaceholder
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordeCampo,
                    unfocusedBorderColor = BordeCampo,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = VerdeEco
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Correo electrónico",
                color = TextoOscuro,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "ejemplo@correo.com",
                        color = GrisPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Correo",
                        tint = GrisPlaceholder
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordeCampo,
                    unfocusedBorderColor = BordeCampo,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = VerdeEco
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Contraseña",
                color = TextoOscuro,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = GrisPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Contraseña",
                        tint = GrisPlaceholder
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                        Icon(
                            imageVector = if (mostrarContrasena) {
                                Icons.Outlined.Visibility
                            } else {
                                Icons.Outlined.VisibilityOff
                            },
                            contentDescription = "Mostrar contraseña",
                            tint = GrisPlaceholder
                        )
                    }
                },
                visualTransformation = if (mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordeCampo,
                    unfocusedBorderColor = BordeCampo,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = VerdeEco
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Confirmar contraseña",
                color = TextoOscuro,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = GrisPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Confirmar contraseña",
                        tint = GrisPlaceholder
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { mostrarConfirmarContrasena = !mostrarConfirmarContrasena }) {
                        Icon(
                            imageVector = if (mostrarConfirmarContrasena) {
                                Icons.Outlined.Visibility
                            } else {
                                Icons.Outlined.VisibilityOff
                            },
                            contentDescription = "Mostrar contraseña",
                            tint = GrisPlaceholder
                        )
                    }
                },
                visualTransformation = if (mostrarConfirmarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordeCampo,
                    unfocusedBorderColor = BordeCampo,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = VerdeEco
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = alRegistrarse,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeEco,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Registrarse  →",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = TextoSecundario
                        )
                    ) {
                        append("¿Ya tienes cuenta? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = VerdeEco,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Iniciar sesión")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alVolver() },
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = alVolver,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Volver",
                    color = VerdeEco
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}