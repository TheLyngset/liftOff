package no.uio.ifi.in2000.team_17

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.team17.Settings
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.data.settings.SettingsSerializer
import no.uio.ifi.in2000.team_17.ui.App
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenViewModel

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
        setContent {
            val homeScreenViewModel: HomeScreenViewModel = viewModel(
                factory = viewModelFactory {
                    HomeScreenViewModel(
                        App.appModule.repository,
                        App.appModule.settingsRepository,
                        App.appModule.thresholdsRepository
                    )
                }
            )
            val uiState by homeScreenViewModel.homeScreenUiState.collectAsState()

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(true) {
                delay(5000)
                snackbarHostState.showSnackbar(
                    "No internet access"
                )

            }

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
                            homeScreenViewModel = homeScreenViewModel
                        )
                    }
                }
            }
        }
    }
}

