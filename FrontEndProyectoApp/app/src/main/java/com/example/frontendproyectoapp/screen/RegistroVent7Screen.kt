package com.example.frontendproyectoapp.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun RegistroVent7Screen(navController: NavController) {
    RegistroVent7ScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro8") }
    )
}

@Composable
fun RegistroVent7ScreenContent(
    onBackClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data("https://drive.google.com/uc?export=view&id=1g0Ky2WqSbPYZdp0Ee0hcg0uvSPJ97-Lr") // Reemplaza con URL válida
            .crossfade(true)
            .listener(
                onSuccess = { _, _ ->
                    Log.d("RegistroVent6Screen", "Imagen cargada correctamente.")
                },
                onError = { _, result ->
                    Log.e("RegistroVent6Screen", "Error al cargar imagen: ${result.throwable}")
                }
            )
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Botón atrás en la parte superior izquierda
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        // Contenido centrado
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen redondeada y centrada
            Image(
                painter = imagePainter,
                contentDescription = "Logo Screen6",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(340.dp) // Imagen más grande y cuadrada
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(24.dp)) // Menor espacio con texto

            Text(
                text = "Continuemos, ahora personalizaremos tu rutina de comidas para alcanzar tus calorías diarias",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Botón continuar anclado en la parte inferior
        Button(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Continuar")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RegistroVent7ScreenPreview() {
    RegistroVent7ScreenContent()
}
