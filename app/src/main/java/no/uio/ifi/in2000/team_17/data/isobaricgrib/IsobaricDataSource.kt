package no.uio.ifi.in2000.team_17.data.isobaricgrib

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel
import java.io.IOException


const val LOG_NAME = "ISOBARIC_DATASOURCE"


class IsobaricDataSource {
    private val BASE_URL = "http://20.100.26.176:5000/collections/isobaric/position"

    private val client = HttpClient { install(ContentNegotiation) { gson() } }
    private fun makeQueryUrl(north: Double, east: Double): String {
        return "$BASE_URL?coords=POINT%28$east%20$north%29"
    }


    // Returns empty IsoBaricModel object on fail
    suspend fun getData(north: Double, east: Double): IsoBaricModel {
        return try {
            Log.d(LOG_NAME, makeQueryUrl(north, east))
            client.get(makeQueryUrl(north, east)).body<IsoBaricModel>()
        } catch (e: IOException) {
            Log.e(LOG_NAME, "Got an IO exception $e")
            IsoBaricModel()
        }
    }
}