package com.example.airqualityapp.utils.cards

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.example.airqualityapp.ui.theme.LightGrayTranslucent
import com.example.airqualityapp.ui.theme.MediumGreen
import com.example.airqualityapp.ui.theme.lightBackTranslucent
import com.example.airqualityapp.utils.IQA_Range

// Função para obter a preferência salva
fun getDetailsCardPreference(context: Context): Boolean {
    val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean("detailsCardPreference", false) // Padrão: fechado
}

// Função para salvar a preferência do usuário
fun saveDetailsCardPreference(context: Context, expanded: Boolean) {
    val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean("detailsCardPreference", expanded).apply()
}
@Composable
fun ResumeCard(
    airQualityStatus: QualityStatus,
    expanded: Boolean = false,
    modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isExpanded by remember {
        if (expanded) mutableStateOf(expanded)
        else mutableStateOf(getDetailsCardPreference(context))
    }
    val resume = IQA_Range.find {it.first == airQualityStatus.status}

    val modifierExpanded = modifier
        .fillMaxWidth()
        .padding(2.dp)
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

    val modifierNotExpanded = modifier
        .fillMaxWidth()
        .padding(2.dp)
        .shadow(
            elevation = 6.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Transparent,
            spotColor = Color.Transparent
        ).clickable { saveDetailsCardPreference(context, !isExpanded); isExpanded = !isExpanded }

    Card(
        modifier = if(isExpanded) modifierExpanded else modifierNotExpanded,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            focusedElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if(isExpanded) lightBackTranslucent else LightGrayTranslucent // Cor preta com transparência (80% opacidade)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isExpanded) 0.dp else 12.dp)
                .animateContentSize(  // Anima qualquer mudança de tamanho no conteúdo
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isExpanded) {
                if (resume != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        horizontalAlignment = Alignment.Start

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 16.dp, start = 16.dp)
                                    .drawBehind {
                                        val strokeWidth = 4.dp.toPx()
                                        drawLine(
                                            color = airQualityStatus.color,
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = strokeWidth
                                        )
                                    }
                            ) {
                                Text(
                                    modifier = Modifier.padding(0.dp),
                                    text = resume.second,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Icon(
                                modifier = Modifier.padding(end = 16.dp).clickable { saveDetailsCardPreference(context, !isExpanded); isExpanded = !isExpanded },
                                imageVector =  Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                            )
                            Icons.Default.KeyboardArrowUp
                        }
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = resume.third,
                            textAlign = TextAlign.Justify,
                            style = MaterialTheme.typography.bodyMedium
                                .copy(
                                    color = Color.White // Cor do texto alterada para branco
                                )
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "Detalhes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White // Cor do texto alterada para branco
                        )
                    )
                    Icon(
                        imageVector =  Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

        }
    }

//    if (isExpanded) {
//        Card(
//            modifier = ,
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 0.dp,
//                focusedElevation = 2.dp
//            ),
//            colors = CardDefaults.cardColors(
//                containerColor = Color(0x33212121) // Cor preta com transparência (80% opacidade)
//            )
//        ) {
//            if (resume != null) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(0.dp),
//                    horizontalAlignment = Alignment.Start
//
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .padding(top = 16.dp, start = 16.dp)
//                            .drawBehind {
//                                val strokeWidth = 4.dp.toPx()
//                                drawLine(
//                                    color = airQualityStatus.color,
//                                    start = Offset(0f, size.height),
//                                    end = Offset(size.width, size.height),
//                                    strokeWidth = strokeWidth
//                                )
//                            }
//                    ) {
//                        Text(
//                            modifier = Modifier.padding(0.dp),
//                            text = resume.second,
//                            color = Color.White,
//                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
//                        )
//                    }
//                    Text(
//                        modifier = Modifier.padding(16.dp),
//                        text = resume.third,
//                        textAlign = TextAlign.Justify,
//                        style = MaterialTheme.typography.bodyMedium
//                            .copy(
//                                color = Color.White // Cor do texto alterada para branco
//                            )
//                    )
//                }
//            }
//        }
    //} else {

    //}
}

@Preview(showBackground = true)
@Composable
fun ResumeCardPreview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            ResumeCard(QualityStatus("Boa", MediumGreen))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResumeCardExpandedPreview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            ResumeCard(QualityStatus("Boa", MediumGreen), true)
        }
    }
}
