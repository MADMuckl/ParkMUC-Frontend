package de.madmuc.parkmuc

import androidx.compose.foundation.clickable
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
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

data class ParkingFilters(
    val isFree: Boolean = false,
    val minDuration: Int = 0,
    val maxDistance: Int = 5
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: SettingsViewModel
) {
    var userLocation by remember { mutableStateOf<Pair<Double, Double>?>( Pair(48.1373, 11.5753)) }
    var centerd by remember {mutableStateOf<Boolean>(false)}

    val geolocator: Geolocator = remember { Geolocator.mobile() }
    var showMenu by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(ParkingFilters()) }
    var parkingData by remember { mutableStateOf<String>("") }
    var error by remember { mutableStateOf<String?>("") }
    val client = remember { HttpClient() }


    LaunchedEffect(Unit) {
//    LaunchedEffect(userLocation) {
//        userLocation?.let { location ->
            try {
                val response: HttpResponse = client.get(
                    "http://131.159.203.21:8080/parking/segments?latitude=48.1373&longitude=11.5753&radius=500"+("&disabledParking="+ (viewModel.getSetting("hasDisabilityPass") as? Boolean ?: true))
//                    "http://131.159.203.21:8080/parking/segments?latitude=${location.second}&longitude=${location.first}&radius=500"
                )
                parkingData = response.bodyAsText()
            } catch (e: Exception) {
                e.printStackTrace()
                error = e.message
            }
    }

    LaunchedEffect(geolocator) {
        geolocator.startTracking()
        geolocator.trackingStatus.collect { status ->
            when (status) {
                is TrackingStatus.Idle -> println("Not Tracking")
                is TrackingStatus.Update -> {
                    val coordinates = status.location.coordinates
                    userLocation = Pair(coordinates.latitude, coordinates.longitude)
                }
                is TrackingStatus.Error -> {
                    val error: GeolocatorResult.Error = status.cause
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

                if (parkingData.isNotEmpty()) {
                    val parkingDataGeoJson = rememberGeoJsonSource(
                        GeoJsonData.JsonString(parkingData)
                    )

                    LineLayer(
                        id = "lines",
                        source = parkingDataGeoJson,
                        color = const(Color.Green),
                        width = const(4.dp),
                    )
                }
            }


            Button(
                onClick = { showMenu = !showMenu },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 48.dp, start = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1b98d5)
                )
            ) {
                Text(
                    "☰"
                    , style = MaterialTheme.typography.headlineSmall, color = Color.White)
            }

            if (showMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showMenu = false }
                )
            }

            if (showMenu) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .width(250.dp),
                    color = Color(0xFF005a9f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Menu",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            modifier = Modifier.padding(top = 48.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        TextButton(
                            onClick = {
                                showMenu = false
                                onNavigate(Screen.SettingsScreen)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Einstellungen", color = Color.White)
                        }
                        TextButton(
                            onClick = {
                                showMenu = false
                                onNavigate(Screen.SpecialRightsScreen)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sonderpark Rechte", color = Color.White)
                        }
//                        Text("__error:"+error, color = Color.White)
//                        Text("__debug:"+parkingData, color = Color.White)
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    centerd = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp),
                containerColor = Color(0xFF1b98d5)
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1b98d5)
                    )
                ) {
                    Text("Parkplätze", color = Color.White)
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

            LaunchedEffect(centerd, userLocation) {
                try {
                    if (centerd && userLocation != null) {
                        userLocation?.let {
                            cameraState.position = cameraState.position.copy(
                                target = Position(latitude = it.first, longitude = it.second),
                                zoom = 14.0
                            )
                        }
                        centerd = false
                    }
                } catch (e: Exception) {
                    println("Camera error: ${e.message}")
                    centerd = false
                }
            }
        }
    }
}