package no.uio.ifi.in2000.team_17.data

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricDataSource
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.team_17.model.IsoBaricModel
import no.uio.ifi.in2000.team_17.model.WeatherPointNew
import no.uio.ifi.in2000.team_17.model.WeatherPointOld
import no.uio.ifi.in2000.team_17.model.WeatherPointsResults
import no.uio.ifi.in2000.team_17.model.weatherDTO.Details
import no.uio.ifi.in2000.team_17.model.weatherDTO.LocationforecastDTO
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsUiState
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    ): List<WeatherPointNew>

    suspend fun getWeatherPointList(): StateFlow<List<WeatherPointOld>>
    fun setSettings(advancedSettingsUiState: AdvancedSettingsUiState)
    suspend fun updatedAt(): String
    suspend fun getListOfWeatherPointsNew(): List<WeatherPointNew>
    suspend fun getWeatherPointsResults(): WeatherPointsResults
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
    private var dayligthSavingAdd = 2
    private var coordinates: LatLng = LatLng(59.96144907197439, 10.713250420850423)
    private var maxHeight: Int = 3

    // Load data from isoBaricDataSource and locationForecast
    private val locationForecastDataSource: LocationForecastDataSource =
        LocationForecastDataSource()
    private val isobaricDataSource: IsobaricDataSource = IsobaricDataSource()

    // Creates necessary StateFlows
    private val isoBaricData = MutableStateFlow(IsoBaricModel())
    private val locationForecastData = MutableStateFlow(LocationforecastDTO(null, null, null))

    //makes a list of weatherPoint with ground level data and lists of layers in alltitude
    val weatherPointList = MutableStateFlow<List<WeatherPointOld>>(listOf())
    var flowOfWeatherPointLists =
        MutableStateFlow(mutableListOf<MutableStateFlow<List<WeatherPointOld>>>())
    var listOfWeatherPointLists = mutableListOf<MutableStateFlow<List<WeatherPointOld>>>()

    //makes a list of weather points, each holding only data neeeded for the UI
    private var listOfWeatherPoints = mutableListOf(WeatherPointNew())
    override suspend fun getListOfWeatherPointsNew(): List<WeatherPointNew> {

        return listOfWeatherPoints
    }

    //updatable variable that holds an object with lists of values for each of the variables that are used in the ResultsUI
    private var weatherPointsResults = WeatherPointsResults()
    override suspend fun getWeatherPointsResults(): WeatherPointsResults {
        return weatherPointsResults
    }

    /**
     * Calls both loadLocationForecast() and loadIsoBaricData()
     * @param latLng is a latitude and longitude object
     * @param maxHeight sets maximum needed height for showing later
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(latLng: LatLng, heigth: Int) {
        coordinates = latLng
        maxHeight = heigth
        loadLocationForecast()
        loadIsobaric()
        generateWeatherPointList(generateGroundWeatherPoint(0))
    }

    override suspend fun getWeatherPointList(): StateFlow<List<WeatherPointOld>> {
        return weatherPointList.asStateFlow()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updatedAt(): String {
        val dateTimeString =
            locationForecastData.value.properties?.meta?.updated_at
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        val time = dateTime.toLocalTime()
        val updatedTime = time.plusHours(dayligthSavingAdd.toLong())
        return updatedTime.toString()
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
     * based on locationForecasst ground weather points creates [WeatherPointOld]'s.
     * These are loaded into weatherPointList
     * Creates weatherpoints and adds them to weatherPointList
     * @param groundWeatherPoint weather data at ground-height
     */
    private fun generateWeatherPointList(
        groundWeatherPoint: WeatherPointOld
    ): MutableStateFlow<List<WeatherPointOld>> {
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
                    WeatherPointOld(
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

        //safechecks maxwind speed and share before adding it to the weather point object
        var maxWindSpeed = windSpeed.maxOrNull()
        var maxWindShear = windShear.maxOrNull()

        if (maxWindSpeed == null) {
            maxWindSpeed = -1.0
        }
        if (maxWindShear == null) {
            maxWindShear = -1.0
        }
        //adds the relevant values to a weatherPoint that holds only information that is needed for the Ui
        val newWeatherPoint = WeatherPointNew(
            date = groundWeatherPoint.date,
            time = groundWeatherPoint.time,
            groundWindSpeed = groundWeatherPoint.windSpeed,
            maxWindSpeed = maxWindSpeed,
            maxWindShear = maxWindShear,
            cloudFraction = groundWeatherPoint.cloudFraction,
            rain = groundWeatherPoint.rain,
            humidity = groundWeatherPoint.humidity,
            dewPoint = groundWeatherPoint.dewPoint,
            fog = groundWeatherPoint.fog
        )
        //adds the weatherpoint above to a list of weather points
        listOfWeatherPoints.add(newWeatherPoint)

        ///adding values to weatherPointsResults for each variable used for the results UI
        groundWeatherPoint.date?.let { weatherPointsResults.date.add(it) }
        groundWeatherPoint.time?.let { weatherPointsResults.time.add(it) }
        groundWeatherPoint.windSpeed?.let { weatherPointsResults.groundWindSpeed.add(it) }
        maxWindSpeed?.let { weatherPointsResults.maxWindSpeed.add(it) }
        maxWindShear?.let { weatherPointsResults.maxWindShear.add(it) }
        groundWeatherPoint.cloudFraction?.let { weatherPointsResults.cloudFraction.add(it) }
        groundWeatherPoint.rain?.let { weatherPointsResults.rain.add(it) }
        groundWeatherPoint.humidity?.let { weatherPointsResults.humidity.add(it) }
        groundWeatherPoint.dewPoint?.let { weatherPointsResults.dewPoint.add(it) }
        groundWeatherPoint.fog?.let { weatherPointsResults.fog.add(it) }


        //this is the pointList used on the homescreen in MVP
        return weatherPointList

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getListOfWeatherPointsLists(
        from: Int,
        to: Int
    ): List<WeatherPointNew> {
        // adding this existing weather point list to the list of lists
        //index 2 : norwegian time, index 53 - 48 hours from NO time
        for (index in from..to) {
            listOfWeatherPointLists.add(
                generateWeatherPointList(
                    generateGroundWeatherPoint(
                        index
                    )
                )
            )
        }
        flowOfWeatherPointLists = MutableStateFlow(listOfWeatherPointLists)

        return listOfWeatherPoints.toList()
    }

    override fun setSettings(advancedSettingsUiState: AdvancedSettingsUiState) {
        Log.d(LOG_NAME, "${advancedSettingsUiState.groundWindSpeed}")
    }

    /**
     * Parses data from locationForecastData into a WeatherPoint object
     * @return weatherpoint with ground level information
     * @author Lelia
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateGroundWeatherPoint(hourIndex: Int): WeatherPointOld {

        changeDSTBasedOnDate() //resetting addDaylightSave to 1 or 2 based on current month
        val timeSerieDetails: Details? =
            locationForecastData.value.properties?.timeseries?.getOrNull(hourIndex)?.data?.instant?.details

        //dateTime extraction
        val dateTimeString =
            locationForecastData.value.properties?.timeseries?.getOrNull(hourIndex)?.time

        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)

        val date = dateTime.toLocalDate()
        val time = dateTime.toLocalTime()
        val updatedTime = time.plusHours(dayligthSavingAdd.toLong())


        return WeatherPointOld(
            date = date.toString(),
            time = (updatedTime).toString(),
            windSpeed = timeSerieDetails!!.wind_speed,
            windDirection = timeSerieDetails.wind_from_direction,
            temperature = timeSerieDetails.air_temperature,
            pressure = timeSerieDetails.air_pressure_at_sea_level,
            cloudFraction = timeSerieDetails.cloud_area_fraction,
            rain = locationForecastData.value.properties!!.timeseries.getOrNull(hourIndex)!!.data.next_1_hours.details.precipitation_amount,
            humidity = timeSerieDetails.relative_humidity,
            height = locationForecastData.value.geometry?.coordinates?.getOrNull(2)!!,
            dewPoint = timeSerieDetails.dew_point_temperature,
            fog = timeSerieDetails.fog_area_fraction
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
            dayligthSavingAdd = 2
        } else { // December - March
            dayligthSavingAdd = 1
        }
    }
}

// TODO: create KDoc
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

