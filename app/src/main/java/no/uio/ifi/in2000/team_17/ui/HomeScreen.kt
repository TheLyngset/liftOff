package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(uiState:UIState){
    Column(Modifier.fillMaxSize()){
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

