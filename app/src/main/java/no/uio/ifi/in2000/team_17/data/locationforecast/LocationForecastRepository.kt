package no.uio.ifi.in2000.team_17.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.team_17.data.WeatherPoint
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.Details
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.LocationforecastDTO
import java.io.IOException
import kotlin.math.round

interface LocationForecastRepository {
    // suspend fun getLocationforecastData(lat: Double, lon: Double): LocationforecastDTO
    suspend fun getGroundWeatherPoint(lat: Double, lon: Double, index: Int): WeatherPoint
}

class LocationForecastRepositoryImplementation(
    private val locationforecastDataSource: LocationForecastDataSource = LocationForecastDataSource()
) : LocationForecastRepository {
    private val LOG_NAME = "LOCATION_FORECAST_REPOSITORY"

    /**
     * Fetches locationforecastData via the dataSource. If exception is thrown, it will return an
     * empty LocationforecastDTO object
     * @param lat coordinates on latitude
     * @param lon coordinates on longitude
     * @return LocationforecastDTO()
     */
    suspend fun getLocationForecastData(lat: Double, lon: Double): LocationforecastDTO {
        var locationforecastData = LocationforecastDTO(null, null, null)
        try {
            locationforecastData = locationforecastDataSource.fetchLocationforecast(lat, lon)
        } catch (e: IOException) {
            Log.e(LOG_NAME, "Error while fetching Locationforecast data: ${e.message}")
        } catch (e: Exception) {
            Log.e(LOG_NAME, "Error while fetching Locationforecast data: ${e.message}")
        }

        return locationforecastData
    }

    /**
     * Fetches locationForecastData via getLocationForecastData() and parses it into a
     * Weatherpoint() data object, to later be used as ground point
     * @param lat coordinates on latitude
     * @param lon coordinates on longitude
     * @param index the index is used to get the wanted start timeframe
     * @return weatherpoint with ground level information
     */
    override suspend fun getGroundWeatherPoint(lat: Double, lon: Double, index: Int): WeatherPoint {
        val allLocationData = getLocationForecastData(lat, lon)

        val timeSeriesInstantDetails: Details? = // Reduces boilerplate later on
            allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details

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
            allLocationData.properties?.timeseries?.getOrNull(index)?.data?.next_1_hours?.details?.precipitation_amount
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