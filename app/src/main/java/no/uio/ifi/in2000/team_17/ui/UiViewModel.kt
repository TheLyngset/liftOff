package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricRepo
import no.uio.ifi.in2000.team_17.data.WeatherPoint
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastRepositoryImplementation
import kotlin.math.round

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
    val latLng: LatLng = LatLng(59.96144907197439, 10.713250420850423)
)
class UiViewModel: ViewModel() {
    val isobaricRepo = IsobaricRepo()
    val locationForecastRepo = LocationForecastRepositoryImplementation()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    fun load(latLng: LatLng){
        viewModelScope.launch {
            val groundWeatherPoint = locationForecastRepo.getGroundWeatherPoint(round(latLng.latitude),round(latLng.longitude), 1)
            isobaricRepo.loadData(latLng.latitude, latLng.longitude, groundWeatherPoint)
            _uiState.update {
                it.copy(
                    weatherPointList = isobaricRepo.weatherPointList.asStateFlow().value
                )
            }
        }
    }
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