package de.madmuc.parkmuc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.jordond.compass.Location
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

// https://maplibre.org/maplibre-compose/
@Composable
fun App() {
    var userLocation by remember { mutableStateOf<Pair<Double, Double>?>( Pair(48.1373, 11.5753)) }
    var test by remember { mutableStateOf<Double?>(null) }

    val geolocator: Geolocator = Geolocator.mobile()

    LaunchedEffect(Unit) {
        val result: Location? = geolocator.current(Priority.HighAccuracy).getOrNull()
        if (result != null) {
            println("TEST"+result.accuracy)
            userLocation = Pair(result.coordinates.latitude, result.coordinates.longitude)
        } else {
            println("Geolocation failed")
        }
    }

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
            ) {
                val markerSource = rememberGeoJsonSource(
                    GeoJsonData.JsonString(
                        """
                        {
                          "type": "Feature",
                          "geometry": {
                            "type": "Point",
                            "coordinates": [${userLocation?.second}, ${userLocation?.first}]
                          },
                          "properties": {
                            "name": "Marienplatz"
                          }
                        }
                        """
                    )
                )

                CircleLayer(
                    id = "user",
                    source = markerSource,
                    color = const(Color(0xFFFFE222)),
                    radius = const(10.dp)
                )
            }

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
