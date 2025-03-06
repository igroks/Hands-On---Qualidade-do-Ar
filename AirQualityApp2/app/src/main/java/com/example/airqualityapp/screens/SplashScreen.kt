package com.example.airqualityapp.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airqualityapp.R
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.utils.getBackgroundGradient
import kotlinx.coroutines.delay

// Abaixo segue o composable caso seja criado a splash screen a partir de uma classe. A splash
// screen atual está sendo chamada direto da pasta layout. Ela é montada a partir do arquivo
// splash_screen.xml.
@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("prefs", Context.MODE_PRIVATE) }
    val onboardingCompleted = sharedPrefs.getBoolean("onboarding_completed", false)

    LaunchedEffect(Unit) {
        delay(5000L) // Tempo de exibição da SplashScreen (2 segundos)
        if (onboardingCompleted) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true } // Remove a SplashScreen da pilha
            }
        } else {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(getBackgroundGradient())
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Air Quality",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
        )
        Image(
            painter = painterResource(id = R.drawable.splash_air_quality_character2), // Substitua pelo nome da sua imagem
            contentDescription = "Splash Screen",
            modifier = Modifier.fillMaxWidth() // Ajuste o tamanho conforme necessário
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