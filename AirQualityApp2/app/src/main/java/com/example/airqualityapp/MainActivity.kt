package com.example.airqualityapp
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
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
import com.example.airqualityapp.screens.SplashScreen
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.utils.getBackgroundGradient
import org.osmdroid.config.Configuration.getInstance


class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_DEVICE_PRIVATE_BASE)

        val sensor1 = sensorList[0]
        sensorManager.registerListener(sds011listener, sensor1, SensorManager.SENSOR_DELAY_NORMAL)
        val sensor2 = sensorList[1]
        sensorManager.registerListener(mq9listener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
        val sensor3 = sensorList[2]
        sensorManager.registerListener(dht11listener, sensor3, SensorManager.SENSOR_DELAY_NORMAL)

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContent {
            AirQualityAppTheme {
                AirQualityApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sds011listener)
        sensorManager.unregisterListener(mq9listener)
        sensorManager.unregisterListener(dht11listener)
    }

    private val sds011listener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // The value of the first subscript in the values array is the current light intensity
            val value = event.values[0]
            Log.d("SDS011", "Value: $value")
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
    }

    private val mq9listener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // The value of the first subscript in the values array is the current light intensity
            val value = event.values[0]
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
    }

    private val dht11listener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // The value of the first subscript in the values array is the current light intensity
            val value = event.values[0]
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
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
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        containerColor = Color.Black.copy(alpha = 0.4f), // Transparência acinzentada no fundo do Scaffold
        topBar = {
            if(currentRoute != "splash") TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundGradient),
                containerColor = Color.Black.copy(alpha = 0.2f), // Transparente e acinzentado no fundo das abas
                contentColor = Color.White,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(4.dp),
                        color = Color.White
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, color = Color.White) },
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
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("home") { HomeScreen() }
            composable("map") { MapScreen("AirQualityMap") }
            composable("faq") { FaqScreen() }
        }
    }
//    if (currentRoute != "splash") {
//
//    } else {
//        SplashScreen(navController)
//    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    AirQualityAppTheme {
        AirQualityApp()
    }
}




