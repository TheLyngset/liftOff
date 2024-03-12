package no.uio.ifi.in2000.team_17.data.isobaricgrib

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.WeatherPoint
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

const val GRAVITATIONAL_ACCELERATION: Double = 9.834 // m/s^2
const val GAS_CONSTANT_AT_DRY_AIR: Double = 287.052874 // J⋅kg−1⋅K−1

class IsobaricRepo {
    private val dataSource = IsobaricDataSource()
    private var pressureAtSeaLevel: Double = 1000.0
    private val isoBaricModel = MutableStateFlow(IsoBaricModel())
    val weatherPointList = MutableStateFlow<List<WeatherPoint>>(listOf())


    suspend fun loadData(north: Double, east: Double, groundWeatherPoint: WeatherPoint = WeatherPoint()) {
        isoBaricModel.update { dataSource.getData(north, east) }
        val layerHeights = mutableListOf<Double>()
        val pressures = isoBaricModel.value.domain.axes.z.values
        isoBaricModel
            .value.ranges.temperature.values
            .zip(pressures)
            .forEach { (temp, pressure) ->
                layerHeights.add(hydrostaticFormula(pressure, temp, pressureAtSeaLevel))
                }

        var s_0 = groundWeatherPoint.windSpeed
        var d_0 = groundWeatherPoint.windDirection
        val windSpeed = isoBaricModel.value.ranges.windSpeed.values
        val temperatures = isoBaricModel.value.ranges.temperature.values
        val windFromDirection = isoBaricModel.value.ranges.windFromDirection.values
        val windShear = mutableListOf<Double>()
        windSpeed.zip(windFromDirection).forEach { (s_1, d_1) ->
            windShear.add(round(WindShear(s_0, d_0, s_1, d_1)))
            s_0 = s_1
            d_0 = d_1
        }
        weatherPointList.update {
            listOf(groundWeatherPoint) + windSpeed.zip(windFromDirection)
                .zip(temperatures) { (speed, direction), temperature ->
                    Triple(speed, direction, temperature)
                }.zip(windShear){(speed, direction, temperature), shear ->
                    listOf(speed, direction, temperature, shear)
                }
                .zip(pressures) { (speed, direction, temperature, shear), pressure ->
                     WeatherPoint(
                         windSpeed = speed,
                         windDirection = direction,
                         windShear = shear,
                         temperature = temperature,
                         pressure = pressure,
                         height = hydrostaticFormula(pressure, temperature, pressureAtSeaLevel)
                     )
                }
            }
        }

    private fun hydrostaticFormula(
        pressure: Double, temperature: Double, pressureAtSeaLevel: Double
    ): Double {
        val tempInKelvin = temperature + 273.15
        //TODO: Pressure at sea level is needed, this we can get from the LocationForecastApi
        return round((GAS_CONSTANT_AT_DRY_AIR / GRAVITATIONAL_ACCELERATION) * tempInKelvin * ln((pressureAtSeaLevel / pressure))) //TODO: Check if this is right

    }

    private fun WindShear(s_0: Double, d_0: Double, s_1: Double, d_1: Double): Double {
        val d_0_rad = d_0 * PI / 180
        val d_1_rad = d_1 * PI / 180
        return sqrt(
            (s_1 * cos(d_1_rad) - s_0 * cos(d_0_rad)).pow(2) + (s_1 * sin(d_1_rad) - s_0 * sin(
                d_0_rad
            )).pow(2)
        )
    }
}