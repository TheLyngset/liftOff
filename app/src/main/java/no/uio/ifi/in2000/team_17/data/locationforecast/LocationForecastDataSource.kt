package no.uio.ifi.in2000.team_17.data.locationforecast

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO.LocationforecastDTO

/**
 * This class is responsible for fetching location forecast data from the API.
 */
class LocationForecastDataSource {
    private val ApiKey = LocationforecastKey()
    private val keyValue = ApiKey.getAPIKey()

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

        val response = client.get(path)
        Log.d("FetchLocationforecast", "${response.status}")

        return response.body<LocationforecastDTO>()
    }
}


