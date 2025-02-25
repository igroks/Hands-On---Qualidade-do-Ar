package com.example.airqualityapp
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.airqualityapp.screens.FaqScreen
import com.example.airqualityapp.screens.HomeScreen
import com.example.airqualityapp.screens.MapScreen
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.utils.getBackgroundGradient

class MainActivity : ComponentActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Infla o layout XML da Splash
        val splashView = layoutInflater.inflate(R.layout.activity_splash_screen, null)
        setContentView(splashView)

        // 2. Aguarda um tempo (ou carregamento de dados) e depois carrega o Compose
        window.decorView.postDelayed({
            setContent {
                AirQualityAppTheme {
                    AirQualityApp()
                }

            }
        }, 3000)
    }
}

@Composable
fun AirQualityApp() {
    val navController = rememberNavController()
    val tabs = listOf("Medidores", "Mapa", "Dúvidas")
    val routes = listOf("home", "map", "faq")

    // Estado para aba selecionada com base na rota atual
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"
    val selectedTabIndex = routes.indexOf(currentRoute).coerceAtLeast(0)
    val backgroundGradient = getBackgroundGradient()
    Scaffold(
        modifier = Modifier.fillMaxSize().background(backgroundGradient),
        containerColor = Color.Black.copy(alpha = 0.4f), // Transparência acinzentada no fundo do Scaffold
        topBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth().background(backgroundGradient),
                containerColor = Color.Black.copy(alpha = 0.2f), // Transparente e acinzentado no fundo das abas
                contentColor = Color.White, // Cor do texto e da indicação da aba
                indicator = {
                    tabPositions -> SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(4.dp),
                    color = Color.White
                )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, color = Color.White) }, // Texto branco nas abas
                        selected = selectedTabIndex == index,
                        onClick = {
                            val targetRoute = routes[index]
                            if (currentRoute != targetRoute) {
                                navController.navigate(targetRoute) {
                                    popUpTo("home") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("map") { MapScreen() }
            composable("faq") { FaqScreen() }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    AirQualityAppTheme {
        AirQualityApp()
    }
}




