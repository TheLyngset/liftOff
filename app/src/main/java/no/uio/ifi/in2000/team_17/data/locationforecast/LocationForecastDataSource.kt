package no.uio.ifi.in2000.team_17.data.locationforecast

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.LocationforecastDTO

private const val LOG_NAME = "LOCATION_FORECAST_DATASOURCE"

/**
 * This class is responsible for fetching location forecast data from the API.
 */
class LocationForecastDataSource {
    private val apiKey = LocationforecastKey()
    private val keyValue = apiKey.getAPIKey()

    /**
     * HttpClient is used to send HTTP requests.
     * The default request URL is set to the API endpoint.
     * The API key is added to the headers of the request.
     * Gson is used for content negotiation.
     */
    private val client = HttpClient {
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            headers.appendIfNameAbsent("X-Gravitee-API-Key", keyValue)
        }
        install(ContentNegotiation) {
            gson()
        }
    }


    /**
     * This function fetches the location forecast for the given latitude and longitude.
     * @param lat The latitude for which the forecast is to be fetched.
     * @param lon The longitude for which the forecast is to be fetched.
     * @return The location forecast data.
     */
    suspend fun fetchLocationforecast(lat: Double, lon: Double): LocationforecastDTO {

        val path = "weatherapi//locationforecast/2.0/complete?lat=${lat}&lon=${lon}"

        Log.e(
            LOG_NAME,
            "Attempting to fetch data from: https://gw-uio.intark.uh-it.no/in2000/$path "
        )
        val response = client.get(path)
        Log.d("FetchLocationforecast", "${response.status}")

        return response.body<LocationforecastDTO>()
    }
}


