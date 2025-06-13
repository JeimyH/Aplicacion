package com.example.frontendproyectoapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.frontendproyectoapp.navigation.AppNavigation
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current.applicationContext as Application

            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = ViewModelProvider.AndroidViewModelFactory(context)
            )

            val navController = rememberNavController()
            AppNavigation(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }
    }
}
