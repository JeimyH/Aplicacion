package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter


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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )  {
        // Aquí podrías añadir imagen o más contenido
        Image(
            painter = rememberAsyncImagePainter("https://drive.google.com/uc?export=view&id=1u8x3AmgqxNkGxGzXeprLSvOs3SRAElAv"),
            contentDescription = "Logo Screen1",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp, max = 400.dp)
                .padding(bottom = 24.dp)
        )

        Row {
            Text("PERSONALIZA Y DISEÑA TU RUTINA CON DIETASMART")
        }
        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = onClick
        ) {
            Text("Comenzar")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text("¿Ya tienes una cuenta?")
            Text(
                text = " Inicia Sesión",
                color = Color.Blue,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent1ScreenPreview() {
    RegistroVent1ScreenContent()
}
