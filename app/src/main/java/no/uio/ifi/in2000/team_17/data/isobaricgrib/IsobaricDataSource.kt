package no.uio.ifi.in2000.team_17.data.isobaricgrib

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import java.nio.channels.UnresolvedAddressException
import com.google.gson.annotations.SerializedName
import no.uio.ifi.in2000.team_17.data.isobaricgrib.model.IsoBaricModel


val LOG_NAME = "ISOBARICDATASOURCE"
class IsobaricDataSource{
    val client = HttpClient { install(ContentNegotiation) { gson() } }

    val BASE_URL = "http://20.100.26.176:5000/collections/isobaric/position"
    fun makeQueryUrl(north: Double,east: Double ): String {
        val queryString = "$BASE_URL?coords=POINT%28$east%20$north%29"
        // Log.v(logName, "queryUrl is $queryString")
        return queryString
    }
    suspend fun getData(north:Double, east:Double): IsoBaricModel {
        return try {
            Log.d(LOG_NAME, makeQueryUrl(north, east))
            client.get(makeQueryUrl(north, east)).body<IsoBaricModel>()
        }catch (e: UnresolvedAddressException){
            Log.d(LOG_NAME, "Unresolved adress")
            IsoBaricModel()
        }
    }
}