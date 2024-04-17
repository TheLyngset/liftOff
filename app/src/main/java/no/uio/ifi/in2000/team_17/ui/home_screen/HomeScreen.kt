package no.uio.ifi.in2000.team_17.ui.home_screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings: () -> Unit,
    setMaxHeight: (String) -> Unit,
    setLat: (String) -> Unit,
    setLng: (String) -> Unit,
    onLoad: () -> Unit
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(homeScreenUiState.latLng, 11f)
    }

    Column(modifier.fillMaxSize()) {
        Card(
            Modifier
                .fillMaxWidth()
                .size(300.dp)
                .padding(16.dp)
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false),
                properties = MapProperties(mapType = MapType.SATELLITE)
            ) {
                Marker(
                    state = MarkerState(position = homeScreenUiState.latLng),
                    title = "Test",
                    snippet = "Marker not in Singapore"
                )
            }
        }
        Card {
            Text(text = "Can launch with current input: ${homeScreenUiState.canLaunch}")
        }
        DateTime(
            homeScreenUiState.weatherPointInTime.date,
            homeScreenUiState.weatherPointInTime.time
        )
        LastUpdated(homeScreenUiState.updated)
        WeatherCard(
            weatherInfoList = listOf(
                Triple("Ground wind", homeScreenUiState.weatherPointInTime.groundWind.speed, "m/s"),
                Triple(
                    "Max wind",
                    homeScreenUiState.weatherPointInTime.maxWind.speed,
                    "m/s"
                ),
                Triple(
                    "Max Shear",
                    homeScreenUiState.weatherPointInTime.maxWindShear.speed,
                    "m/s"
                )
            )
        )
        WeatherCard(
            weatherInfoList = listOf(
                Triple(
                    "Cloud coverage",
                    homeScreenUiState.weatherPointInTime.cloudFraction,
                    "%"
                ),
                Triple("Rain", homeScreenUiState.weatherPointInTime.rain.median, "mm"),
                Triple("Fog", homeScreenUiState.weatherPointInTime.fog, "%"),
                Triple("Humidity", homeScreenUiState.weatherPointInTime.humidity, "%"),
                Triple("Dewpoint", homeScreenUiState.weatherPointInTime.dewPoint, "˚C"),
            )
        )
        InputSheet(
            Modifier.fillMaxWidth(),
            homeScreenUiState = homeScreenUiState,
            toAdvancedSettings = toAdvancedSettings,
            setMaxHeight = { setMaxHeight(it) },
            setLat = { setLat(it) },
            setLng = { setLng(it) },
            onLoad = onLoad
        )
    }
}

@Composable
fun WeatherInfo(title: String, value: String, unit: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            title, style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
        Text(
            text = "$value $unit",
            style = TextStyle(
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun WeatherCard(weatherInfoList: List<Triple<String, String, String>>) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .padding(horizontal = 16.dp)
        ) {
            weatherInfoList.forEach {
                Spacer(modifier = Modifier.size(5.dp))
                WeatherInfo(title = it.first, value = it.second, unit = it.third)
                Divider()
                Spacer(modifier = Modifier.size(7.dp))
            }
        }
    }
}

@Composable
fun DateTime(date: String, time: String) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "$date kl. $time", style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
    }
}

@Composable
fun LastUpdated(time: String) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Updated at: $time", style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
    }
}

@Composable
fun LaunchClearanceCard(canLaunch: String) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                canLaunch, style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSheet(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings: () -> Unit,
    setMaxHeight: (String) -> Unit,
    setLat: (String) -> Unit,
    setLng: (String) -> Unit,
    onLoad: () -> Unit
) {
    var sheetState by remember { mutableStateOf(false) }
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { sheetState = true }) {
            Text(text = "Change input")
        }
    }
    if (sheetState) {
        ModalBottomSheet(onDismissRequest = { sheetState = false }) {
            InputSheetContent(
                homeScreenUiState = homeScreenUiState,
                toAdvancedSettings = toAdvancedSettings,
                setMaxHeight = { setMaxHeight(it) },
                setLat = { setLat(it) },
                setLng = { setLng(it) },
                onLoad = {
                    onLoad()
                    sheetState = false
                }
            )
        }
    }
}

@Composable
fun InputSheetContent(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings: () -> Unit,
    setMaxHeight: (String) -> Unit,
    setLat: (String) -> Unit,
    setLng: (String) -> Unit,
    onLoad: () -> Unit
) {
    var maxHeightText by remember { mutableStateOf("") }
    var latString by remember { mutableStateOf("") }
    var lngString by remember { mutableStateOf("") }
    Column(
        modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = maxHeightText,
                onValueChange = { maxHeightText = it },
                label = "Maximum height in km"
            ) { setMaxHeight(maxHeightText) }
            Text(homeScreenUiState.maxHeight.toString())
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = latString,
                onValueChange = { latString = it },
                label = "Latitude",
                modifier = Modifier.weight(1f)
            ) { setLat(latString) }
            Text(homeScreenUiState.latLng.latitude.toString())
            InputTextField(
                value = lngString,
                onValueChange = { lngString = it },
                label = "Longitude",
                modifier = Modifier.weight(1f)
            ) { setLng(lngString) }
            Text(homeScreenUiState.latLng.longitude.toString())
        }
        Button(onClick = onLoad) { Text(text = "Load new settings") }
        ListItem(
            colors = ListItemDefaults.colors(MaterialTheme.colorScheme.primaryContainer),
            headlineContent = {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Advanced settings"
                )
            },
            supportingContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = "The settings under are set with appropriate standard values, read more about why these values have been chosen here"
                    )
                }
            }
        )
        Button(onClick = { toAdvancedSettings() }) {
            Text(text = "Advanced settings")
        }
    }
}

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onDone: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDone(value)
            }
        ),
        modifier = modifier
    )

}
