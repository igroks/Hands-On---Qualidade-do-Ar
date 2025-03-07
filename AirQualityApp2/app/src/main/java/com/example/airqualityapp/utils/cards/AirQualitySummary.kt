package com.example.airqualityapp.utils.cards

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.airqualityapp.ui.theme.*
import com.example.airqualityapp.utils.calculateIQA

data class QualityStatus(
    val status: String,
    val color: Color
)

@Composable
fun airQualityStatus(value: Int): QualityStatus {
    val (statusText, statusColor) = when {
        value <= 40 -> "Boa" to MediumGreen
        value <= 80 -> "Moderada" to GoldenYellow
        value <= 120 -> "Ruim" to VividOrange
        value <= 200 -> "Péssima" to IntenseRed
        else -> "Crítica" to WinePurple
    }
    return QualityStatus(status = statusText, color = statusColor)
}

@Composable
fun AirQualitySummary(
    city: String,
    state: String,
    iqa: Int = 0,
) {
    val currentTime = remember { mutableStateOf(LocalTime.now()) }
    val airQualityStatus = airQualityStatus(iqa)

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime.value = LocalTime.now()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)
            .padding(top = 40.dp)
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
                text = "IQA: $iqa",
                style = MaterialTheme.typography.displayMedium.copy(color = Color.White)
            )
            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Qualidade: ",
                    style = MaterialTheme.typography.bodyLarge.copy(
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
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            ResumeCard(airQualityStatus)
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
            AirQualitySummary("São Paulo", "SP")
        }
    }
}