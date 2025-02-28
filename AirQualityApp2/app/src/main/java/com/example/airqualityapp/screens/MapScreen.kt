package com.example.airqualityapp.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.airqualityapp.ui.theme.AirQualityAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.ArrayList
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    val ponto = GeoPoint(-3.119027, -60.021731)

                    val manausLat = -3.109027
                    val manausLon = -59.959999

                    val coords = ArrayList<GeoPoint>()

                    val random = Random

                    val range = 0.05

                    for (i in 0..50) {
                        val randomLat: Double = manausLat + (random.nextDouble() * 2 - 1) * range
                        val randomLon: Double = manausLon + (random.nextDouble() * 2 - 1) * range
                        coords.add(GeoPoint(randomLat, randomLon))
                    }

                    controller.setZoom(12.5)
                    controller.setCenter(ponto)

                    for ((a, i) in coords.withIndex()) {
                        val marker = Marker(this)
                        marker.position = i
                        marker.title = "Risco de Perigo $a"
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        this.overlays.add(marker)
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
        Button(onClick = {
            val database = Firebase.firestore

            val sensor = hashMapOf(
                "pm_25" to 1.6,
                "sm_22" to 0.25,
                "isValid" to true
            )

            database.collection("sensor").document("dia1").set(sensor).addOnSuccessListener {
                Log.d("Firebase","Documento adicionado com sucesso")
            }.addOnFailureListener { e ->
                Log.w("Firebase", "Erro ao escrever documento", e)
            }
        }) {
            Text("Oi")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapPreview() {
    AirQualityAppTheme {
        MapScreen("AirQualityMap")
    }
}
