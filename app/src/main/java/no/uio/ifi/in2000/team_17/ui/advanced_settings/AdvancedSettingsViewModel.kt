package no.uio.ifi.in2000.team_17.ui.advanced_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.AdvancedSettingsRepository

data class AdvancedSettingsUIState(
    val groundWindSpeed: Double = 8.6,
    val maxWindSpeed: Double = 17.2,
    val maxWindShear: Double = 24.5,
    val cloudFraction: Double = 15.0,
    val fog: Double = 0.0,
    val rain: Double = 0.0,
    val humidity: Double = 75.0,
    val dewPoint: Double = 15.0,
    val margin: Double = 0.6
)
class AdvancedSettingsViewModel(
    private val repo: AdvancedSettingsRepository
):ViewModel(){
    val advancedSettingsUIState = repo.advancedSettingsFlow
        .map { AdvancedSettingsUIState(
            it.groundWindSpeed,
            it.maxWindSpeed,
            it.maxWindShear,
            it.cloudFraction,
            it.fog,
            it.rain,
            it.humidity,
            it.dewPoint,
            it.margin
        ) }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AdvancedSettingsUIState()
            )
    fun setGroundWind(groundWind:Double){
        viewModelScope.launch{ repo.setGroundWind(groundWind) }
    }
    fun setMaxWind(maxWind:Double){
        viewModelScope.launch{ repo.setMaxWind(maxWind) }
    }
    fun setMaxWindShear(windShear:Double){
        viewModelScope.launch{ repo.setMaxWindShear(windShear) }
    }
    fun setCloudFraction(fraction:Double){
        viewModelScope.launch{ repo.setCloudFraction(fraction) }
    }
    fun setFog(fog:Double){
        viewModelScope.launch{ repo.setFog(fog) }
    }
    fun setRain(rain:Double){
        viewModelScope.launch{ repo.setRain(rain) }
    }
    fun setHumiditiy(humidity:Double){
        viewModelScope.launch{ repo.setHumidity(humidity) }
    }
    fun setDewPoint(dewPoint:Double){
        viewModelScope.launch{ repo.setDewPoint(dewPoint) }
    }
    fun setMargin(margin:Double){
        viewModelScope.launch{ repo.setMargin(margin) }
    }
    fun reset(){
        viewModelScope.launch { repo.reset() }
    }
}

