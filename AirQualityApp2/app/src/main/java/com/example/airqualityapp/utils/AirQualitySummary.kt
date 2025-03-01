package com.example.airqualityapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.example.airqualityapp.ui.theme.*

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
    pm25: Float,
    pm10: Float,
    co: Float,
) {
    val currentTime = remember { mutableStateOf(LocalTime.now()) }
    val iqa = calculateIQA(pm10, pm25, co)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp).padding(top = 16.dp)
                    .border(
                        width = 3.dp, // Largura da borda
                        color = airQualityStatus.color, // Cor sólida da borda (altere conforme necessário)
                        shape = RoundedCornerShape(16.dp) // Mesma curvatura do Card
                    )
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Transparent,
                        spotColor = Color.Transparent
                    )
                ,
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, focusedElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x33212121) // Cor preta com transparência (80% opacidade)
                )
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Uma descrição do IQA",
                    style = MaterialTheme.typography.bodySmall
                        .copy(
                        color = Color.White // Cor do texto alterada para branco
                    )
                )
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
            AirQualitySummary("São Paulo", "SP", 15.0f, 40.0f, 9.0f)
        }
    }
}