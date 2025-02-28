package com.example.airqualityapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.utils.getBackgroundGradient
import kotlinx.coroutines.delay

// Abaixo segue o composable caso seja criado a splash screen a partir de uma classe. A splash
// screen atual está sendo chamada direto da pasta layout. Ela é montada a partir do arquivo
// splash_screen.xml.
@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(5000L) // Tempo de exibição da SplashScreen (2 segundos)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true } // Remove a SplashScreen da pilha
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getBackgroundGradient())
            .padding(4.dp),
    ) {
        Text(
            text = "Splash Screen",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview(){
    AirQualityAppTheme {
        SplashScreen(navController = NavHostController(LocalContext.current))
    }
}