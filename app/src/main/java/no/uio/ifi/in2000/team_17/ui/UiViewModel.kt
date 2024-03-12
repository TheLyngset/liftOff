package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricRepo
import no.uio.ifi.in2000.team_17.data.WeatherPoint
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastRepositoryImplementation
import kotlin.math.round

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint())
)
class UiViewModel: ViewModel() {
    val isobaricRepo = IsobaricRepo()
    val locationForecastRepo = LocationForecastRepositoryImplementation()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        val lat = 59.96144907197439
        val lon = 10.713250420850423
        viewModelScope.launch {
            val groundWeatherPoint = locationForecastRepo.getGroundWeatherPoint(round(lat),round(lon), 1)
            isobaricRepo.loadData(lat, lon, groundWeatherPoint)
            _uiState.update {
                it.copy(
                    weatherPointList = isobaricRepo.weatherPointList.asStateFlow().value
                )
            }
        }
    }
}