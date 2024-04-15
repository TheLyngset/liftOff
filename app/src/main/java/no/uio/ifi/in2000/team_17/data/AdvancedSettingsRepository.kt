package no.uio.ifi.in2000.team_17.data

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team17.AdvancedSettings

private val LOG_NAME = "ADVANCED_SETTINGS_REPOSITORY"
class AdvancedSettingsRepository(private val advancedSettingsStore: DataStore<AdvancedSettings>) {
    val advancedSettingsFlow: Flow<AdvancedSettings> = advancedSettingsStore.data

    suspend fun setGroundWind(groundWind: Double){
        Log.d(LOG_NAME, "setting ground wind to $groundWind")
        advancedSettingsStore.updateData {
            it.toBuilder().setGroundWindSpeed(groundWind).build()
        }
    }
    suspend fun setMaxWind(maxWind: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setMaxWindSpeed(maxWind).build()
        }
    }
    suspend fun setMaxWindShear(maxWindShear: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setMaxWindShear(maxWindShear).build()
        }
    }
    suspend fun setCloudFraction(fraction: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setCloudFraction(fraction).build()
        }
    }
    suspend fun setFog(fog: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setFog(fog).build()
        }
    }
    suspend fun setRain(rain: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setRain(rain).build()
        }
    }
    suspend fun setHumidity(humidity: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setHumidity(humidity).build()
        }
    }
    suspend fun setDewPoint(dewPoint: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setDewPoint(dewPoint).build()
        }
    }
    suspend fun setMargin(margin: Double){
        advancedSettingsStore.updateData {
            it.toBuilder().setMargin(margin).build()
        }
    }
    suspend fun reset(){
        advancedSettingsStore.updateData {
            AdvancedSettingsSerializer.defaultValue
        }
    }
}