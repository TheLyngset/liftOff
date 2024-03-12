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

class LocationForecastDataSource() {
    private val ApiKey = LocationforecastKey()
    private val keyValue = ApiKey.getAPIKey()


    private val client = HttpClient () {
           defaultRequest {
               url("https://gw-uio.intark.uh-it.no/in2000/")
                headers.appendIfNameAbsent("X-Gravitee-API-Key", keyValue)
           }
            install(ContentNegotiation) {
                gson()
            }
        }

    suspend fun fetchLocationforecast(lat: Double, lon: Double): LocationforecastDTO {

        var path = "weatherapi//locationforecast/2.0/compact?lat=${lat}&lon=${lon}"

        val response = client.get(path)
        Log.d("FetchLocationforecast", "$response.status")

        return response.body<LocationforecastDTO>()
    }
}


