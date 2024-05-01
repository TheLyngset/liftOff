package no.uio.ifi.in2000.team_17.ui.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.settings.SettingsRepository

data class SplashScreenUiState(
    val isLoading: Boolean = true,
    val hasData: Boolean = false,
    val hasIsobaric: Boolean = false,
    val hasLocationforecast: Boolean = false
)
class SplashScreenViewModel(
    private val repository: Repository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    private  val isLoading = _isLoading.asStateFlow()

    val uiState: StateFlow<SplashScreenUiState> = combine(
        repository.hasLocationForecastData,
        repository.hasIsoBaricData,
        settingsRepository.settingsFlow,
        isLoading
    ){hasLocationForecast: Boolean, hasIsobaric: Boolean, settings: Settings, isLoading: Boolean ->

        repository.load(LatLng(settings.lat, settings.lng), settings.maxHeight)

        SplashScreenUiState(
            isLoading,
            hasLocationForecast && hasIsobaric,
            hasIsobaric,
            hasLocationForecast
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SplashScreenUiState()
    )
    init {
        viewModelScope.launch {
            delay(4000)
            _isLoading.update { false }
        }
    }
}