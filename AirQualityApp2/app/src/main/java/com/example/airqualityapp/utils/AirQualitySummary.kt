package com.example.airqualityapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class QualityStatus(
    val status: String,
    val color: Color
)

@Composable
fun airQualityStatus(averagePm: Float): QualityStatus {
    val (statusText, statusColor) = when {
        averagePm <= 40 -> "Bom" to Color(0xFF4CAF50)
        averagePm <= 80 -> "Moderada" to Color(0xFFFFEB3B)
        averagePm <= 120 -> "Ruim" to Color(0xFFFF9800)
        averagePm <= 200 -> "Muito Ruim" to Color(0xFFF44336)
        else -> "Péssima" to Color(0xFF990066)
    }
    return QualityStatus(status = statusText, color = statusColor)
}

@Composable
fun AirQualitySummary(city: String, state: String, pm25: Float, pm10: Float) {
    val currentTime = remember { mutableStateOf(LocalTime.now()) }
    val average = (pm25 + pm10) / 2
    val airQualityStatus = airQualityStatus(average)

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime.value = LocalTime.now()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(top = 20.dp)
        ,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Qualidade do Ar Agora",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$city/$state",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
                Text(
                    text = currentTime.value.format(DateTimeFormatter.ofPattern(" - HH:mm:ss")),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
            }
            Text(
                text = "${"%.1f".format(average)} µg/m³",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Qualidade: ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White, fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .background(airQualityStatus.color, shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = airQualityStatus.status,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputSummaryPreview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            AirQualitySummary("São Paulo", "SP", 100.0f, 200.0f)
        }
    }
}