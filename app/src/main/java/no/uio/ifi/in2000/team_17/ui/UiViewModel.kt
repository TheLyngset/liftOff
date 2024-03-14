package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.WeatherPoint
import no.uio.ifi.in2000.team_17.data.locationforecast.WeatherUseCase

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
    val latLng: LatLng = LatLng(59.96144907197439, 10.713250420850423),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = false
)
class UiViewModel: ViewModel() {
    val repo = Repository()
    val _uiState = MutableStateFlow(UIState())
    val useCase = WeatherUseCase()
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

    fun findMaxSpeed(): Double{
        return uiState.value.weatherPointList.maxOf { it.windSpeed }
    }
    fun findMaxShear(): Double{
        return uiState.value.weatherPointList.maxOf { it.windShear }

    }
    init {
        loadWeatherData()
        canLaunch()
    }

    fun loadWeatherData(){
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

    fun canLaunch(){
                 viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        canLaunch = useCase.canLaunch(uiState.value.weatherPointList.first(),
                            findMaxSpeed(), findMaxShear() )
                    )
                }
            }
    }

    /*
    // old init
    init {
        viewModelScope.launch {
            viewModelScope.launch {
                repo.load(latLng = uiState.value.latLng, uiState.value.maxHeight)
                _uiState.update {
                    it.copy(
                        weatherPointList = repo.weatherPointList.asStateFlow().value,
                        latLng = uiState.value.latLng,
                        maxHeight = uiState.value.maxHeight,
                    )
                }
            }
        }
    }

     */
}