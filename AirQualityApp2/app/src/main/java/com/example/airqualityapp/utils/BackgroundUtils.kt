// BackgroundUtils.kt
package com.example.airqualityapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import java.time.LocalTime

@Composable
fun getBackgroundGradient(currentTime: LocalTime = LocalTime.now()): Brush {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.toFloat()

    val startY = screenHeight * 0.10f  // Início do degradê a 10% da altura da tela
    val endY = screenHeight * 1.70f    // Fim do degradê a 170% da altura da tela

    return when {
        currentTime.isAfter(LocalTime.of(5, 30)) && currentTime.isBefore(LocalTime.of(7, 1)) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF3F51B5),
                    Color(0xFF2F3B81),
                    Color(0xFF1D244F)
                ),
                startY = startY,
                endY = endY
            )
        }
        currentTime.isAfter(LocalTime.of(7, 0)) && currentTime.isBefore(LocalTime.of(15, 1)) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF4EA4D2),
                    Color(0xFF3A69A8),
                    Color(0xFF3F51B5)
                ),
                startY = startY,
                endY = endY
            )
        }
        currentTime.isAfter(LocalTime.of(15, 0)) && currentTime.isBefore(LocalTime.of(17, 41)) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF7536A2),
                    Color(0xFFFF4500),

                ),
                startY = startY,
                endY = endY
            )
        }
        currentTime.isAfter(LocalTime.of(17, 40)) && currentTime.isBefore(LocalTime.of(19, 1)) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF191970),
                    Color(0xFF483D8B),
                    Color(0xFF7536A2)
                ),
                startY = startY,
                endY = endY
            )
        }
        else -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0E0E19),
                    Color(0xFF0E0E26),
                    Color(0xFF0E0E41),
                ),
                startY = startY,
                endY = endY
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Background1Preview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundGradient(LocalTime.of(7, 0)))
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Background2Preview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundGradient(LocalTime.of(8, 0)))
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Background3Preview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundGradient(LocalTime.of(16, 0)))
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Background4Preview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundGradient(LocalTime.of(19, 0)))
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Background5Preview() {
    AirQualityAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundGradient(LocalTime.of(0, 0)))
                .padding(4.dp)
        )
    }
}
