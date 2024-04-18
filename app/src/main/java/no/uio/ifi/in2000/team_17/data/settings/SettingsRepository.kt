package no.uio.ifi.in2000.team_17.data.settings

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team17.Settings

class SettingsRepository(private val settingsStore: DataStore<Settings>) {
    val settingsFlow: Flow<Settings> = settingsStore.data

    suspend fun setMaxHeight(height:Int){
        settingsStore.updateData { it.toBuilder().setMaxHeight(height).build() }
    }
    suspend fun setLat(lat: Double){
        settingsStore.updateData { it.toBuilder().setLat(lat).build() }
    }
    suspend fun setLng(lng: Double){
        settingsStore.updateData { it.toBuilder().setLng(lng).build() }
    }
    suspend fun setTimeIndex(index: Int){
        settingsStore.updateData { it.toBuilder().setTimeIndex(index).build() }
    }
}