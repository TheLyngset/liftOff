package no.uio.ifi.in2000.team_17

import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.data.settings.SettingsSerializer
import no.uio.ifi.in2000.team_17.ui.App
import no.uio.ifi.in2000.team_17.ui.splash_screen.SplashScreenViewModel

val Context.thresholdsDataStore: DataStore<Thresholds> by dataStore(
    fileName = "advanced_settings",
    serializer = ThresholdsSerializer
)
val Context.settingsStore: DataStore<Settings> by dataStore(
    fileName = "settings",
    serializer = SettingsSerializer
)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.getWindow().requestFeature(Window.FEATURE_ACTION_BAR)
        super.getActionBar()?.hide()
        setContent {
            val splashScreenViewModel: SplashScreenViewModel = viewModel(
                factory = viewModelFactory {
                    SplashScreenViewModel(
                        App.appModule.repository,
                        App.appModule.settingsRepository,
                    )
                }
            )
            val uiState by splashScreenViewModel.uiState.collectAsState()

            installSplashScreen().apply {
                this.setKeepOnScreenCondition {
                    uiState.isLoading
                }
            }

            val windowSizeClass = calculateWindowSizeClass(this)
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    if (!uiState.hasData) {
                        Box(
                            Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ElevatedCard {
                                Row(Modifier.padding(16.dp)) {
                                    Text(text = "No internet connection, make sure you are connected to the internet and relaunch the app")
                                }
                            }
                        }
                    } else {
                        App(
                            windowSizeClass = windowSizeClass,
                        )
                    }
                }
            }
        }
    }
}

