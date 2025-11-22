package de.madmuc.parkmuc

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

class MapScreen {
    @Composable
    fun MunichMapScreen() {
        // Camera starting at Munich city center (Marienplatz-ish)
        val cameraState = rememberCameraState(
            firstPosition = CameraPosition(
                target = Position(
                    latitude = 48.137154,
                    longitude = 11.576124
                ),
                zoom = 13.0   // adjust as needed
            )
        )

        MaplibreMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
            // Use a free, documented OpenFreeMap style (from MapLibre docs)
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty")
        )
    }

}