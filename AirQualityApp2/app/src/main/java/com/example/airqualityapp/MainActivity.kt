package com.example.airqualityapp
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.airqualityapp.ui.theme.AirQualityAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirQualityAppTheme {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }, 3000)
            }

        }
    }
}
