package no.uio.ifi.in2000.team_17.data

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsoBaricTime
import no.uio.ifi.in2000.team_17.data.isobaricgrib.IsobaricDataSource
import no.uio.ifi.in2000.team_17.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.team_17.model.IsoBaricModel
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.weatherDTO.LocationforecastDTO
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

interface newRepository {
    suspend fun load(latLng: LatLng, maxHeight: Int)
    val weatherDataLists: StateFlow<WeatherDataLists>
}

class newRepositoryImplementation(private val context: Context) : newRepository {
    private val LOG_NAME = "REPOSITORY"
    private var dayligthSavingAdd = 2

    // Load data from isoBaricDataSource and locationForecast
    private val locationForecastDataSource: LocationForecastDataSource =
        LocationForecastDataSource()
    private val isobaricDataSource: IsobaricDataSource = IsobaricDataSource()

    // Creates necessary StateFlows
    private val isoBaricData = MutableStateFlow(listOf(IsoBaricModel()))
    private val locationForecastData = MutableStateFlow(LocationforecastDTO(null, null, null))

    private val _weatherDataLists = MutableStateFlow(WeatherDataLists())
    override val weatherDataLists = _weatherDataLists.asStateFlow()

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

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadIsobaric(latLng: LatLng) {
        val startIndex = LocalDateTime.now(ZoneId.systemDefault()).hour % 3
        var newIsoBaricModel = mutableListOf<IsoBaricModel>()
        try {
            for (i: Int in startIndex..3) {
                newIsoBaricModel.add(
                    isobaricDataSource.getData(
                        latLng.latitude,
                        latLng.longitude,
                        IsoBaricTime.NOW
                    )
                )
            }
            for (i: Int in 1..3) {
                newIsoBaricModel.add(
                    isobaricDataSource.getData(
                        latLng.latitude,
                        latLng.longitude,
                        IsoBaricTime.IN_3
                    )
                )
            }
            for (i: Int in 1..6) {
                newIsoBaricModel.add(
                    isobaricDataSource.getData(
                        latLng.latitude,
                        latLng.longitude,
                        IsoBaricTime.IN_9_OR_12
                    )
                )
            }
        } catch (e: IOException) {
            Log.e(LOG_NAME, "Error while fetching isobaric data: ${e.message}")
            IsoBaricModel()
        }
    }

    override suspend fun load(latLng: LatLng, maxHeight: Int) {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateWeatherDataLists() {
        val listLength = locationForecastData.value.properties?.timeseries?.size
        val date: List<String> = try {
            locationForecastData.value.properties?.timeseries?.map {
                LocalDateTime
                    .parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                    .toLocalDate()
                    .toString()
            }!!
        } catch (e: NullPointerException) {
            e.printStackTrace()
            listOf("Date not found")
        }
        val time: List<String> = try {
            locationForecastData.value.properties?.timeseries?.map {
                LocalDateTime
                    .parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                    .toLocalTime()
                    .plusHours(2) //summertime
                    .toString()
            }!!
        } catch (e: NullPointerException) {
            e.printStackTrace()
            listOf("Time not found")
        }

        val groundWind = try {
            locationForecastData.value.properties?.timeseries?.map {
                WindLayer(
                    speed = it.data.instant.details.wind_speed,
                    height = 10.0,
                    direction = it.data.instant.details.wind_from_direction
                )
            }!!
        } catch (e: NullPointerException) {
            e.printStackTrace()
            listOf(WindLayer())
        }

        val maxWindShear = try {
            isoBaricData.value.map {
                val windSpeed = it.ranges.windSpeed
                val windFromDirection = it.ranges.windFromDirection
                val windShear = mutableListOf<Double>()

                // windSpeed.zip(windFromDirection)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            listOf(WindLayer())
        }
        _weatherDataLists.update {
            it.copy(
            )
        }
    }

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