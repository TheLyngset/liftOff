package no.uio.ifi.in2000.team_17.ui.home_screen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsRepository
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.settings.SettingsRepository
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.usecases.SaveTimeUseCase

data class HomeScreenUiState(
    val weatherPointInTime: WeatherPointInTime = WeatherPointInTime(),
    val canLaunch: TrafficLightColor = TrafficLightColor.WHITE,
    val updated: String = "00",
    val thresholds: Thresholds = ThresholdsSerializer.defaultValue
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
    private val thresholdsRepository: ThresholdsRepository
) : ViewModel() {

    val uiState: StateFlow<HomeScreenUiState> = combine(
        repository.weatherDataList,
        thresholdsRepository.thresholdsFlow,
        settingsRepository.settingsFlow
    ){ weatherDataList: WeatherDataLists, thresholds: Thresholds, settings:Settings ->

        val weatherPointInTime = weatherDataList.get(SaveTimeUseCase.timeStringToIndex(settings.time))
        HomeScreenUiState(
            weatherPointInTime = weatherPointInTime,
            canLaunch = WeatherUseCase.canLaunch(weatherPointInTime, thresholds),
            updated = weatherDataList.updated,
            thresholds = thresholds
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState()
    )
}