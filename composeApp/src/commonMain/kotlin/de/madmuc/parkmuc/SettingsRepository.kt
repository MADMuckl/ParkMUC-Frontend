package de.madmuc.parkmuc

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsRepository {
    private val _settings = MutableStateFlow<Map<String, Any>>(emptyMap())
    val settings: StateFlow<Map<String, Any>> = _settings

    fun saveSetting(key: String, value: Any) {
        val currentSettings = _settings.value.toMutableMap()
        currentSettings[key] = value
        _settings.value = currentSettings
    }

    fun getSetting(key: String): Any? = _settings.value[key]

    fun clearSettings() {
        _settings.value = emptyMap()
    }
}