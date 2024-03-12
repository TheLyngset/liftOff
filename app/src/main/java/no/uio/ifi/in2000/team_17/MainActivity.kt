package no.uio.ifi.in2000.team_17

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team_17.ui.theme.isobaricTestScreen.UiViewModel
import no.uio.ifi.in2000.team_17.ui.theme.Team17Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Team17Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiViewModel: UiViewModel = viewModel()
                    val uiState = uiViewModel.uiState.collectAsState()

                    Row (
                        Modifier
                            .fillMaxSize()
                    ){
                        Column(Modifier.background(Color.Gray)) {
                            Text(" Height ")
                            uiState.value.layerHeights.forEach {
                                Text(text = it.toString())
                            }
                        }
                        Column(Modifier.background(Color.LightGray)) {
                            Text(" Windspeed ")
                            uiState.value.windSpeeds.forEach {
                                Text(text = it.toString())
                            }
                        }
                        Column(Modifier.background(Color.Gray)) {
                            Text(" Wind direction ")
                            uiState.value.windFromDirections.forEach {
                                Text(text = it.toString())
                            }
                        }
                        Column (Modifier.background(Color.LightGray)){
                            Text(" temp ")
                            uiState.value.temperatures.forEach {
                                Text(text = it.toString())
                            }
                        }
                        Column (Modifier.background(Color.Gray)){
                            Text(" shear ")
                            uiState.value.windShear.forEach {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Team17Theme {
        Greeting("Android")
    }
}