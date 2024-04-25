package no.uio.ifi.in2000.team_17.data.thresholds

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team17.Thresholds

private val LOG_NAME = "ADVANCED_SETTINGS_REPOSITORY"

/**
 * a repository for reading and saving Thresholds using proto Data Store
 */
class ThresholdsRepository(private val thresholdsDataStore: DataStore<Thresholds>) {
    val thresholdsFlow: Flow<Thresholds> = thresholdsDataStore.data


    suspend fun setGroundWind(groundWind: Double){
        Log.d(LOG_NAME, "setting ground wind to $groundWind")
        thresholdsDataStore.updateData {
            it.toBuilder().setGroundWindSpeed(groundWind).build()
        }
    }
    suspend fun setMaxWind(maxWind: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setMaxWindSpeed(maxWind).build()
        }
    }
    suspend fun setMaxWindShear(maxWindShear: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setMaxWindShear(maxWindShear).build()
        }
    }
    suspend fun setCloudFraction(fraction: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setCloudFraction(fraction).build()
        }
    }
    suspend fun setFog(fog: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setFog(fog).build()
        }
    }
    suspend fun setRain(rain: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setRain(rain).build()
        }
    }
    suspend fun setHumidity(humidity: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setHumidity(humidity).build()
        }
    }
    suspend fun setDewPoint(dewPoint: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setDewPoint(dewPoint).build()
        }
    }
    suspend fun setMargin(margin: Double){
        thresholdsDataStore.updateData {
            it.toBuilder().setMargin(margin).build()
        }
    }
    suspend fun reset(threshold: String){
        val default = ThresholdsSerializer.defaultValue
        when(threshold){
            "groundWind" -> setGroundWind(default.groundWindSpeed)
            "maxWind" -> setMaxWind(default.maxWindSpeed)
            "maxShear" -> setMaxWindShear(default.maxWindShear)
            "cloud" -> setCloudFraction(default.cloudFraction)
            "fog" -> setFog(default.fog)
            "rain" -> setRain(default.rain)
            "humidity" -> setHumidity(default.humidity)
            "dewPoint" -> setDewPoint(default.dewPoint)
            "margin" -> setMargin(default.margin)
        }
    }
}