package com.example.airqualityapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.utils.AirQualitySummary
import com.example.airqualityapp.utils.SensorCard
import com.example.airqualityapp.utils.SensorCardDualValues
import com.example.airqualityapp.utils.getBackgroundGradient
import com.example.airqualityapp.utils.validateDataSensors
import java.time.LocalTime

data class DHT11(
    val temperature: Float = 0.0f,
    val humidity: Float = 0.0f,
)

data class SDS011(
    val pm25: Float = 0.0f,
    val pm10: Float = 0.0f,
)

data class MQ9(
    val carbonMonoxide: Float = 0.0f,
)

data class Sensors(
    val dht11: DHT11,
    val sds011: SDS011,
    val mq9: MQ9,
)

@Composable
fun HomeScreen(currentTime: LocalTime = LocalTime.now()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getBackgroundGradient(currentTime))
            .padding(0.dp)
    ) {
        // Instâncias dos sensores com valores em Float
        val dht11 = DHT11(temperature = 30.0f, humidity = 30.0f)
        val sds011 = SDS011(pm25 = 20.0f, pm10 = 70.0f)
        val mq9 = MQ9(carbonMonoxide = 9f)
        val dataSensors = Sensors(dht11, sds011, mq9)
        val validatedData = validateDataSensors(dataSensors).sortedBy { it.priority }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AirQualitySummary(city = "São Paulo", state = "SP", pm25 = sds011.pm25, pm10 = sds011.pm10, co = mq9.carbonMonoxide)
            }

            items(validatedData) {sensorData ->
                when (val sensor = sensorData.sensor) {
                    is DHT11 -> {
                        SensorCardDualValues(
                            sensorName = "DHT11",
                            value1Title = "Temperatura",
                            value1 = sensor.temperature,
                            value1Unit = "°C",
                            value2Title = "Umidade",
                            value2 = sensor.humidity,
                            value2Unit = "%",
                            extraInfo = sensorData.extraInfo
                        )
                    }

                    is SDS011 -> {
                        SensorCardDualValues(
                            sensorName = "SDS011",
                            value1Title = "PM2.5",
                            value1 = sensor.pm25,
                            value1Unit = "µg/m³",
                            value2Title = "PM10",
                            value2 = sensor.pm10,
                            value2Unit = "µg/m³",
                            extraInfo = sensorData.extraInfo
                        )
                    }

                    is MQ9 -> {
                        SensorCard(
                            "Monóxido de Carbono",
                            "${sensor.carbonMonoxide} ppm",
                            "MQ9", extraInfo = sensorData.extraInfo)
                    }
                }
            }
            item {
                Box(modifier = Modifier.padding(top = 20.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ValueInputScreenPreview1() {
    AirQualityAppTheme {
        HomeScreen(LocalTime.of(8, 0))
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputScreenPreview2() {
    AirQualityAppTheme {
        HomeScreen(LocalTime.of(16, 0))
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputScreenPreview3() {
    AirQualityAppTheme {
        HomeScreen(LocalTime.of(19, 0))
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputScreenPreview4() {
    AirQualityAppTheme {
        HomeScreen(LocalTime.of(0, 0))
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputScreenPreview5() {
    AirQualityAppTheme {
        HomeScreen(LocalTime.of(7, 0))
    }
}
