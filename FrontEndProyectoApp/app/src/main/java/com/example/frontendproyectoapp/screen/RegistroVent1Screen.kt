package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import com.example.frontendproyectoapp.R

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegistroVent1Screen(navController: NavController) {
    // NavController solo se usa para la vista previa, no es necesario pasarlo en el Composable principal
    RegistroVent1ScreenContent(
        onClick = { navController.navigate("registro2") },
        onLoginClick = { navController.navigate("login") }
    )
}

@Composable
fun RegistroVent1ScreenContent(
    onClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    // Determina el tema de color actual para seleccionar la imagen de fondo
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundImage = if (isDarkTheme) {
        painterResource(id = R.drawable.fondo1) // Imagen de fondo para el modo oscuro
    } else {
        painterResource(id = R.drawable.fondo2) // Imagen de fondo para el modo claro
    }

    // Estado para la animación del difuminado
    val animatedBlurRadius by animateDpAsState(
        targetValue = 1.dp,
        animationSpec = tween(durationMillis = 1500, easing = LinearEasing), label = "BlurAnimation"
    )

    // Estado para la animación de opacidad del contenido
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500), label = "ContentAlpha"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo con difuminado animado
        Image(
            painter = backgroundImage,
            contentDescription = "Fondo Bienvenida",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radiusX = animatedBlurRadius,
                    radiusY = animatedBlurRadius,
                    edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
                )
        )

        // Capa de contenido superior
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(138.dp))

            // Título principal con sombras mejoradas
            Text(
                text = "Bienvenido a DietaSmart",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        offset = Offset(x = 2f, y = 2f),
                        blurRadius = 8f
                    )
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Eslogan o frase motivacional
            Text(
                text = "Comienza hoy. Tú puedes lograrlo.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontStyle = FontStyle.Italic,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        offset = Offset(x = 2f, y = 2f),
                        blurRadius = 8f
                    )
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }

        // Capa de contenido inferior
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Subtítulo motivador
            Text(
                text = "Tu salud es nuestra prioridad.\nDiseña tu rutina personalizada.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        offset = Offset(x = 2f, y = 2f),
                        blurRadius = 8f
                    )
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Botón de registro
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    "Empezar ahora",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            offset = Offset(x = 2f, y = 2f),
                            blurRadius = 8f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Opción para iniciar sesión
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "¿Ya tienes una cuenta?",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            offset = Offset(x = 2f, y = 2f),
                            blurRadius = 8f
                        )
                    )
                )
                Text(
                    text = " Inicia sesión",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            offset = Offset(x = 2f, y = 2f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier
                        .clickable { onLoginClick() }
                        .padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent1ScreenPreview() {
    RegistroVent1ScreenContent()
}