package no.uio.ifi.in2000.team_17.data

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricDataSource
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.Details
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.LocationforecastDTO
import java.io.IOException
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

const val GRAVITATIONAL_ACCELERATION: Double = 9.834 // m/s^2
const val GAS_CONSTANT_AT_DRY_AIR: Double = 287.052874 // J⋅kg−1⋅K−1
class Repo {
    private val isoBaricDataSource = IsobaricDataSource()
    private val locationForecastDataSource = LocationForecastDataSource()
    private val LOG_NAME = "REPOSITORY"
    private var pressureAtSeaLevel: Double = 1000.0

    private val isoBaricData = MutableStateFlow(IsoBaricModel())
    private val locationForecastData = MutableStateFlow(LocationforecastDTO(null, null, null))
    val weatherPointList = MutableStateFlow<List<WeatherPoint>>(listOf())


    suspend fun load(latLng: LatLng, maxHeight: Int){
        loadLocationForecast(latLng)
        loadIsobaricData(latLng, maxHeight, generateGroundWeatherPoint())

    }
    private suspend fun loadLocationForecast(latLng: LatLng){
        locationForecastData.update {
            try{
                locationForecastDataSource.fetchLocationforecast(
                    round(latLng.latitude),
                    round(latLng.longitude)
                )
            }catch (e: IOException) {
                Log.e(LOG_NAME, "Error while fetching Locationforecast data: ${e.message}")
                LocationforecastDTO(null, null, null)
            } catch (e: Exception) {
                Log.e(LOG_NAME, "Error while fetching Locationforecast data: ${e.message}")
                LocationforecastDTO(null, null, null)
            }
        }
    }

    private suspend fun loadIsobaricData(latLng: LatLng, maxHeight: Int, groundWeatherPoint:WeatherPoint){
        pressureAtSeaLevel = groundWeatherPoint.pressure
        isoBaricData.update { isoBaricDataSource.getData(latLng.latitude, latLng.longitude) }
        val layerHeights = mutableListOf<Double>()
        val pressures = isoBaricData.value.domain.axes.z.values
        isoBaricData
            .value.ranges.temperature.values
            .zip(pressures)
            .forEach { (temp, pressure) ->
                layerHeights.add(calculateHeight(pressure, temp, pressureAtSeaLevel))
            }
        var s_0 = groundWeatherPoint.windSpeed //Wind speed at lower level
        var d_0 = groundWeatherPoint.windDirection //Wind direction at lower level
        val windSpeed = isoBaricData.value.ranges.windSpeed.values
        val temperatures = isoBaricData.value.ranges.temperature.values
        val windFromDirection = isoBaricData.value.ranges.windFromDirection.values
        val windShear = mutableListOf<Double>()
        windSpeed.zip(windFromDirection).forEach { (s_1, d_1) -> //Wind speed and wind direction at higher level
            windShear.add(round(calculateWindShear(s_0, d_0, s_1, d_1)))
            s_0 = s_1
            d_0 = d_1
        }
        weatherPointList.update {
            val allLayers = listOf(groundWeatherPoint) + windSpeed.zip(windFromDirection)
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
                        height = calculateHeight(pressure, temperature, pressureAtSeaLevel)
                    )
                }
            allLayers.filter { it.height <= maxHeight*1000 + 1000 }
        }
    }

    private fun generateGroundWeatherPoint(): WeatherPoint {
        val index = 1
        val timeSeriesInstantDetails: Details? = // Reduces boilerplate later on
            locationForecastData.value.properties?.timeseries?.getOrNull(index)?.data?.instant?.details

        val windSpeed: Double? =
            timeSeriesInstantDetails?.wind_speed
        val windFromDirection: Double? =
            timeSeriesInstantDetails?.wind_from_direction
        val airTemperature: Double? =
            timeSeriesInstantDetails?.air_temperature
        val pressureSeaLevel: Double? =
            timeSeriesInstantDetails?.air_pressure_at_sea_level
        val cloudFraction: Double? =
            timeSeriesInstantDetails?.cloud_area_fraction
        val rain: Double? =
            locationForecastData.value.properties?.timeseries?.getOrNull(index)?.data?.next_1_hours?.details?.precipitation_amount
        val relativeHumidity: Double? =
            timeSeriesInstantDetails?.relative_humidity
        val dewPoint: Double = computeDewPointGround(airTemperature, relativeHumidity)

        //var weatherPoint = GroundWeatherPoint(windSpeed, windFromDirection, airTemperature, pressureSeaLevel,cloudFraction,rain, humidity, 0.0)
        val weatherPoint = WeatherPoint(
            windSpeed = windSpeed!!,
            windDirection = windFromDirection!!,
            temperature = airTemperature!!,
            pressure = pressureSeaLevel!!,
            cloudFraction = cloudFraction!!,
            rain = rain!!,
            humidity = relativeHumidity!!,
            height = 0.0,
            dewPoint = dewPoint
        )

        return weatherPoint
    }

    private fun calculateHeight(
        pressure: Double, temperature: Double, pressureAtSeaLevel: Double
    ): Double {
        val tempInKelvin = temperature + 273.15
        return round((GAS_CONSTANT_AT_DRY_AIR / GRAVITATIONAL_ACCELERATION) * tempInKelvin * ln((pressureAtSeaLevel / pressure))) //TODO: Check if this is right
    }

    private fun calculateWindShear(s_0: Double, d_0: Double, s_1: Double, d_1: Double): Double {
        val d_0_rad = d_0 * PI / 180
        val d_1_rad = d_1 * PI / 180
        return sqrt(
            (s_1 * cos(d_1_rad) - s_0 * cos(d_0_rad)).pow(2) + (s_1 * sin(d_1_rad) - s_0 * sin(
                d_0_rad
            )).pow(2)
        )
    }
    private fun computeDewPointGround(temperature: Double?, relativeHumidity: Double?): Double {
        //https://iridl.ldeo.columbia.edu/dochelp/QA/Basic/dewpoint.html
        //Td = T - ((100 - RH)/5.)
        //Td is dew point temperature (in degrees Celsius),
        // T is observed temperature (in degrees Celsius), and
        // RH is relative humidity (in percent).
        // Apparently this relationship is fairly accurate for relative humidity values above 50%.
        if (temperature != null && relativeHumidity != null) {
            return round((temperature - ((100 - relativeHumidity) / 5)))
        }
        return -1.0
    }
}