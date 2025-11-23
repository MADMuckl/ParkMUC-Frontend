package de.madmuc.parkmuc

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val settingsRepository: SettingsRepository = SettingsRepository()
) : ViewModel() {

    val settings: StateFlow<Map<String, Any>> = settingsRepository.settings

    fun saveSetting(key: String, value: Any) {
        settingsRepository.saveSetting(key, value)
    }

    fun getSetting(key: String): Any? {
        return settingsRepository.getSetting(key)
    }

    fun clearSettings() {
        settingsRepository.clearSettings()
    }
}