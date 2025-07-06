package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


@Composable
fun RegistroVent1Screen(navController: NavController) {
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
    val context = LocalContext.current

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data("https://drive.google.com/uc?export=view&id=1u8x3AmgqxNkGxGzXeprLSvOs3SRAElAv")
            .crossfade(true)
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Elimina el degradado
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Imagen circular ajustada completamente
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .shadow(8.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Logo Circular",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()  // Se ajusta completamente al círculo
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PERSONALIZA Y DISEÑA TU RUTINA CON DIETASMART",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Registrarse", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        "¿Ya tienes una cuenta?",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = " Inicia Sesión",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier
                            .clickable { onLoginClick() }
                            .padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistroVent1ScreenPreview() {
    RegistroVent1ScreenContent()
}
