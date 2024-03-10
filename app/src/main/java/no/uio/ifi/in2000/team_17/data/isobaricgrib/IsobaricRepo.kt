package no.uio.ifi.in2000.team_17.data.isobaricgrib

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel
import kotlin.math.ln
import kotlin.math.round

val GRAVITATIONAL_ACCELERATION: Double = 9.834 // m/s^2
val GAS_CONSTANT_AT_DRY_AIR: Double = 287.052874 // J⋅kg−1⋅K−1
class IsobaricRepo {
    private val dataSource = IsobaricDataSource()
    private var pressureAtSeaLevel:Double = 1000.0
    val isoBaricModel = MutableStateFlow(IsoBaricModel())
    val layerHeights = MutableStateFlow<List<Double>>(listOf())
    val windSpeeds = MutableStateFlow<List<Double>>(listOf())
    val windFromDirection = MutableStateFlow<List<Double>>(listOf())
    val temperatures = MutableStateFlow<List<Double>>(listOf())
    suspend fun loadData(north:Double, east:Double){
        isoBaricModel.update {dataSource.getData(north, east)}

        val pressures = isoBaricModel.value.domain.axes.z.values
        isoBaricModel
            .value.ranges.temperature.values
            .zip(pressures)
            .forEach { (temp, pressure) ->
                layerHeights.update {
                    it + hydrostaticFormula(pressure, temp, pressureAtSeaLevel)
                }
            }
        windSpeeds.update { isoBaricModel.value.ranges.windSpeed.values }
        windFromDirection.update { isoBaricModel.value.ranges.windFromDirection.values }
        temperatures.update { isoBaricModel.value.ranges.temperature.values }

    }
    fun setPressureAtSealevel(pressure: Double){
        pressureAtSeaLevel = pressure
    }
    fun hydrostaticFormula(
        pressure: Double, temperature: Double, pressureAtSeaLevel: Double
    ): Double {
        val tempInKelvin = temperature + 273.15
        //TODO: Pressure at sea level is needed, this we can get from the LocationForecastApi
        return round((GAS_CONSTANT_AT_DRY_AIR / GRAVITATIONAL_ACCELERATION) * tempInKelvin * ln((pressureAtSeaLevel / pressure))) //TODO: Check if this is right

    }
}