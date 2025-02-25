package com.example.airqualityapp.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
fun SensorCard(title: String, value: String, sensorName: String, expanded: Boolean = false, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(expanded) } // Estado de expansão do card

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
                .padding(16.dp)
                .animateContentSize(  // Anima qualquer mudança de tamanho no conteúdo
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sensor: $sensorName",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White // Cor do texto alterada para branco
                    )
                )
                // Ícone
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Alerta",
                    tint = Color.Red,
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White) // Texto em branco
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White) // Texto em branco
            )

            // Texto + Seta para expandir/contrair
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
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Mostrar mais ",
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        //.align(Alignment.CenterHorizontally),
                    , style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
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

@Preview(showBackground = true)
@Composable
fun ValueInputCardExpandedPreview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            SensorCard("Sensor 1", "Valor 1", "Sensor 1", true)
        }
    }
}
