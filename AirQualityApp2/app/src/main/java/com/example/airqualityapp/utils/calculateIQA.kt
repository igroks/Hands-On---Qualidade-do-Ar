package com.example.airqualityapp.utils


import kotlin.math.*

data class PollutantRange(
    val cLow: Float, val cHigh: Float,
    val iLow: Int, val iHigh: Int
)

// Tabela de faixas para cada poluente
val mp10Ranges = listOf(
    PollutantRange(0.0f, 50.0f, 0, 40),
    PollutantRange(50.1f, 100.0f, 41, 80),
    PollutantRange(100.1f, 150.0f, 81, 120),
    PollutantRange(150.1f, 250.0f, 121, 200),
    PollutantRange(250.1f, Float.MAX_VALUE, 201, 300)
)

val mp25Ranges = listOf(
    PollutantRange(0.0f, 25.0f, 0, 40),
    PollutantRange(25.1f, 50.0f, 41, 80),
    PollutantRange(50.1f, 75.0f, 81, 120),
    PollutantRange(75.1f, 125.0f, 121, 200),
    PollutantRange(125.1f, Float.MAX_VALUE, 201, 300)
)

val coRanges = listOf(
    PollutantRange(0.0f, 9.0f, 0, 40),
    PollutantRange(9.1f, 11.0f, 41, 80),
    PollutantRange(11.1f, 13.0f, 81, 120),
    PollutantRange(13.1f, 15.0f, 121, 200),
    PollutantRange(15.1f, Float.MAX_VALUE, 201, 300)
)

// Função de interpolação linear
fun calculatePollutantIQA(value: Float, ranges: List<PollutantRange>): Int {
    val range = ranges.find { value in it.cLow .. it.cHigh } ?: return 0
    return (((range.iHigh - range.iLow) / (range.cHigh - range.cLow)) * (value - range.cLow) + range.iLow).toInt()
}

// Função principal para calcular o IQA total
fun calculateIQA(pm10: Float, pm25: Float, co: Float): Int {
    val iqaMp10 = calculatePollutantIQA(pm10, mp10Ranges)
    val iqaMp25 = calculatePollutantIQA(pm25, mp25Ranges)
    val iqaCo = calculatePollutantIQA(co, coRanges)
    return maxOf(iqaMp10, iqaMp25, iqaCo)
}

fun calculateHeatIndex(temperature: Float, humidity: Float): Double {
    // Fórmula simplificada do Heat Index (válida para temperaturas ≥ 27°C)
    return if (temperature >= 27) {
        -8.784695 + 1.61139411 * temperature + 2.338549 * humidity - 0.14611605 * temperature * humidity -
                0.012308094 * temperature.pow(2) - 0.016424828 * humidity.pow(2) +
                0.002211732 * temperature.pow(2) * humidity + 0.00072546 * temperature * humidity.pow(2) -
                0.000003582 * temperature.pow(2) * humidity.pow(2)
    } else {
        temperature.toDouble() // Para temperaturas menores, o HI é a própria temperatura.
    }
}

fun calculateWindChill(temperature: Float, windSpeed: Double = 3.0): Double {
    // Fórmula do Wind Chill (válida para temperaturas ≤ 10°C e ventos ≥ 4.8 km/h)
    return if (temperature <= 10 && windSpeed >= 4.8) {
        13.12 + 0.6215 * temperature - 11.37 * windSpeed.pow(0.16) + 0.3965 * temperature * windSpeed.pow(0.16)
    } else {
        temperature.toDouble() // Sem efeito de resfriamento para temperaturas altas ou ventos fracos.
    }
}

fun calculateDewPoint(temperature: Float, humidity: Float): Double {
    // Fórmula de Magnus-Tetens para o ponto de orvalho
    val a = 17.27
    val b = 237.7
    val alpha = ((a * temperature) / (b + temperature)) + ln(humidity / 100)
    return (b * alpha) / (a - alpha)
}
