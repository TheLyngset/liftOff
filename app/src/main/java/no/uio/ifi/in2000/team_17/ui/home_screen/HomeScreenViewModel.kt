package no.uio.ifi.in2000.team_17.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.MainActivity
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.WeatherPoint

data class HomeScreenUiState(
    val weatherPointList: List<WeatherPoint> = listOf(WeatherPoint()),
    val latLng: LatLng = LatLng(59.96, 10.71),
    val maxHeight: Int = 3,
    val canLaunch: Boolean = true
)

class HomeScreenViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = MainActivity.repository
                HomeScreenViewModel(
                    repository = repository
                )
            }
        }
    }

    val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val useCase = WeatherUseCase()
    val homeScreenUiState = _homeScreenUiState.asStateFlow()

    fun load(latLng: LatLng, maxHeight: Int) {
        viewModelScope.launch {
            this@HomeScreenViewModel.repository.load(latLng, maxHeight)
            _homeScreenUiState.update {
                it.copy(
                    weatherPointList = this@HomeScreenViewModel.repository.getWeatherPointList()
                        .asStateFlow().value,
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
            this@HomeScreenViewModel.repository.load(
                latLng = homeScreenUiState.value.latLng,
                homeScreenUiState.value.maxHeight
            )
            _homeScreenUiState.update {
                it.copy(
                    weatherPointList = this@HomeScreenViewModel.repository.getWeatherPointList()
                        .asStateFlow().value,
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