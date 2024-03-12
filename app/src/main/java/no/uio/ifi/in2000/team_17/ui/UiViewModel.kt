package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricRepo
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.WeatherPoint

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint())
)
class UiViewModel: ViewModel() {
    val repo = IsobaricRepo()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.loadData(59.96144907197439, 10.713250420850423)
            _uiState.update {
                it.copy(
                    weatherPointList = repo.weatherPointList.asStateFlow().value
                )
            }
        }
    }
}