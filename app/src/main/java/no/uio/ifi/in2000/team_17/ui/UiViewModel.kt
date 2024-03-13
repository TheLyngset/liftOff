package no.uio.ifi.in2000.team_17.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricRepo
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.WeatherPoint
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastRepositoryImplementation
import no.uio.ifi.in2000.team_17.data.locationforecast.WeatherUseCase
import kotlin.math.round

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
)
class UiViewModel: ViewModel() {
    val isobaricRepo = IsobaricRepo()
    val useCase = WeatherUseCase()
    val locationForecastRepo = LocationForecastRepositoryImplementation()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        val lat = 59.96144907197439
        val lon = 10.713250420850423
        viewModelScope.launch {
            val groundWeatherPoint = locationForecastRepo.getGroundWeatherPoint(round(lat),round(lon), 1)
            isobaricRepo.loadData(lat, lon, groundWeatherPoint)
            _uiState.update {currentState ->
                currentState.copy(
                    weatherPointList = isobaricRepo.weatherPointList.asStateFlow().value,
                )
            }
        }
    }

/*
    private fun canLaunch(){
        viewModelScope.launch {
            val temp = WeatherUseCase(locationForecastRepo).canLaunch()
            if (temp != null){
                _locationforecastUiState.update {
                    LocationforecastUiState()
                }
            }
        }
    }


 */
}