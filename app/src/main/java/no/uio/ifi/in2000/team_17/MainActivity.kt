package no.uio.ifi.in2000.team_17

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import no.uio.ifi.in2000.team_17.ui.BackGroundImage
import no.uio.ifi.in2000.team_17.ui.Background
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
                        val context = LocalContext.current
                        BackGroundImage()
                        Background()
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ElevatedCard {
                                Box(){
                                    Column(Modifier.padding(16.dp)) {
                                        Text(text = getString(R.string.no_Internet_Connection))
                                    }
                                    Box(
                                        modifier = Modifier.matchParentSize(),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        TextButton(onClick = { context.restart() }) {
                                            Text(text = getString(R.string.retry))
                                        }
                                    }
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

fun Context.restart() {
    val packageManager = packageManager
    val intent = packageManager.getLaunchIntentForPackage(packageName)!!
    val componentName = intent.component!!
    val restartIntent = Intent.makeRestartActivityTask(componentName)
    startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}

