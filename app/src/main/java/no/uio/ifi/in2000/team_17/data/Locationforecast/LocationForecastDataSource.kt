package no.uio.ifi.in2000.team_17.data.Locationforecast

import android.util.Log
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.headersOf
import io.ktor.serialization.gson.gson

class LocationForecastDataSource() {
    private val ApiKey = LocationforecastKey()
    private val keyValue = ApiKey.getAPIKey()

    private val client = HttpClient () {
           defaultRequest {
                header("X-Gravitee-API-Key", keyValue)
           }
            install(ContentNegotiation) {
                gson()
            }
        }
    suspend fun fetchLocationforecast(): LocationforecastAllData {
        var lat = "61"
        var lon = "10"
       //var path = "gw-uio.intark.uh-it.no/in2000/weatherapi//locationforecast/2.0/compact?lat=${lat}lon=${lon}"
       var path ="https://gw-uio.intark.uh-it.no/in2000/weatherapi//locationforecast/2.0/compact?lat=61&lon=10"
        val response = client.get(path)
        Log.d("FetchLocationforecast", "$response.status")

        // Return the inner object from the deserialized object
     //   Log.d("ooo", response.status.toString())

        return response.body()
    }

}

/*
suspend fun main(){
    private val ApiKey = LocationforecastKey()
    private val keyValue = ApiKey.getAPIKey()

    val client = HttpClient(CIO)
    val path = "https://gw-uio.intark.uh-it.no/in2000/weatherapi//locationforecast/2.0/compact?lat=61&lon=10"
    try {
        val response: io.ktor.client.statement.HttpResponse = client.get(path)
        headersOf("X-Gravitee-API-Key", keyValue)
        Log.d("ooo", "OOOO $response.status.toString()")
    }

    catch (e: Exception) {
        println("Failed to make requedts ${e.message}")
        } finally {
        client.close()
    }
}

 */

