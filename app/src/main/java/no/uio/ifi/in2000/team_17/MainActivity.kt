package no.uio.ifi.in2000.team_17

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import no.uio.ifi.in2000.team_17.ui.theme.locationforecastTest.LocationforecastViewModel
import no.uio.ifi.in2000.team_17.ui.theme.Team17Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team_17.ui.theme.LocationforecastTestScreen.LocationforecastScreenTest


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
                    //Greeting("Team 17, Samuel, Hedda, Lelia, Malin")
                    val LocationforecastViewModel: LocationforecastViewModel = viewModel()
                    LocationforecastScreenTest()

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