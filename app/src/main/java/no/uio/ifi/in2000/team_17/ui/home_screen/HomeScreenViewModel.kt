package no.uio.ifi.in2000.team_17.ui.home_screen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsRepository
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.settings.SettingsRepository
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.usecases.SaveTimeUseCase

data class HomeScreenUiState(
    val weatherPointInTime: WeatherPointInTime = WeatherPointInTime(),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: TrafficLightColor = TrafficLightColor.WHITE,
    val updated: String = "00"
)

enum class TrafficLightColor(val color: Color, val description : String, val image : Int) {
    RED (Color(0x88FF8282), "Hold off for now!", R.drawable.redlight),
    YELLOW(Color(0x88ffde38), "Proceed with caution!", R.drawable.yellowlight),
    GREEN(Color(0x8876ff5e), "You're good to go!", R.drawable.greenlight),
    WHITE(Color(0x00ffffff), "", 0)
}

class HomeScreenViewModel(
    private val repository: Repository,
    private val settingsRepository: SettingsRepository,
    private val thresholdsRepository: ThresholdsRepository) : ViewModel() {

    val homeScreenUiState: StateFlow<HomeScreenUiState> = combine(
        repository.weatherDataList,
        settingsRepository.settingsFlow,
        thresholdsRepository.thresholdsFlow,
    ){weatherDataList: WeatherDataLists, settings: Settings, thresholds: Thresholds->
        repository.load(LatLng(settings.lat, settings.lng), settings.maxHeight)
        val weatherPointInTime = weatherDataList.get(SaveTimeUseCase.timeStringToIndex(settings.time))
        HomeScreenUiState(
            weatherPointInTime = weatherPointInTime,
            latLng = LatLng(settings.lat, settings.lng),
            maxHeight = settings.maxHeight,
            canLaunch = WeatherUseCase.canLaunch(weatherPointInTime, thresholds)
            ,
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
}