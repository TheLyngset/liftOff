package no.uio.ifi.in2000.team_17.data.Locationforecast

import android.util.Log
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.get
import io.ktor.client.request.header

class LocationForecastDataSource {
    val client = HttpClient(CIO) {
        defaultRequest {
            url("gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", "<4fc44a0a-d804-4696-9e91-bf936486aed5>")
        }
    }
    suspend fun fetchLocationforecast(): LocationforecastAllData {
        // Fetch and deserialize data from the API
        // (JSON -> Kotlin Data Class)
        val response = client.get("weatherapi/")

        // Logging is optional, this is an example of how to do it
        Log.d(
            "FORCAST_DATA_SOURCE",
            "forecastDataSource.fetchLocationforecast() HTTP status: ${response.status}"
        ) // log the HTTP status code

        // Deserialize the response body to on object
        val locationForcastAllData: LocationforecastAllData = response.body()

        // Return the inner object from the deserialized object
        return locationForcastAllData
    }

}