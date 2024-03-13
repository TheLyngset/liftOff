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
import kotlin.math.max
import kotlin.math.round

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
    val latLng: LatLng = LatLng(59.96144907197439, 10.713250420850423),
    val maxHeight: Int = 3
)
class UiViewModel: ViewModel() {
    val isobaricRepo = IsobaricRepo()
    val locationForecastRepo = LocationForecastRepositoryImplementation()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    fun load(latLng: LatLng, maxHeight: Int){
        viewModelScope.launch {
            val groundWeatherPoint = locationForecastRepo.getGroundWeatherPoint(round(latLng.latitude),round(latLng.longitude), 1)
            isobaricRepo.loadData(latLng.latitude, latLng.longitude, groundWeatherPoint, maxHeight = maxHeight)
            _uiState.update {
                it.copy(
                    weatherPointList = isobaricRepo.weatherPointList.asStateFlow().value,
                    latLng = latLng,
                    maxHeight = maxHeight
                )
            }
        }
    }
    fun setLatLng(latLng: LatLng){
        load(latLng, uiState.value.maxHeight)
    }
    fun setMaxHeight(maxHeight:Int){
        load(uiState.value.latLng, maxHeight)
    }
    init {
        viewModelScope.launch {
            val groundWeatherPoint = locationForecastRepo.getGroundWeatherPoint(round(uiState.value.latLng.latitude),round(uiState.value.latLng.longitude), 1)
            isobaricRepo.loadData(uiState.value.latLng.latitude, uiState.value.latLng.longitude, groundWeatherPoint, uiState.value.maxHeight)
            _uiState.update {
                it.copy(
                    weatherPointList = isobaricRepo.weatherPointList.asStateFlow().value
                )
            }
        }
    }
}