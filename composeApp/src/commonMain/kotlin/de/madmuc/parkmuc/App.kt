package de.madmuc.parkmuc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

sealed class Screen {
    object MapScreen : Screen()
    object SettingsScreen : Screen()
    object SpecialRightsScreen : Screen()
}

@Composable
fun rememberNavigationState() = remember { mutableStateOf<Screen>(Screen.MapScreen) }

@Composable
fun NavigationHost(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    when (currentScreen) {
        is Screen.MapScreen -> MapScreen(onNavigate)
        is Screen.SettingsScreen -> SettingsScreen(onNavigate)
        is Screen.SpecialRightsScreen -> SpecialRightsScreen(onNavigate)
    }
}

@Composable
fun App() {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.MapScreen) }

    NavigationHost(
        currentScreen = currentScreen.value,
        onNavigate = { screen -> currentScreen.value = screen }
    )
}