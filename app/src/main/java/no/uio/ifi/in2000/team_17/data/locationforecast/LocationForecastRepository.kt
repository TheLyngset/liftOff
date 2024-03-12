package no.uio.ifi.in2000.team_17.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.WeatherPoint
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.LocationforecastDTO
import java.io.IOException

interface LocationForecastRepository{
   // suspend fun getLocationforecastData(lat: Double, lon: Double): LocationforecastDTO
    suspend fun getGroundWeatherPoint(lan: Double, lon: Double, index: Int): WeatherPoint
}
class LocationForecastRepositoryImplementation (
    private val locationforecastDataSource: LocationForecastDataSource = LocationForecastDataSource()
) : LocationForecastRepository  {
    suspend fun getLocationforecastData(lat: Double, lon: Double): LocationforecastDTO {
        var locationforecastData = LocationforecastDTO(null, null, null)
        try {
            locationforecastData = locationforecastDataSource.fetchLocationforecast(lat, lon)
        } catch (e: IOException) {
            // log the exception
            Log.e("LOCATIONFORECAST_REPOSITORY", "Error while fetching Locationforecast data: ${e.message}")
        } catch (e: Exception) {
            Log.e("LOCATIONFORECAST_REPOSITORY", "Error while fetching Locationforecast data: ${e.message}")
        }
        //Log.d("REPOSITORY", "type: ${locationforecastData.type}")

        return locationforecastData
    }

    /* funksjoner som returnenre kun den dataen vi er interessert i.*/

    //rename this to ground weather point
    override suspend fun getGroundWeatherPoint(lat: Double, lon: Double, index: Int): WeatherPoint {
        val allLocationData = getLocationforecastData(lat, lon)
        val windSpeed: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details?.wind_speed
        val windFromDirection: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details?.wind_from_direction
        val airTemperature: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details?.air_temperature
        val pressureSeaLevel: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details?.air_pressure_at_sea_level
        val cloudFraction: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details?.cloud_area_fraction
        val rain: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.next_1_hours?.details?.precipitation_amount
        val humidity: Double? = allLocationData.properties?.timeseries?.getOrNull(index)?.data?.instant?.details?.relative_humidity

        //var weatherPoint = GroundWeatherPoint(windSpeed, windFromDirection, airTemperature, pressureSeaLevel,cloudFraction,rain, humidity, 0.0)
        val weatherPoint = WeatherPoint(
            windSpeed= windSpeed!!,
            windDirection =  windFromDirection!!,
            temperature =  airTemperature!!,
            pressure = pressureSeaLevel!!,
            cloudFraction =  cloudFraction!!,
            rain = rain!!,
            humidity = humidity!!,
            height = 0.0
        )

        return weatherPoint
    }
    
}