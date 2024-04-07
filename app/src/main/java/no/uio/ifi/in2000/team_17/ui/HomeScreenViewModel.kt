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

data class HomeScreenUiState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = true
)

class HomeScreenViewModel : ViewModel() {
    val repo = Repository()
    val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val useCase = WeatherUseCase()
    val homeScreenUiState = _homeScreenUiState.asStateFlow()

    fun load(latLng: LatLng, maxHeight: Int) {
        viewModelScope.launch {
            repo.load(latLng, maxHeight)
            _homeScreenUiState.update {
                it.copy(
                    weatherPointList = repo.weatherPointList.asStateFlow().value,
                    latLng = latLng,
                    maxHeight = maxHeight
                )
            }
        }
    }


    fun setLatLng(latLng: LatLng) {
        load(latLng, homeScreenUiState.value.maxHeight)
    }

    fun setMaxHeight(maxHeight: Int) {
        load(homeScreenUiState.value.latLng, maxHeight)
    }

    fun findMaxSpeed(): Double {
        return homeScreenUiState.value.weatherPointList.maxOf { it.windSpeed }
    }

    fun findMaxShear(): Double {
        return homeScreenUiState.value.weatherPointList.maxOf { it.windShear }

    }

    init {
        viewModelScope.launch {
            repo.load(latLng = homeScreenUiState.value.latLng, homeScreenUiState.value.maxHeight)
            _homeScreenUiState.update {
                it.copy(
                    weatherPointList = repo.weatherPointList.asStateFlow().value,
                    latLng = homeScreenUiState.value.latLng,
                    maxHeight = homeScreenUiState.value.maxHeight
                )
            }
            canLaunch()
        }
    }

    fun canLaunch() {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    canLaunch = useCase.canLaunch(
                        homeScreenUiState.value.weatherPointList.first(),
                        findMaxSpeed(), findMaxShear()
                    )
                )
            }
        }
    }

}