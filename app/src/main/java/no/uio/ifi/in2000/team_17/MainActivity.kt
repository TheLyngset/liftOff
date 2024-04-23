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
import com.example.compose.AppTheme
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.data.settings.SettingsSerializer
import no.uio.ifi.in2000.team_17.ui.App

val Context.thresholdsDataStore: DataStore<Thresholds> by dataStore(
    fileName = "advanced_settings",
    serializer = ThresholdsSerializer
)
val Context.settingsStore: DataStore<Settings> by dataStore(
    fileName = "settings",
    serializer = SettingsSerializer
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
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

