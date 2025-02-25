package com.example.airqualityapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.utils.InfoCard
import com.example.airqualityapp.utils.getBackgroundGradient

val questions = listOf(
    "O que é o AirQuality?" to "O AirQuality é um aplicativo que monitora e exibe informações sobre a qualidade do ar em tempo real.",
    "O que são os dados na tela principal?" to "São leituras dos sensores conectados que mostram informações como PM2.5, PM10, temperatura, umidade e monóxido de carbono.",
    "Entendendo os dados do sensor SDS011" to "O sensor SDS011 mede partículas PM2.5 e PM10, onde valores menores indicam melhor qualidade do ar.",
    "Entendendo os dados do sensor MQ9" to "O sensor MQ9 detecta níveis de monóxido de carbono. Quanto menor o valor, melhor a qualidade do ar.",
    "Para quê serve o mapa do aplicativo?" to "O mapa exibe a localização dos sensores e a qualidade do ar em diferentes regiões.",
    "Recebeu uma notificação do aplicativo?" to "As notificações alertam sobre mudanças significativas na qualidade do ar ou outros eventos importantes."
)

@Composable
fun FaqScreen () {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getBackgroundGradient())
            .padding(0.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Preenche a largura disponível
                        .padding(top = 36.dp)
                ){
                Text(
                    text = "Dúvidas Frequentes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 20.dp)
                )}
            }

            itemsIndexed(questions) {index, (question, answer) ->
                InfoCard(question = question, answer = answer, index = index + 1, listState = listState)
            }
            item {
                Box(modifier = Modifier.padding(top = 20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FaqScreenPreview() {
    AirQualityAppTheme {
        FaqScreen()
    }
}