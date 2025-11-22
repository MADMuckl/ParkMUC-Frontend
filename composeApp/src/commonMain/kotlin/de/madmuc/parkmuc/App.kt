package de.madmuc.parkmuc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

// https://maplibre.org/maplibre-compose/
@Composable
fun App() {
    MaterialTheme {
        // City Center of Munich (Marienplatz or something like that)
        val cameraState = rememberCameraState(
            CameraPosition(
                target = Position(
                    latitude = 48.1373,
                    longitude = 11.5753,
                ),
                zoom = 14.0
            )
        )

        Box(Modifier.fillMaxSize()) {
            // Dont use the ugly default map
            MaplibreMap(
                modifier = Modifier.fillMaxSize(),
                baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
                cameraState = cameraState,
            )

            // Optional overlay UI
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                tonalElevation = 4.dp
            ) {}
        }
    }
}
