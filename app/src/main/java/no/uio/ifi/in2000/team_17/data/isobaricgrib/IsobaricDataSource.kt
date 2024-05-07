package no.uio.ifi.in2000.team_17.data.isobaricgrib

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson


/**
 * Handles retrieval of isobaric data from remote service.
 *
 * This class is responsible for building the query URL and performing HTTP GET requests
 * to fetch isobaric data from an Azure server, which typically includes information
 * such as wind speed, temperature, pressure, etc for layers over the geographic location.
 *
 * The class abstracts away the networking logic and error handling, providing a simple interface
 * for retrieving the IsoBaricModel objects that contain the requested isobaric data.
 */
enum class IsoBaricTime(val URL: String) {
    NOW("http://158.39.75.8:5000/collections/isobaric/position"),
    IN_3("http://158.39.75.8:5001/collections/isobaric/position"),
    IN_6("http://158.39.75.8:5002/collections/isobaric/position")
}

class IsobaricDataSource {
    private val LOG_NAME = "ISOBARIC_DATASOURCE"
    private val client = HttpClient {
        install(ContentNegotiation) { gson() }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    // Returns empty IsoBaricModel object on fail
    suspend fun getData(
        lat: Double,
        lng: Double,
        isoBaricTime: IsoBaricTime = IsoBaricTime.NOW
    ): IsoBaricModel {
        val queryUrl = "${isoBaricTime.URL}?coords=POINT%28$lng%20$lat%29"
        Log.d(LOG_NAME, "Attempting to fetch data from: $queryUrl")
        return client.get(queryUrl).body<IsoBaricModel>()
    }

}