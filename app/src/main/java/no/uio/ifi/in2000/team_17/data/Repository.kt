package no.uio.ifi.in2000.team_17.data

import android.icu.util.Calendar
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricDataSource
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.team_17.model.IsoBaricModel
import no.uio.ifi.in2000.team_17.model.WeatherPoint
import no.uio.ifi.in2000.team_17.model.weatherDTO.Details
import no.uio.ifi.in2000.team_17.model.weatherDTO.LocationforecastDTO
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
const val MOLAR_MASS_OF_AIR: Double = 0.028964425278793993 // kg/mol

interface Repository {
    suspend fun load(latLng: LatLng, heigth: Int)
    suspend fun getListOfWeatherPointsLists(
        from: Int,
        to: Int
    ): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>>
}

/**
 * Repository class manages and provides the data needed for the application, by sending and recieving requests
 * from datasources IsobaricDataSource and LocationForecastDataSource.
 *
 * It also performs calculations like wind shear and alltitude
 *
 * The data sources are used to populate StateFlows (mutable) which are then observed by the ViewModel or UI components.
 *
 * The most important StateFlow is going to be weatherPointList, containing all of the different WeatherPoint for specific coordinates.
 *
 * Error handling is mostly done in the DataSources
 */
class RepositoryImplementation : Repository {
    private val LOG_NAME = "REPOSITORY"
    private var hourIndex = 2
    private var coordinates: LatLng = LatLng(59.96144907197439, 10.713250420850423)
    private var maxHeight: Int = 3

    // Load data from isoBaricDataSource and locationForecast
    private val locationForecastDataSource: LocationForecastDataSource =
        LocationForecastDataSource()
    private val isobaricDataSource: IsobaricDataSource = IsobaricDataSource()

    // Creates necessary StateFlows
    private val isoBaricData = MutableStateFlow(IsoBaricModel())
    private val locationForecastData = MutableStateFlow(LocationforecastDTO(null, null, null))

    //does this need to be a state flow?
    val weatherPointList = MutableStateFlow<List<WeatherPoint>>(listOf())
    var flowOfWeatherPointLists =
        MutableStateFlow(mutableListOf<MutableStateFlow<List<WeatherPoint>>>())
    var listOfWeatherPointLists = mutableListOf<MutableStateFlow<List<WeatherPoint>>>()


    /**
     * Calls both loadLocationForecast() and loadIsoBaricData()
     * @param latLng is a latitude and longitude object
     * @param maxHeight sets maximum needed height for showing later
     */
    override suspend fun load(latLng: LatLng, heigth: Int) {
        coordinates = latLng
        maxHeight = heigth
        loadLocationForecast()
        loadIsobaric()
        generateWeatherPointList(generateGroundWeatherPoint(hourIndex))
    }

    /**
     * Fetches locationforecastData via the dataSource. If exception is thrown, it will return an
     * empty LocationforecastDTO object
     * @param latLng latitudal and longitudal coordinate
     * @return LocationforecastDTO()
     */
    private suspend fun loadLocationForecast() {
        locationForecastData.update {
            try {
                locationForecastDataSource.fetchLocationforecast(
                    round(coordinates.latitude),
                    round(coordinates.longitude)
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

    private suspend fun loadIsobaric() {
        try {
            isoBaricData.update {
                isobaricDataSource.getData(
                    coordinates.latitude,
                    coordinates.longitude
                )
            }
        } catch (e: IOException) {
            Log.e(LOG_NAME, "Error while fetching isobaric data: ${e.message}")
            IsoBaricModel()
        } catch (e: Exception) {
            Log.e(LOG_NAME, "Error while fetching isobaric data: ${e.message}")
            IsoBaricModel()
        }

    }


    /**
     * Loads data from [isobaricDataSource]
     * based on locationForecasst ground weather points creates [WeatherPoint]'s.
     * These are loaded into weatherPointList
     * Creates weatherpoints and adds them to weatherPointList
     * @param groundWeatherPoint weather data at ground-height
     */
    private fun generateWeatherPointList(
        groundWeatherPoint: WeatherPoint
    ): MutableStateFlow<List<WeatherPoint>> {
        val pressureAtSeaLevel = groundWeatherPoint.pressure

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
        return weatherPointList

    }

    override suspend fun getListOfWeatherPointsLists(
        from: Int,
        to: Int
    ): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        // adding this existing weather point list to the list of lists
        //index 2 : NO time, index 53 - 48 hours from NO time
        for (index in from..to) {
            listOfWeatherPointLists.add(generateWeatherPointList(generateGroundWeatherPoint(index)))
        }
        flowOfWeatherPointLists = MutableStateFlow(listOfWeatherPointLists)
        return flowOfWeatherPointLists
    }

    /**
     * Parses data from locationForecastData into a WeatherPoint object
     * @return weatherpoint with ground level information
     * @author Lelia
     */
    private fun generateGroundWeatherPoint(hourIndex: Int): WeatherPoint {
        //rain data available: next hour, next 6 hours, next 12 hours.
        //default rain: next hour.
        //default time: UTC

        //Compute local time from UTC + timezone + DST.
        // UTC time for current day is at index 0 - Norwegian time is UTC + 2 (from last sunday in March) during summer and UTC + 1 during winter (from last sunday in October) .
        // DST changes: Last sunday in March and last sunday in October

        changeDSTBasedOnDate() //resetting addDaylightSave to 1 or 2 based on current month

        val timeSeriesInstantDetails: Details? = // Reduces boilerplate later on
            locationForecastData.value.properties?.timeseries?.getOrNull(hourIndex)?.data?.instant?.details

        return WeatherPoint(
            windSpeed = timeSeriesInstantDetails!!.wind_speed,
            windDirection = timeSeriesInstantDetails.wind_from_direction,
            temperature = timeSeriesInstantDetails.air_temperature,
            pressure = timeSeriesInstantDetails.air_pressure_at_sea_level,
            cloudFraction = timeSeriesInstantDetails.cloud_area_fraction,
            rain = locationForecastData.value.properties!!.timeseries.getOrNull(hourIndex)!!.data.next_1_hours.details.precipitation_amount,
            humidity = timeSeriesInstantDetails.relative_humidity,
            height = locationForecastData.value.geometry?.coordinates?.getOrNull(2)!!,
            dewPoint = timeSeriesInstantDetails.dew_point_temperature,
            fog = timeSeriesInstantDetails.fog_area_fraction
        )
    }

    fun changeDSTBasedOnDate() {
        val calendar: Calendar = Calendar.getInstance()
        val dayOfWeek: Int =
            calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) //keeping here for more detailed computing later
        val dayOfMonth: Int =
            calendar.get(Calendar.DAY_OF_MONTH) //keeping here for more detailed computing later
        val month: Int = calendar.get(Calendar.MONTH)

        // Note: months are 0-based (January is 0)
        // Change the value of the variable based on the date
        if (month >= 3 && month <= 10) { //April to (including) October.
            hourIndex = 2
        } else { // December - March
            hourIndex = 1
        }
    }
}

// TODO: create KDoc
internal fun calculateHeight(
    pressure: Double, temperature: Double, pressureAtSeaLevel: Double
): Double {
    //https://en.wikipedia.org/wiki/Barometric_formula
    val tempInKelvin = temperature + 273.15

    /* round(
        (MOLAR_GAS_CONSTANT / GRAVITATIONAL_ACCELERATION) * tempInKelvin
                * ln((pressureAtSeaLevel / pressure))
    ) */

    return round(
        (-MOLAR_GAS_CONSTANT * tempInKelvin) * ln(pressure / pressureAtSeaLevel) /
                (GRAVITATIONAL_ACCELERATION * MOLAR_MASS_OF_AIR)
    )
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
/*internal fun computeDewPointGround(temperature: Double?, relativeHumidity: Double?): Double {
    //https://iridl.ldeo.columbia.edu/dochelp/QA/Basic/dewpoint.html
    // accuracy within 1 Celcius for relative humidity over 50%
    if (temperature != null && relativeHumidity != null) {
        return round((temperature - ((100 - relativeHumidity) / 5)))
    }
    return -1.0
}

 */