package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.Repo
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
    val repo = Repo()
    val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    fun load(latLng: LatLng, maxHeight: Int){
        viewModelScope.launch {
            repo.load(latLng, maxHeight)
            _uiState.update {
                it.copy(
                    weatherPointList = repo.weatherPointList.asStateFlow().value,
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
            viewModelScope.launch {
                repo.load(latLng = uiState.value.latLng, uiState.value.maxHeight)
                _uiState.update {
                    it.copy(
                        weatherPointList = repo.weatherPointList.asStateFlow().value,
                        latLng = uiState.value.latLng,
                        maxHeight = uiState.value.maxHeight
                    )
                }
            }
        }
    }
}