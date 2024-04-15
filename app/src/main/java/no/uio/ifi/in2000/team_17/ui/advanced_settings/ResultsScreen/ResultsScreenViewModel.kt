package no.uio.ifi.in2000.team_17.ui.advanced_settings.ResultsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team17.AdvancedSettings
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team_17.data.AdvancedSettingsRepository
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.SettingsRepository
import no.uio.ifi.in2000.team_17.model.WeatherPointsResults

data class ResultsScreenUiState(
    val weatherPointsResult: WeatherPointsResults = WeatherPointsResults(),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = true,
    val updated: String = "00"
)

class ResultsScreenViewModel(
    private val repository: Repository,
    private val settingsRepository: SettingsRepository,
    private val advancedSettingsRepository: AdvancedSettingsRepository
) : ViewModel() {

    val resultsScreenUiState: StateFlow<ResultsScreenUiState> = combine(
        repository.getWeatherPointsResults(),
        settingsRepository.settingsFlow,
        advancedSettingsRepository.advancedSettingsFlow,
        repository.updatedAt
    ) { weatherPointsResult: WeatherPointsResults, settings: Settings, advancedSettings: AdvancedSettings, updatedAt: String ->
        return@combine ResultsScreenUiState(
            weatherPointsResult = weatherPointsResult,
            latLng = LatLng(settings.lat, settings.lng),
            maxHeight = settings.maxHeight,
            updated = updatedAt
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResultsScreenUiState()
    )

    init {
        viewModelScope.launch {
            resultsScreenUiState.collect { uiState ->
                repository.load(uiState.latLng, uiState.maxHeight)
            }
        }
    }
}