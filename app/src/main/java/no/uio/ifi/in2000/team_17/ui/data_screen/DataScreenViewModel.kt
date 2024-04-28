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
import no.uio.ifi.in2000.team_17.usecases.SaveTimeUseCase

data class DataScreenUiState(
    val weatherDataLists: WeatherDataLists = WeatherDataLists(),
    val thresholds: Thresholds = ThresholdsSerializer.defaultValue,
    val selectedTimeIndex: Int = 0,
    val hasDissmissed: Boolean = false,

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
        DataScreenUiState(
            weatherDataList,
            thresholds,
            SaveTimeUseCase.timeStringToIndex(settings.time),
            settings.hasDismissed
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DataScreenUiState()
    )

    fun setTimeIndex(index: Int) {
        viewModelScope.launch { settingsRepo.setTime(SaveTimeUseCase.timeIndexToString(index)) }
    }
    fun dontShowAgain(){
        viewModelScope.launch { settingsRepo.setHasDissmissed(true) }
    }
}