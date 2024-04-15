package no.uio.ifi.in2000.team_17.ui.home_screen

import androidx.compose.runtime.collectAsState
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
import no.uio.ifi.in2000.team_17.model.Thresholds
import no.uio.ifi.in2000.team_17.model.WeatherPointOld
import kotlin.math.ln

data class HomeScreenUiState(
    val weatherPointList: List<WeatherPointOld> = listOf(WeatherPointOld()),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = true,
    val updated: String = "00"
)

class HomeScreenViewModel(private val repository: Repository, private val settingsRepository: SettingsRepository, private val advancedSettingsRepository: AdvancedSettingsRepository) : ViewModel() {
    val homeScreenUiState: StateFlow<HomeScreenUiState> = combine(
        repository.getWeatherPointList(),
        settingsRepository.settingsFlow,
        advancedSettingsRepository.advancedSettingsFlow,
        repository.updatedAt
    ){weatherPointList: List<WeatherPointOld>, settings: Settings, advancedSettings: AdvancedSettings, updatedAt:String ->
        return@combine HomeScreenUiState(
            weatherPointList = weatherPointList,
            latLng = LatLng(settings.lat, settings.lng),
            maxHeight = settings.maxHeight,
            canLaunch = WeatherUseCase.canLaunch(
                weatherPoint = weatherPointList.first(),
                maxWindSpeed = advancedSettings.maxWindSpeed,
                maxShearWind = advancedSettings.maxWindShear,
                threshholds = advancedSettings
            ),
            updated = updatedAt
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState()
    )
    fun load() {
        viewModelScope.launch {
            repository.load(homeScreenUiState.value.latLng, homeScreenUiState.value.maxHeight)
        }
    }
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
/*
    fun findMaxSpeed(): Double {
        return homeScreenUiState.value.weatherPointList.maxOf { it.windSpeed }
    }

    fun findMaxShear(): Double {
        return homeScreenUiState.value.weatherPointList.maxOf { it.windShear }

    }
*/

    init {
        viewModelScope.launch {
            repository.load(homeScreenUiState.value.latLng, homeScreenUiState.value.maxHeight)
        }
    }

/*    fun canLaunch() {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    canLaunch = WeatherUseCase.canLaunch(
                        homeScreenUiState.value.weatherPointList.first(),
                        findMaxSpeed(), findMaxShear()
                    )
                )
            }
        }
    }*/
}