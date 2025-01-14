package no.uio.ifi.in2000.team_17.data

import android.util.Log
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsoBaricModel
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsoBaricTime
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricDataSource
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.Properties
import no.uio.ifi.in2000.team_17.model.LatLng
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WeatherPointLayer
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

//List of constant pressures used by the edrisobaric api
private val PRESSURES: List<Double> = listOf(
    850.0,
    750.0,
    700.0,
    600.0,
    500.0,
    450.0,
    400.0,
    350.0,
    300.0,
    275.0,
    250.0,
    225.0,
    200.0,
    150.0,
    100.0
)

//Constants for calculating height
const val GRAVITATIONAL_ACCELERATION: Double = 9.80665 // m/s^2
const val MOLAR_GAS_CONSTANT: Double = 8.3144598 // J⋅kg−1⋅K−1
const val MOLAR_MASS_OF_AIR: Double = 0.028964425278793993 // kg/mol

/**
 * This repository loads data from [IsobaricDataSource] and [LocationForecastDataSource]
 * and exposes it through the datastructure [WeatherDataLists]
 * [load] takes the parameters
 */
interface Repository {
    suspend fun load(latLng: LatLng, maxHeight: Int)
    val weatherDataList: StateFlow<WeatherDataLists>
    val hasLocationForecastData: StateFlow<Boolean>
    val hasIsoBaricData: StateFlow<Boolean>
    val failedToUpdate: StateFlow<Boolean>
}

class RepositoryImplementation : Repository {
    private val logname = "REPOSITORY"
    private var lastLatLng = LatLng(59.0, 10.0)
    private var lastMaxHeight = 3

    // Load data from isoBaricDataSource and locationForecastDataSource
    private val locationForecastDataSource: LocationForecastDataSource =
        LocationForecastDataSource()
    private val isobaricDataSource: IsobaricDataSource = IsobaricDataSource()

    // Creates necessary StateFlows
    private val _isoBaricData = MutableStateFlow(listOf(IsoBaricModel.Ranges()))
    private val isoBaricData = _isoBaricData.asStateFlow()


    private val _locationForecastData = MutableStateFlow(Properties())
    private val locationForecastData = _locationForecastData.asStateFlow()

    private val _weatherDataLists = MutableStateFlow(WeatherDataLists())
    override val weatherDataList = _weatherDataLists.asStateFlow()

    private val _hasLocationForecastData = MutableStateFlow(false)
    override val hasLocationForecastData = _hasLocationForecastData.asStateFlow()

    private val _hasIsoBaricData = MutableStateFlow(false)
    override val hasIsoBaricData = _hasIsoBaricData.asStateFlow()

    private val _failedToUpdate = MutableStateFlow(false)
    override val failedToUpdate = _failedToUpdate.asStateFlow()

    /**
     * loads data from [locationForecastDataSource] and updates the StateFlow [locationForecastData]
     * @param latLng a data class containing the coordinates from which to load data
     */
    private suspend fun loadLocationForecast(latLng: LatLng) {
        _locationForecastData.update {
            try {
                val data = locationForecastDataSource.fetchLocationforecast(
                    latLng.latitude,
                    latLng.longitude
                ).properties
                _hasLocationForecastData.update { true }
                data
            } catch (e: IOException) {
                Log.e(logname, "Error while fetching Locationforecast data: ${e.message}")
                _failedToUpdate.update { true }
                delay(500)
                _failedToUpdate.update { false }
                it.copy()
            } catch (e: NoTransformationFoundException) {
                Log.e(
                    logname,
                    "NoTransformationFoundException while fetching Locationforecast data: ${e.message}"
                )
                _failedToUpdate.update { true }
                delay(500)
                _failedToUpdate.update { false }
                it.copy()
            } catch (e: ClientRequestException) {
                Log.e(
                    logname,
                    "ClientRequestException while fetching Locationforecast data: ${e.message}"
                )
                _failedToUpdate.update { true }
                delay(500)
                _failedToUpdate.update { false }
                it.copy()
            } catch (e: ServerResponseException) {
                Log.e(
                    logname,
                    "ServerResponseException while fetching Locationforecast data: ${e.message}"
                )
                _failedToUpdate.update { true }
                delay(500)
                _failedToUpdate.update { false }
                it.copy()
            }
        }
    }

    /**
     * loads data from [isobaricDataSource] and mimics [locationForecastData] by
     * making a list of [IsoBaricModel.Ranges] with the index 0 corresponding to now,
     * index 1 corresponding to in one hour and so on
     * @param latLng a data class containing the coordinates from which to load data
     */
    private suspend fun loadIsobaric(latLng: LatLng) {
        val startIndex = LocalDateTime.now(ZoneId.systemDefault()).hour % 3
        val newIsoBaricModel = mutableListOf<IsoBaricModel.Ranges>()
        var isoBaricNow = IsoBaricModel.Ranges()
        var isoBaricIn3 = IsoBaricModel.Ranges()
        var isoBaricIn6 = IsoBaricModel.Ranges()
        try {
            isoBaricNow = isobaricDataSource.getData(
                latLng.latitude,
                latLng.longitude,
                IsoBaricTime.NOW
            ).ranges
            _hasIsoBaricData.update { true }
        } catch (e: IOException) {
            Log.e(logname, "Error while fetching isobaric data: ${e.message}")
            if (hasIsoBaricData.value) {
                _failedToUpdate.update { true }
                delay(500)
                _failedToUpdate.update { false }
                return
            } else {
                _hasIsoBaricData.update { false }
            }
        }

        try {
            isoBaricIn3 = isobaricDataSource.getData(
                latLng.latitude,
                latLng.longitude,
                IsoBaricTime.IN_3
            ).ranges
        } catch (e: IOException) {
            Log.e(logname, "Error while fetching isobaric data: ${e.message}")
        }

        try {
            isoBaricIn6 = isobaricDataSource.getData(
                latLng.latitude,
                latLng.longitude,
                IsoBaricTime.IN_6
            ).ranges
        } catch (e: IOException) {
            Log.e(logname, "Error while fetching isobaric data: ${e.message}")
        }

        if (isoBaricNow.temperature.values.isNotEmpty()) {
            (startIndex..2).forEach { _ -> newIsoBaricModel.add(isoBaricNow) }
            if (isoBaricIn3.temperature.values.isNotEmpty()) {
                (1..3).forEach { _ -> newIsoBaricModel.add(isoBaricIn3) }
                if (isoBaricIn6.temperature.values.isNotEmpty()) {
                    (1..3).forEach { _ -> newIsoBaricModel.add(isoBaricIn6) }
                }
            }
        }
        _isoBaricData.update { newIsoBaricModel }
    }

    /**
     * Loads data from [locationForecastDataSource] and [isobaricDataSource] if nessecary
     * and updates the exposed [weatherDataList]
     * @param latLng a data class containing the coordinates to get data from
     * @param maxHeight The maximum possible height the rocket can reach
     */
    override suspend fun load(latLng: LatLng, maxHeight: Int) {
        if (latLng != lastLatLng) {
            loadLocationForecast(latLng)
            loadIsobaric(latLng)
            updateWeatherDataLists(maxHeight)
            lastLatLng = latLng
        } else if (maxHeight != lastMaxHeight) {
            updateWeatherDataLists(maxHeight)
        }
    }

    /**
     * Updates the WeatherDataLists by using [locationForecastData]
     * and [isoBaricData] to make a [weatherDataList] which
     * generates a weatherPointList for each hour available from
     * [isoBaricData] and uses this to generate [WindShear] and
     * [WindLayer] values when available. The resulting data class
     * has lists of varying length depending on the data available.
     *
     * @param maxHeight The maximum possible height the rocket can reach
     */
    private fun updateWeatherDataLists(maxHeight: Int) {
        _weatherDataLists.update {
            val locationData = locationForecastData.value
            val isobaricData = isoBaricData.value
            val listOfWeatherPointList =
                isobaricData.zip(locationData.timeseries) { isoBaric, location ->
                    val locationDetails = location.data.instant.details
                    val groundWeatherPoint = WeatherPointLayer(
                        windSpeed = locationDetails.wind_speed,
                        windDirection = locationDetails.wind_from_direction,
                        temperature = locationDetails.air_temperature,
                        pressure = locationDetails.air_pressure_at_sea_level,
                        height = 10.0,
                        cloudFraction = locationDetails.cloud_area_fraction,
                        rain = location.data.next_1_hours.details.probability_of_precipitation,
                        humidity = locationDetails.relative_humidity,
                        dewPoint = locationDetails.dew_point_temperature,
                        fog = locationDetails.fog_area_fraction
                    )
                    generateWeatherPointLayerList(
                        isoData = isoBaric,
                        groundWeatherPoint = groundWeatherPoint,
                        maxHeight = maxHeight
                    )
                }
            WeatherDataLists(
                dateTime = locationData.timeseries.map {
                    LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                        .plusHours(2)
                        .toString()
                },
                date = locationData.timeseries.map {
                    LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                        .plusHours(2)
                        .toLocalDate()
                        .toString()
                },
                time = locationData.timeseries.map {
                    LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                        .toLocalTime()
                        .plusHours(2)
                        .toString()
                },
                groundWind = locationData.timeseries.map {
                    WindLayer(
                        it.data.instant.details.wind_speed,
                        10.0,
                        it.data.instant.details.wind_from_direction
                    )
                },
                maxWindShear = listOfWeatherPointList.map { weatherPointLayerList ->
                    val windShear = weatherPointLayerList.map { it.windShear }
                    val maxWindShear = windShear.max()
                    val index = windShear.indexOf(windShear.max())
                    WindShear(maxWindShear, weatherPointLayerList[index].height)
                },
                maxWind = listOfWeatherPointList.map { weatherPointLayerList ->
                    val windSpeed = weatherPointLayerList.map { it.windSpeed }
                    val maxWindSpeed = windSpeed.max()
                    val index = windSpeed.indexOf(maxWindSpeed)
                    WindLayer(maxWindSpeed, weatherPointLayerList[index].height, weatherPointLayerList[index].windDirection)
                },
                cloudFraction = locationData.timeseries.map { it.data.instant.details.cloud_area_fraction },
                rain = locationData.timeseries.map {
                    val data = it.data.next_1_hours.details
                    Rain(
                        data.precipitation_amount_min,
                        data.precipitation_amount,
                        data.precipitation_amount_max,
                        data.probability_of_precipitation
                    )
                },
                temperature = locationData.timeseries.map { it.data.instant.details.air_temperature },
                humidity = locationData.timeseries.map { it.data.instant.details.relative_humidity },
                dewPoint = locationData.timeseries.map { it.data.instant.details.dew_point_temperature },
                fog = locationData.timeseries.map { it.data.instant.details.fog_area_fraction },
                updated = try {
                    LocalDateTime.parse(
                        locationData.meta.updated_at,
                        DateTimeFormatter.ISO_DATE_TIME
                    ).toLocalTime()
                        .plusHours(2)
                        .toString()
                } catch (e: DateTimeParseException) {
                    e.printStackTrace()
                    ""
                }
            )
        }
    }
}


/**
 * Generates a list of [WeatherPointLayer]
 * @param isoData is the data from the edrisoBaricApi
 * @param pressures is a list of predetermined pressures from the api
 * @param groundWeatherPoint is a generated [WeatherPointLayer] from [LocationForecastDataSource]
 * @param maxHeight is the max height predicted for the rocket to reach
 */
private fun generateWeatherPointLayerList(
    isoData: IsoBaricModel.Ranges,
    pressures: List<Double> = PRESSURES,
    groundWeatherPoint: WeatherPointLayer,
    maxHeight: Int
): List<WeatherPointLayer> {
    val pressureAtSeaLevel = groundWeatherPoint.pressure
    var s0 = groundWeatherPoint.windSpeed //Wind speed at lower level
    var d0 = groundWeatherPoint.windDirection //Wind direction at lower level
    val windSpeed = isoData.windSpeed.values
    val temperatures = isoData.temperature.values
    val windFromDirection = isoData.windFromDirection.values
    val windShear = mutableListOf<Double>()

    windSpeed.zip(windFromDirection)
        .forEach { (s1, d1) -> // Wind speed and wind direction at higher level
            windShear.add(round(calculateWindShear(s0, d0, s1, d1)))
            s0 = s1
            d0 = d1
        }
    val allLayers = listOf(groundWeatherPoint) + windSpeed.zip(windFromDirection)
        .zip(temperatures) { (speed, direction), temperature ->
            Triple(speed, direction, temperature)
        }.zip(windShear) { (speed, direction, temperature), shear ->
            listOf(speed, direction, temperature, shear)
        }
        .zip(pressures) { (speed, direction, temperature, shear), pressure ->
            WeatherPointLayer(
                windSpeed = speed,
                windDirection = direction,
                windShear = shear,
                temperature = temperature,
                pressure = pressure,
                height = calculateHeight(pressure, temperature, pressureAtSeaLevel)
            )
        }
    return allLayers.filter { it.height <= maxHeight * 1000 }
}

/**
 * Calculates the Height at a given pressure using the Barometric formula
 * @param pressure Pressure at unknown height
 * @param temperature temperature at unknown height
 * @param pressureAtSeaLevel Known pressure at Sea Level from LocationForecast
 * @return height in moh
 */
internal fun calculateHeight(
    pressure: Double, temperature: Double, pressureAtSeaLevel: Double
): Double {
    // https://en.wikipedia.org/wiki/Barometric_formula
    val tempInKelvin = temperature + 273.15

    return round(
        (-MOLAR_GAS_CONSTANT * tempInKelvin) * ln(pressure / pressureAtSeaLevel) /
                (GRAVITATIONAL_ACCELERATION * MOLAR_MASS_OF_AIR)
    )
}

/**
 * Calculates the Wind Shear between two points considering the wind speed and wind direction at each point.
 * Uses the formula: sqrt((s1 * cos(d1_rad) - s0 * cos(d0_rad))^2 + (s1 * sin(d1_rad) - s0 * sin(d0_rad))^2)
 * @param s0 Wind Speed at the lower level
 * @param d0 Wind direction at the lower level
 * @param s1 Wind Speed at the higher level
 * @param d1 Wind direction at the higher level
 * @return Wind Shear between the two points
 */

internal fun calculateWindShear(s0: Double, d0: Double, s1: Double, d1: Double): Double {
    val d0Rad = d0 * PI / 180
    val d1Rad = d1 * PI / 180
    return sqrt((s1 * cos(d1Rad) - s0 * cos(d0Rad)).pow(2) + (s1 * sin(d1Rad) - s0 * sin(
            d0Rad
        )).pow(2)
    )
}
