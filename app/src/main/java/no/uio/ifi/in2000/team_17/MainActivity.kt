package no.uio.ifi.in2000.team_17

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import no.uio.ifi.in2000.team17.AdvancedSettings
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team_17.data.AdvancedSettingsSerializer
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.RepositoryImplementation
import no.uio.ifi.in2000.team_17.data.SettingsSerializer
import no.uio.ifi.in2000.team_17.ui.App
import no.uio.ifi.in2000.team_17.ui.theme.Team17Theme

val Context.advancedSettingsStore: DataStore<AdvancedSettings> by dataStore(
    fileName = "advanced_settings",
    serializer = AdvancedSettingsSerializer
)
val Context.settingsStore: DataStore<Settings> by dataStore(
    fileName = "settings",
    serializer = SettingsSerializer
)
class MainActivity : ComponentActivity() {
    val repository: RepositoryImplementation by lazy {
        RepositoryImplementation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Team17Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

