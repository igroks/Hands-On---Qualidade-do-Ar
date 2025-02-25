package com.example.airqualityapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun SensorCardDualValues(
    sensorName: String,
    value1Title: String,
    value1: Float,
    value1Unit: String,
    value2Title: String,
    value2: Float,
    value2Unit: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) } // Estado de expansão do card

    Card(
        modifier = modifier
            .fillMaxWidth()
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
                .fillMaxWidth()
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),  // Garante que a altura se ajuste ao conteúdo
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Valor 1
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = value1Title, style = MaterialTheme.typography.titleMedium.copy(color = Color.White)) // Texto em branco
                    Text(text = "$value1 $value1Unit", style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)) // Texto em branco
                }

                // Linha divisória ajustada
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(color = Color.White)
                )

                // Valor 2
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = value2Title, style = MaterialTheme.typography.titleMedium.copy(color = Color.White)) // Texto em branco
                    Text(text = "$value2 $value2Unit", style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)) // Texto em branco
                }
            }

            // Texto + Seta para expandir/contrair (centralizado corretamente)
            Text(
                text = if (isExpanded) "Mostrar menos ▲" else "Mostrar mais ▼",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .clickable { isExpanded = !isExpanded },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                )
            )

            // Informações adicionais ao expandir
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Informações adicionais sobre o sensor.", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                    Text("Dados técnicos e históricos podem ser exibidos aqui.", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValueInputDualCardPreview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            SensorCardDualValues("Sensor 1", "Valor 1", 100.0f, "Km", "Valor 2", 999.0f, "Km")
        }
    }
}