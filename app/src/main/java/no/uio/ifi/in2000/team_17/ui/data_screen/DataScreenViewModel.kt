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
import no.uio.ifi.in2000.team_17.usecases.CalculateTimeIndexUseCase
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase


/**
 * The UI state for the DataScreen
 *
 * @property weatherDataLists The weather data lists
 * @property thresholds The user defined thresholds
 * @property selectedTimeIndex The selected time index
 * @property showGraphTutorial Whether to show the graph tutorial, switched off permanently if wanted
 * @property showTableTutorial Whether to show the table tutorial, switched off permanently if wanted
 * @property graphBackgroundSwitch Whether to show the colorful graph background
 * @property launchWindows The list of time indices where the weather data is safe to launch
 */
data class DataScreenUiState(
    val weatherDataLists: WeatherDataLists = WeatherDataLists(),
    val thresholds: Thresholds = ThresholdsSerializer.defaultValue,
    val selectedTimeIndex: Int = 0,
    val showGraphTutorial: Boolean = true,
    val showTableTutorial: Boolean = true,
    val graphBackgroundSwitch: Boolean = true,
    val launchWindows: List<Int> = listOf()
)

/**
 * The ViewModel for the DataScreen
 *
 * Exposes the UI state for the DataScreen, and provides methods for updating the state
 *
 * @property repo The repository for weather data
 * @property settingsRepo The repository for settings
 * @property thresholdsRepository The repository for thresholds
 */
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
            CalculateTimeIndexUseCase.timeStringToIndex(settings.time, weatherDataList),
            settings.graphShowTutorial, settings.tableShowTutorial,
            settings.graphBackgroundSwitch,
            launchWindows
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DataScreenUiState()
    )

    fun setTimeIndex(index: Int) {
        viewModelScope.launch {
            settingsRepo.setTime(
                uiState.value.weatherDataLists.dateTime[index]
            )
        }
    }

    fun dontShowTableTutorialAgain() {
        viewModelScope.launch { settingsRepo.setTableShowTutorial(false) }
    }

    fun dontShowGraphTutorialAgain() {
        viewModelScope.launch { settingsRepo.setGraphShowTutorial(false) }
    }

    fun graphBackgroundSwitch(switch: Boolean) {
        viewModelScope.launch { settingsRepo.setGraphBackgroundSwitch(switch) }
    }

     /**
     * Resets the show tutorial flags to true
     *
     * This is used to reset the show tutorial flags to true, so that the tutorials will be shown again. Primarily used for testing.
     */
    fun resetShowTutorial() {
        viewModelScope.launch {
            settingsRepo.setGraphShowTutorial(true)
            settingsRepo.setTableShowTutorial(true)
        }
    }
}