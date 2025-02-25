package com.example.airqualityapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

// Abaixo segue o composable caso seja criado a splash screen a partir de uma classe. A splash
// screen atual está sendo chamada direto da pasta layout. Ela é montada a partir do arquivo
// splash_screen.xml.
@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(5000) // Aguarda 5 segundos antes de navegar para a Home
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true } // Remove a Splash da pilha de navegação
        }
    }
}