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
// import no.uio.ifi.in2000.team_17.data.locationforecast.LocationforecastKey

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
    suspend fun fetchLocationforecast(): LocationforecastDTO {
        var lat = "61"
        var lon = "10"
        //  var path: String = "https://gw-uiointark.uh-it.no/in2000/weatherapi//locationforecast/2.0/compact?lat=$lat&lon=$lon"
        //var path = "gw-uio.intark.uh-it.no/in2000/weatherapi//locationforecast/2.0/compact?lat=${lat}lon=${lon}"
        var path = "weatherapi//locationforecast/2.0/compact?lat=61&lon=10"

        val response = client.get(path)
        Log.d("FetchLocationforecast", "$response.status")

        return response.body<LocationforecastDTO>()
    }
   /* suspend fun getWeatherDataLocation(lat: Double, lon: Double): FoodCategory?:Meal? {
   val categoryString = when((lat){
   FoodCatefory.CKICKEN -> "Chicken"
   }
   client.get("filter.php?C=seafood")

    }

    */
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

