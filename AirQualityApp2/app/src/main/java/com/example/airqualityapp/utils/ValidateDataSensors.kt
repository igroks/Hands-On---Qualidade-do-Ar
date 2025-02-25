package com.example.airqualityapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.airqualityapp.screens.Sensors
import com.example.airqualityapp.ui.theme.AirQualityAppTheme

data class ValidatedData<T>(
    val sensor: T,
    val priority: Int = 0,
    val extraInfo: List<String> = emptyList()
)

@Composable
fun validateDataSensors(sensors: Sensors): List<ValidatedData<*>> {
    val dhtPriority = if (sensors.dht11.temperature > 30f) 1 else 3
    val sdsPriority = if (sensors.sds011.pm25 > 50f) 1 else 2
    val mq9Priority = if (sensors.mq9.carbonMonoxide > 10f) 1 else 4

    return listOf(
        ValidatedData(
            sensor = sensors.dht11,
            priority = dhtPriority,
            extraInfo = listOf("Sensor de temperatura e umidade")
        ),
        ValidatedData(
            sensor = sensors.sds011,
            priority = sdsPriority,
            extraInfo = listOf("Monitoramento de partículas PM2.5 e PM10")
        ),
        ValidatedData(
            sensor = sensors.mq9,
            priority = mq9Priority,
            extraInfo = listOf("Detecção de monóxido de carbono")
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ExtraInfoValidationPreview() {
    AirQualityAppTheme {
        //validateDataSensors()
    }
}
