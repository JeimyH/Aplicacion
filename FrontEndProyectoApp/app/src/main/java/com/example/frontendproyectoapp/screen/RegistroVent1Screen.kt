package com.example.frontendproyectoapp.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
            .data("https://drive.google.com/uc?export=view&id=1u8x3AmgqxNkGxGzXeprLSvOs3SRAElAv") // Reemplaza por una URL directa válida
            .crossfade(true)
            .listener(
                onSuccess = { _, _ ->
                    Log.d("RegistroVent1Screen", "Imagen cargada exitosamente.")
                },
                onError = { _, result ->
                    Log.e("RegistroVent1Screen", "Error al cargar imagen: ${result.throwable}")
                }
            )
            .build()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        // Imagen circular centrada
        Image(
            painter = imagePainter,
            contentDescription = "Logo Circular",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(340.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "PERSONALIZA Y DISEÑA TU RUTINA CON DIETASMART",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors( // ✅ ADAPTADO al tema
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("¿Ya tienes una cuenta?",
                    color = MaterialTheme.colorScheme.onBackground
                    )
                Text(
                    text = " Inicia Sesión",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun RegistroVent1ScreenPreview() {
    RegistroVent1ScreenContent()
}
