package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun RegistroVent6Screen(navController: NavController) {
    RegistroVent6ScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro7") }
    )
}

@Composable
fun RegistroVent6ScreenContent(
    onBackClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
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
            Image(
                painter = rememberAsyncImagePainter("https://drive.google.com/uc?export=view&id=1g0Ky2WqSbPYZdp0Ee0hcg0uvSPJ97-Lr"),
                contentDescription = "Logo Screen6",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp, max = 400.dp)
                    .padding(bottom = 24.dp)
            )
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
                //.fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Continuar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistroVent6ScreenPreview() {
    RegistroVent6ScreenContent()
}
