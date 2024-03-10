package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricRepo
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel

data class UIState(
    val layerHeights:List<Double> = listOf(),
    val windSpeeds:List<Double> = listOf(),
    val windFromDirections: List<Double> = listOf(),
    val temperatures: List<Double> = listOf(),
    val windShear:List<Double> = listOf()
)
class UiViewModel: ViewModel() {
    val repo = IsobaricRepo()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.loadData(59.65797123887148, 10.76636053581773)
            _uiState.update {
                it.copy(
                    layerHeights = repo.layerHeights.asStateFlow().value,
                    windSpeeds = repo.windSpeeds.asStateFlow().value,
                    windFromDirections = repo.windFromDirection.asStateFlow().value,
                    temperatures = repo.temperatures.asStateFlow().value,
                    windShear = repo.windShear.asStateFlow().value
                )
            }
        }
    }
}