package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
fun HomeScreen(modifier: Modifier = Modifier, uiState:UIState){
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uiState.latLng, 11f)
    }
    Column(modifier.fillMaxSize()){
        Card(
            Modifier
                .fillMaxWidth()
                .size(350.dp)
                .padding(16.dp)
        ){
            GoogleMap(
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false),
                properties = MapProperties(mapType = MapType.SATELLITE)
            ) {
                Marker(
                    state = MarkerState(position = uiState.latLng),
                    title = "Test",
                    snippet = "Marker not in Singapore"
                )
            }
        }
        WeatherCard(
            weatherInfoList = listOf(
                Triple("Ground wind", uiState.weatherPointList.first().windSpeed, "m/s" ),
                Triple("Max wind", uiState.weatherPointList.maxOf { it.windSpeed }, "m/s" ),
                Triple("Max Shear", uiState.weatherPointList.maxOf { it.windShear }, "m/s" )
            )
        )
        WeatherCard(
            weatherInfoList = listOf(
                Triple("Cloud coverage", uiState.weatherPointList.first().cloudFraction, "%" ),
                Triple("Rain", uiState.weatherPointList.first().rain, "mm" ),
                Triple("Humidity", uiState.weatherPointList.first().humidity, "%" ),
                Triple("Dewpoint", uiState.weatherPointList.first().dewPoint, "˚C" )
            )
        )
    }
}
@Composable
fun WeatherInfo(title:String, value: Double, unit: String){
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
        Text(
            title, style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,)
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
fun WeatherCard(weatherInfoList:List<Triple<String, Double, String>>){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
            weatherInfoList.forEach {
                WeatherInfo(title = it.first, value = it.second, unit = it.third)
                Divider()
                Spacer(Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

