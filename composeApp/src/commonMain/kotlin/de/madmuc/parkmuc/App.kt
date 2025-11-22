package de.madmuc.parkmuc

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.TrackingStatus
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
import kotlin.time.Duration.Companion.seconds

data class ParkingFilters(
    val isFree: Boolean = false,
    val minDuration: Int = 0,
    val maxDistance: Int = 5
)

// https://maplibre.org/maplibre-compose/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var userLocation by remember { mutableStateOf<Pair<Double, Double>?>( Pair(48.1373, 11.5753)) }
    var centerd by remember {mutableStateOf<Boolean>(false)}

    val geolocator: Geolocator = remember { Geolocator.mobile() }
    var showFilter by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(ParkingFilters()) }

//    HideSystemBars()

    LaunchedEffect(geolocator) {
        geolocator.startTracking()
        geolocator.trackingStatus.collect { status ->
            when (status) {
                is TrackingStatus.Idle -> println("Not Tracking")
                is TrackingStatus.Update -> {
                    val coordinates = status.location.coordinates
                    userLocation = Pair(coordinates.latitude, coordinates.longitude)
                    println("Location updated: ${coordinates.latitude}, ${coordinates.longitude}")
                }
                is TrackingStatus.Error -> {
                    val error: GeolocatorResult.Error = status.cause
                    println("Geolocation error: $error")
                }
                else -> {}
            }
        }
    }

    DisposableEffect(geolocator) {
        onDispose {
            geolocator.stopTracking()
        }
    }

    MaterialTheme {

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
                            "name": "User Location"
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

            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                tonalElevation = 4.dp
            ) {}

            FloatingActionButton(
                onClick = {
                    centerd = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp)
            ) {
                Text("📍", style = MaterialTheme.typography.headlineSmall)
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = { showFilter = true },
                    modifier =
                        Modifier.weight(1f)
                        .padding(bottom = 32.dp)
                ) {
                    Text("Filtern")
                }
            }
        }

        if (showFilter) {
            ModalBottomSheet(
                onDismissRequest = { showFilter = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Filter", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Kostenlos")
                        Checkbox(
                            checked = filters.isFree,
                            onCheckedChange = {
                                filters = filters.copy(isFree = it)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Mindestparkdauer", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = filters.minDuration.toFloat(),
                        onValueChange = {
                            filters = filters.copy(minDuration = it.toInt())
                        },
                        valueRange = 0f..24f,
                        steps = 23,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${filters.minDuration} Stunden", style = MaterialTheme.typography.labelSmall)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Maximale Entfernung", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = filters.maxDistance.toFloat(),
                        onValueChange = {
                            filters = filters.copy(maxDistance = it.toInt())
                        },
                        valueRange = 0f..20f,
                        steps = 19,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${filters.maxDistance} km", style = MaterialTheme.typography.labelSmall)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                filters = ParkingFilters()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Zurücksetzen")
                        }

                        Button(
                            onClick = {
                                showFilter = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Anwenden")
                        }
                    }
                }
            }
        }

        if (centerd) {
            centerd = !centerd
            userLocation?.let {
                LaunchedEffect(Unit) {
                    cameraState.animateTo(
                        finalPosition =
                            cameraState.position.copy(target = Position(latitude = it.first, longitude = it.second), zoom = 14.0),
                        duration = 3.seconds,
                    )
                }
            }
        }
    }
}