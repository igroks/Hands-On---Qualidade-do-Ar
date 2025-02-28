package com.example.airqualityapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.airqualityapp.DHT11
import com.example.airqualityapp.MQ9
import com.example.airqualityapp.SDS011
import com.example.airqualityapp.Sensors
import com.example.airqualityapp.ui.theme.AirQualityAppTheme

data class ValidatedData<T>(
    val sensor: T,
    val priority: Int = 0,
    val extraInfo: List<String> = emptyList()
)

// Níveis de prioridade
enum class Priority(val value: Int) {
    P1(5), P2(4), P3(3), P4(2), P5(1)
}

// Prioridade da sensação térmica
fun heatIndexPriority(value: Double): Priority = when {
    value < 27 -> Priority.P1
    value in 27.0..32.0 -> Priority.P2
    value in 33.0..41.0 -> Priority.P3
    value in 42.0..53.0 -> Priority.P4
    else -> Priority.P5
}

// Prioridade do resfriamento evaporativo
fun windChillPriority(value: Double): Priority = when {
    value > 10 -> Priority.P1
    value in 0.0..10.0 -> Priority.P2
    value in -10.0..0.0 -> Priority.P3
    value in -28.0..-10.0 -> Priority.P4
    else -> Priority.P5
}

// Prioridade do ponto de orvalho
fun dewPointPriority(value: Double): Priority = when {
    value < 10 -> Priority.P1
    value in 10.0..15.0 -> Priority.P2
    value in 16.0..20.0 -> Priority.P3
    value in 21.0..24.0 -> Priority.P4
    else -> Priority.P5
}

fun pm25Priority(value: Double): Priority = when (value) {
    in 0.0..25.0 -> Priority.P1
    in 26.0..50.0 -> Priority.P2
    in 51.0..75.0 -> Priority.P3
    in 76.0..125.0 -> Priority.P4
    else -> Priority.P5
}

fun pm10Priority(value: Double): Priority = when (value) {
    in 0.0..50.0 -> Priority.P1
    in 51.0..100.0 -> Priority.P2
    in 101.0..150.0 -> Priority.P3
    in 151.0..250.0 -> Priority.P4
    else -> Priority.P5
}

fun coPriority(value: Float): Priority = when (value) {
    in 0.0..9.0 -> Priority.P1
    in 10.0..11.0 -> Priority.P2
    in 12.0..13.0 -> Priority.P3
    in 14.0..15.0 -> Priority.P4
    else -> Priority.P5
}

// Determina qual dos dados do sensor tem prioridade.
fun dht11Priority(sensor: DHT11): Priority = listOf(
    heatIndexPriority(calculateHeatIndex(sensor.temperature, sensor.humidity)),
    windChillPriority(calculateWindChill(sensor.temperature)),
    dewPointPriority(calculateDewPoint(sensor.temperature, sensor.humidity))
).minBy { it.value }

fun sdsPriority(sensor: SDS011): Priority = listOf(
    pm25Priority(sensor.pm25.toDouble()),
    pm10Priority(sensor.pm10.toDouble())
).minBy { it.value }

fun mq9Priority(sensor: MQ9): Priority = coPriority(sensor.carbonMonoxide)

@Composable
fun validateDataSensors(sensors: Sensors): List<ValidatedData<*>> {
    val dhtPriority = dht11Priority(sensors.dht11)
    val sdsPriority = sdsPriority(sensors.sds011)
    val mq9Priority = mq9Priority(sensors.mq9)

    return listOf(
        ValidatedData(
            sensor = sensors.dht11,
            priority = dhtPriority.value,
            extraInfo = listOf("Sensor de temperatura e umidade", "${dhtPriority.value}")
        ),
        ValidatedData(
            sensor = sensors.sds011,
            priority = sdsPriority.value,
            extraInfo = listOf("Monitoramento de partículas PM2.5 e PM10", "${sdsPriority.value}")
        ),
        ValidatedData(
            sensor = sensors.mq9,
            priority = mq9Priority.value,
            extraInfo = listOf("Detecção de monóxido de carbono", "${mq9Priority.value}")
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
