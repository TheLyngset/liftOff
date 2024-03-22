package no.uio.ifi.in2000.team_17.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.WeatherPoint

data class UIState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = true
)

class UiViewModel : ViewModel() {
    val repo = Repository()
    val _uiState = MutableStateFlow(UIState())
    val useCase = WeatherUseCase()
    val uiState = _uiState.asStateFlow()

    fun load(latLng: LatLng, maxHeight: Int) {
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

    fun setLatLng(latLng: LatLng) {
        load(latLng, uiState.value.maxHeight)
    }

    fun setMaxHeight(maxHeight: Int) {
        load(uiState.value.latLng, maxHeight)
    }

    fun findMaxSpeed(): Double {
        return uiState.value.weatherPointList.maxOf { it.windSpeed }
    }

    fun findMaxShear(): Double {
        return uiState.value.weatherPointList.maxOf { it.windShear }

    }

    init {
        viewModelScope.launch {
            repo.load(latLng = uiState.value.latLng, uiState.value.maxHeight)
            _uiState.update {
                it.copy(
                    weatherPointList = repo.weatherPointList.asStateFlow().value,
                    latLng = uiState.value.latLng,
                    maxHeight = uiState.value.maxHeight
                )
            }
            canLaunch()
        }
    }

    fun canLaunch() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    canLaunch = useCase.canLaunch(
                        uiState.value.weatherPointList.first(),
                        findMaxSpeed(), findMaxShear()
                    )
                )
            }
        }
    }

}