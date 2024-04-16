package no.uio.ifi.in2000.team_17.ui.home_screen

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
import no.uio.ifi.in2000.team_17.data.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime

data class HomeScreenUiState(
    val weatherPointInTime: WeatherPointInTime = WeatherPointInTime(),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = true,
    val updated: String = "00"
)

class HomeScreenViewModel(private val repository: Repository, private val settingsRepository: SettingsRepository, private val advancedSettingsRepository: AdvancedSettingsRepository) : ViewModel() {
    val homeScreenUiState: StateFlow<HomeScreenUiState> = combine(
        repository.weatherDataList,
        settingsRepository.settingsFlow,
        advancedSettingsRepository.advancedSettingsFlow,
    ){weatherDataList: WeatherDataLists, settings: Settings, advancedSettings: AdvancedSettings->
        repository.load(LatLng(settings.lat, settings.lng), settings.maxHeight)
        val weatherPointInTime = weatherDataList.get(settings.timeIndex)
        HomeScreenUiState(
            weatherPointInTime = weatherPointInTime,
            latLng = LatLng(settings.lat, settings.lng),
            maxHeight = settings.maxHeight,
            canLaunch = WeatherUseCase.canLaunch(
                weatherPointInTime,
                advancedSettings
            ),
            updated = weatherDataList.updated
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState()
    )
    fun setLat(lat:Double){
        viewModelScope.launch {
            settingsRepository.setLat(lat)
        }
    }
    fun setLng(lng:Double){
        viewModelScope.launch {
            settingsRepository.setLng(lng)
        }
    }
    fun setMaxHeight(height: Int){
        viewModelScope.launch{
            settingsRepository.setMaxHeight(height)
        }
    }

    fun setTimeIndex(index: Int){
        viewModelScope.launch { settingsRepository.setTimeIndex(index) }
    }
}