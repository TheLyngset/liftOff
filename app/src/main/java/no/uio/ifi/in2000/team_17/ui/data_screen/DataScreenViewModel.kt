package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.settings.SettingsRepository
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsRepository
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.SaveTimeUseCase
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase

data class DataScreenUiState(
    val weatherDataLists: WeatherDataLists = WeatherDataLists(),
    val thresholds: Thresholds = ThresholdsSerializer.defaultValue,
    val selectedTimeIndex: Int = 0,
    val showGraphTutorial: Boolean = true,
    val showTableTutorial: Boolean = true,
    val launchWindows: List<Int> = listOf()
)

class DataScreenViewModel(
    private val repo: Repository,
    private val settingsRepo: SettingsRepository,
    private val thresholdsRepository: ThresholdsRepository
) : ViewModel() {
    val uiState: StateFlow<DataScreenUiState> = combine(
        repo.weatherDataList,
        settingsRepo.settingsFlow,
        thresholdsRepository.thresholdsFlow
    ) { weatherDataList: WeatherDataLists, settings: Settings, thresholds: Thresholds ->
        val size = weatherDataList.time.size
        val launchWindows = (0..<size).filter {
            WeatherUseCase.canLaunch(weatherDataList.get(it), thresholds) != TrafficLightColor.RED
        }
        DataScreenUiState(
            weatherDataList,
            thresholds,
            SaveTimeUseCase.timeStringToIndex(settings.time),
            settings.graphShowTutorial, settings.tableShowTutorial,
            launchWindows
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DataScreenUiState()
    )

    fun setTimeIndex(index: Int) {
        viewModelScope.launch { settingsRepo.setTime(SaveTimeUseCase.timeIndexToString(index)) }
    }

    fun dontShowTableTurotialAgain() {
        viewModelScope.launch { settingsRepo.setTableShowTutorial(false) }
    }

    fun dontShowGraphTurotialAgain() {
        viewModelScope.launch { settingsRepo.setGraphShowTutorial(false) }
    }

    //This function is useful when user-testing if we want to show the tutorial again
    fun resetShowTutorial() {
        viewModelScope.launch {
            settingsRepo.setGraphShowTutorial(true)
            settingsRepo.setTableShowTutorial(true)
        }
    }
}