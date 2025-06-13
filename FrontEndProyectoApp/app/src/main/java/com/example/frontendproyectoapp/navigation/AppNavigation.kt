package com.example.frontendproyectoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontendproyectoapp.screen.AjustesScreen
import com.example.frontendproyectoapp.screen.AlimentosFavoritosScreen
import com.example.frontendproyectoapp.screen.BuscarAlimentoScreen
import com.example.frontendproyectoapp.screen.DetalleAlimentoScreen
import com.example.frontendproyectoapp.screen.InicioScreen
import com.example.frontendproyectoapp.screen.LoginScreen
import com.example.frontendproyectoapp.screen.RegistroVent1Screen
import com.example.frontendproyectoapp.screen.RegistroVent2Screen
import com.example.frontendproyectoapp.screen.RegistroVent3Screen
import com.example.frontendproyectoapp.screen.RegistroVent4Screen
import com.example.frontendproyectoapp.screen.RegistroVent5Screen
import com.example.frontendproyectoapp.screen.RegistroVent6Screen
import com.example.frontendproyectoapp.screen.RegistroVent7Screen
import com.example.frontendproyectoapp.screen.RegistroVent8Screen
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: UsuarioViewModel) {
    NavHost(navController = navController, startDestination = "registro1") {
        composable("registro1") { RegistroVent1Screen(navController) }
        composable("registro2") { RegistroVent2Screen(navController, viewModel) }
        composable("registro3") { RegistroVent3Screen(navController, viewModel) }
        composable("registro4") { RegistroVent4Screen(navController, viewModel) }
        composable("registro5") { RegistroVent5Screen(navController, viewModel) }
        composable("registro6") { RegistroVent6Screen(navController) }
        composable("registro7") { RegistroVent7Screen(navController, viewModel) }
        composable("registro8") { RegistroVent8Screen(navController, viewModel) }
        composable("inicio") { InicioScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("buscarAlimentos") {BuscarAlimentoScreen(navController)}
        composable("favoritos") { AlimentosFavoritosScreen(navController)}
        //composable("rutina") {RutinaScreen(navController)}
        //composable("estadisticas") {Estadisticas(navController)}
        composable("ajustes") { AjustesScreen(navController)}
        composable("detalleAlimento/{idAlimento}") { backStackEntry ->
            val idAlimento = backStackEntry.arguments?.getString("idAlimento")?.toLongOrNull()
            if (idAlimento != null) {
                DetalleAlimentoScreen(idAlimento = idAlimento, navController = navController)
            }
        }

    }
}