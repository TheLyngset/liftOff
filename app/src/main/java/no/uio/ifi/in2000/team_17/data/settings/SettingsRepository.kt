package no.uio.ifi.in2000.team_17.data.settings

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team17.Settings

/**
 * a repository for reading and saving Settings using proto Data Store
 */
class SettingsRepository(private val settingsStore: DataStore<Settings>) {
    val settingsFlow: Flow<Settings> = settingsStore.data

    suspend fun setMaxHeight(height: Int) {
        settingsStore.updateData { it.toBuilder().setMaxHeight(height).build() }
    }

    suspend fun setLat(lat: Double) {
        settingsStore.updateData { it.toBuilder().setLat(lat).build() }
    }

    suspend fun setLng(lng: Double) {
        settingsStore.updateData { it.toBuilder().setLng(lng).build() }
    }

    suspend fun setTime(time: String) {
        settingsStore.updateData { it.toBuilder().setTime(time).build() }
    }

    suspend fun setGraphShowTutorial(show: Boolean) {
        settingsStore.updateData { it.toBuilder().setGraphShowTutorial(show).build() }
    }

    suspend fun setTableShowTutorial(show: Boolean) {
        settingsStore.updateData { it.toBuilder().setTableShowTutorial(show).build() }
    }

    suspend fun setGraphBackgroundSwitch(switch: Boolean) {
        settingsStore.updateData { it.toBuilder().setGraphBackgroundSwitch(switch).build() }
    }
}