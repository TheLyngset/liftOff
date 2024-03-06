package no.uio.ifi.in2000.team_17.data.Locationforecast

import android.util.Log
import java.io.IOException

interface LocationForecastRepository{
    suspend fun getLocationforecastData(): LocationforecastWeatherData
}
class LocationForecastRepositoryImplementation (
    private val locationforecastDataSource: LocationForecastDataSource = LocationForecastDataSource()
) : LocationForecastRepository  {
    override suspend fun getLocationforecastData(): LocationforecastWeatherData {
        var locationforecastData = LocationforecastWeatherData(null)
        try {
            locationforecastData = locationforecastDataSource.fetchLocationforecast()
        } catch (e: IOException) {
            // log the exception
            Log.e("LOCATIONFORECAST_REPOSITORY", "Error while fetching Locationforecast data: ${e.message}")
        } catch (e: Exception) {
            Log.e("LOCATIONFORECAST_REPOSITORY", "Error while fetching Locationforecast data: ${e.message}")
        }
        return locationforecastData
    }
}