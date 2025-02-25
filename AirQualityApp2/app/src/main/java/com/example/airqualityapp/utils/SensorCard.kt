package com.example.airqualityapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme

@Composable
fun SensorCard(title: String, value: String, sensorName: String, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) } // Estado de expansão do card

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(
                min = 100.dp,
                max = if (isExpanded) 250.dp else 170.dp)  // Altera a altura com base na expansão
            .padding(2.dp)
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
            containerColor = Color(0x43B3B3B3) // Cor preta com transparência (80% opacidade)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Sensor: $sensorName",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Cor do texto alterada para branco
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White) // Texto em branco
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White) // Texto em branco
            )

            // Texto + Seta para expandir/contrair
            Spacer(modifier = Modifier.weight(1f))  // Para empurrar a seta para o final
            Text(
                text = "Mostrar mais ▼",
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                )
            )

            // Informações adicionais
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Informações adicionais sobre o sensor.", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                    Text("Mais dados técnicos do sensor podem ser exibidos aqui.", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputCardPreview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            SensorCard("Sensor 1", "Valor 1", "Sensor 1")
        }
    }
}
