package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenUiState

data class DataScreenUiState(
    val weatherDataLists: WeatherDataLists = WeatherDataLists(),
    val advancedSettings: AdvancedSettings = AdvancedSettings.getDefaultInstance(),
    val selectedTimeIndex: Int = 0
)
class DataScreenViewModel(
    private val repo:Repository,
    private val settingsRepo: SettingsRepository,
    private val advancedSettingsRepo: AdvancedSettingsRepository
) :ViewModel(){
    val dataScreenUiState: StateFlow<DataScreenUiState> = combine(
        repo.weatherDataList,
        settingsRepo.settingsFlow,
        advancedSettingsRepo.advancedSettingsFlow
    ){weatherDataList:WeatherDataLists, settings:Settings, advancedSettings:AdvancedSettings ->
        DataScreenUiState(
            weatherDataList,
            advancedSettings,
            settings.timeIndex
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DataScreenUiState()
    )
    fun setTimeIndex(index:Int){
        viewModelScope.launch { settingsRepo.setTimeIndex(index) }
    }
}