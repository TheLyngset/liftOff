package no.uio.ifi.in2000.team_17.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.LocationforecastDTO
import java.io.IOException

interface LocationForecastRepository{
    suspend fun getLocationforecastData(): LocationforecastDTO
    suspend fun getTemperature(): Double?
}
class LocationForecastRepositoryImplementation (
    private val locationforecastDataSource: LocationForecastDataSource = LocationForecastDataSource()
) : LocationForecastRepository  {
    override suspend fun getLocationforecastData(): LocationforecastDTO {
        var locationforecastData = LocationforecastDTO(null, null, null)
        try {
            locationforecastData = locationforecastDataSource.fetchLocationforecast()
        } catch (e: IOException) {
            // log the exception
            Log.e("LOCATIONFORECAST_REPOSITORY", "Error while fetching Locationforecast data: ${e.message}")
        } catch (e: Exception) {
            Log.e("LOCATIONFORECAST_REPOSITORY", "Error while fetching Locationforecast data: ${e.message}")
        }
        //Log.d("REPOSITORY", "type: ${locationforecastData.type}")

        return locationforecastData
    }

    override suspend fun getTemperature(): Double? {
        val all = locationforecastDataSource.fetchLocationforecast()
        val temp: Double? = all.properties?.timeseries?.firstOrNull()?.data?.instant?.details?.air_temperature
        return temp
    }
    /* funksjoner som returnenre kun den dataen vi er interessert i.*/
}