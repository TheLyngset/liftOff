package no.uio.ifi.in2000.team_17.ui.advanced_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.team_17.MainActivity
import no.uio.ifi.in2000.team_17.data.Repository

data class AdvancedSettingsUiState(
    val groundWindSpeed: Double = 8.6,
    val humidity: Double = 75.0,
    val dewpoint: Double = 15.0,
    val cloudFraction: Double = 15.0,
    val rain: Double = 0.1,
    val windShear: Double = 24.5,
    val fog: Double = 0.1,
    val maxWindSpeed: Double = 17.2,
    val maxShearWind: Double = 24.5
)
class AdvancedSettingsViewModel(private val repository: Repository) : ViewModel(){
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = MainActivity.repository
                AdvancedSettingsViewModel(
                    repository = repository
                )
            }
        }
    }

    private val _advancedSettingsUiState = MutableStateFlow(AdvancedSettingsUiState())
    val advancedSettingsUiState = _advancedSettingsUiState.asStateFlow()
    fun setSettings(advancedSettingsUiState: AdvancedSettingsUiState){
        repository.setSettings(advancedSettingsUiState)
    }

}