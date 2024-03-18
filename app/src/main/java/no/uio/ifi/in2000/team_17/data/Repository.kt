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

const val GRAVITATIONAL_ACCELERATION: Double = 9.80665 // m/s^2
const val MOLAR_GAS_CONSTANT: Double = 8.3144598 // J⋅kg−1⋅K−1


/**
 * Repository class manages and provides the data needed for the application, by sending and recieving requests
 * from datasources IsobaricDataSource and LocationForecastDataSource.
 *
 * It also performs calculations like wind shear, height and dew ,.
 *
 * The data sources are used to populate StateFlows (mutable) which are then observed by the ViewModel or UI components.
 *
 * The most important StateFlow is going to be weatherPointList, containing all of the different WeatherPoint for specific coordinates.
 *
 * Error handling is mostly done in the DataSources
 */
class Repository {
    private val LOG_NAME = "REPOSITORY"

    // Load data from isoBaricDataSource and locationForecast,
    private val isoBaricDataSource = IsobaricDataSource()
    private val locationForecastDataSource = LocationForecastDataSource()

    // Creates necessary StateFlows
    private val isoBaricData = MutableStateFlow(IsoBaricModel())
    private val locationForecastData = MutableStateFlow(LocationforecastDTO(null, null, null))
    val weatherPointList = MutableStateFlow<List<WeatherPoint>>(listOf())


    /**
     * Calls both loadLocationForecast() and loadIsoBaricData()
     * @param latLng is a latitude and longitude object
     * @param maxHeight sets maximum needed height for showing later
     */
    suspend fun load(latLng: LatLng, maxHeight: Int) {
        loadLocationForecast(latLng)
        loadIsobaricData(latLng, maxHeight, generateGroundWeatherPoint())

    }

    /**
     * Fetches locationforecastData via the dataSource. If exception is thrown, it will return an
     * empty LocationforecastDTO object
     * @param latLng latitudal and longitudal coordinate
     * @return LocationforecastDTO()
     */
    private suspend fun loadLocationForecast(latLng: LatLng) {
        locationForecastData.update {
            try {
                locationForecastDataSource.fetchLocationforecast(
                    round(latLng.latitude),
                    round(latLng.longitude)
                )
            } catch (e: IOException) {
                Log.e(LOG_NAME, "Error while fetching Locationforecast data: ${e.message}")
                LocationforecastDTO(null, null, null)
            } catch (e: Exception) {
                Log.e(LOG_NAME, "Error while fetching Locationforecast data: ${e.message}")
                LocationforecastDTO(null, null, null)
            }
        }
    }

    /**
     * Loads data from [isoBaricDataSource] and creates [WeatherPoint]'s.
     * These are loaded into weatherPointList
     * Creates weatherpoints and adds them to weatherPointList
     * @param latLng geographic coordinate point for wanted information
     * @param maxHeight max height of information needed
     * @param groundWeatherPoint geographic point at ground-height
     */
    private suspend fun loadIsobaricData(
        latLng: LatLng,
        maxHeight: Int,
        groundWeatherPoint: WeatherPoint
    ) {
        val pressureAtSeaLevel = groundWeatherPoint.pressure
        isoBaricData.update { isoBaricDataSource.getData(latLng.latitude, latLng.longitude) }

        // Parses data from isoBaricData into relevant values
        val isoData =
            isoBaricData.value.ranges // Extracts the relevant value ranges from isobaricModel -> less boilerplate
        val pressures = isoBaricData.value.domain.axes.z.values
        var s_0 = groundWeatherPoint.windSpeed //Wind speed at lower level
        var d_0 = groundWeatherPoint.windDirection //Wind direction at lower level
        val windSpeed = isoData.windSpeed.values
        val temperatures = isoData.temperature.values
        val windFromDirection = isoData.windFromDirection.values
        val windShear = mutableListOf<Double>()

        windSpeed.zip(windFromDirection)
            .forEach { (s_1, d_1) -> // Wind speed and wind direction at higher level
                windShear.add(round(calculateWindShear(s_0, d_0, s_1, d_1)))
                s_0 = s_1
                d_0 = d_1
            }

        // Zips lists to parse everything together into a single WeatherPoint
        weatherPointList.update {
            val allLayers = listOf(groundWeatherPoint) + windSpeed.zip(windFromDirection)
                .zip(temperatures) { (speed, direction), temperature ->
                    Triple(speed, direction, temperature)
                }.zip(windShear) { (speed, direction, temperature), shear ->
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
            allLayers.filter { it.height <= maxHeight * 1000 + 1000 }
        }
    }

    /**
     * Parses data from locationForecastData into a WeatherPoint object
     * @return weatherpoint with ground level information
     * @author Lelia
     */
    private fun generateGroundWeatherPoint(): WeatherPoint {
        val index = 1
        val timeSeriesInstantDetails: Details? = // Reduces boilerplate later on
            locationForecastData.value.properties?.timeseries?.getOrNull(index)?.data?.instant?.details

        return WeatherPoint(
            windSpeed = timeSeriesInstantDetails!!.wind_speed,
            windDirection = timeSeriesInstantDetails.wind_from_direction,
            temperature = timeSeriesInstantDetails.air_temperature,
            pressure = timeSeriesInstantDetails.air_pressure_at_sea_level,
            cloudFraction = timeSeriesInstantDetails.cloud_area_fraction,
            rain = locationForecastData.value.properties!!.timeseries.getOrNull(index)!!.data.next_1_hours.details.precipitation_amount,
            humidity = timeSeriesInstantDetails.relative_humidity,
            height = 0.0,
            dewPoint = timeSeriesInstantDetails.dew_point_temperature,
            fog = timeSeriesInstantDetails.fog_area_fraction
        )
    }
}

// TODO: create KDoc
internal fun calculateHeight(
    pressure: Double, temperature: Double, pressureAtSeaLevel: Double
): Double {
    //https://en.wikipedia.org/wiki/Barometric_formula
    val tempInKelvin = temperature + 273.15
    return round(
        (MOLAR_GAS_CONSTANT / GRAVITATIONAL_ACCELERATION) * tempInKelvin
                * ln((pressureAtSeaLevel / pressure))
    ) //TODO: Check if this is right
}

/**
 * Calculates the Wind Shear between two points considering the wind speed and wind direction at each point.
 * Uses the formula: sqrt((s1 * cos(d1_rad) - s0 * cos(d0_rad))^2 + (s1 * sin(d1_rad) - s0 * sin(d0_rad))^2)
 * @param s_0 Wind Speed at the lower level
 * @param d_0 Wind direction at the lower level
 * @param s_1 Wind Speed at the higher level
 * @param d_1 Wind direction at the higher level
 * @return Wind Shear between the two points
 */
internal fun calculateWindShear(s_0: Double, d_0: Double, s_1: Double, d_1: Double): Double {
    //trenger vi egentlig polar koordinater her?
    val d_0_rad = d_0 * PI / 180
    val d_1_rad = d_1 * PI / 180
    return sqrt(
        (s_1 * cos(d_1_rad) - s_0 * cos(d_0_rad)).pow(2) + (s_1 * sin(d_1_rad) - s_0 * sin(
            d_0_rad
        )).pow(2)
    )
}

/**
 * Calculates the dew point at ground level considering the temperature and the relative humidity.
 * Uses the formula: T - ((100 - RH) / 5), where T is the temperature and RH is the relative humidity.
 * Note: This relationship is fairly accurate for relative humidity values above 50%.
 * @param temperature Observed temperature
 * @param relativeHumidity Relative humidity
 * @return Dew point at ground level, if temperature and relative humidity are not null. Otherwise, returns -1.
 */
internal fun computeDewPointGround(temperature: Double?, relativeHumidity: Double?): Double {
    //https://iridl.ldeo.columbia.edu/dochelp/QA/Basic/dewpoint.html
    // accuracy within 1 Celcius for relative humidity over 50%
    if (temperature != null && relativeHumidity != null) {
        return round((temperature - ((100 - relativeHumidity) / 5)))
    }
    return -1.0
}