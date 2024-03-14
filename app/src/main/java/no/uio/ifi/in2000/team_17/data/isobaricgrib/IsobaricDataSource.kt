package no.uio.ifi.in2000.team_17.data.isobaricgrib

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel
import java.io.IOException


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
class IsobaricDataSource {
    private val LOG_NAME = "ISOBARIC_DATASOURCE"
    private val BASE_URL = "http://20.100.26.176:5000/collections/isobaric/position"

    private val client = HttpClient { install(ContentNegotiation) { gson() } }
    private fun makeQueryUrl(north: Double, east: Double): String {
        return "$BASE_URL?coords=POINT%28$east%20$north%29"
    }


    // Returns empty IsoBaricModel object on fail
    suspend fun getData(north: Double, east: Double): IsoBaricModel {
        val queryUrl: String = makeQueryUrl(north, east)
        return try {
            Log.d(LOG_NAME, "Attempting to fetch data from: $queryUrl")
            client.get(queryUrl).body<IsoBaricModel>()
        } catch (e: IOException) {
            Log.e(LOG_NAME, "Got an IO exception while trying to fetch from $queryUrl", e)
            IsoBaricModel()
        }
    }
}