package no.uio.ifi.in2000.team_17.ui.home_screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.Available
import no.uio.ifi.in2000.team_17.ui.Rocket

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState
) {
    Rocket()
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        BottomCard(homeScreenUiState)
    }
}

@Composable
fun BottomCard(homeScreenUiState: HomeScreenUiState) { //weatherInfoList: List<Triple<String, Double, String>>
    Card(
        Modifier
            .fillMaxWidth(1f),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = Color.Unspecified,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = Color.Unspecified
        ),
        //elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchClearanceCard1(homeScreenUiState.canLaunch)
            /**
            //TODO: alle tre verdier på rain?
            //WARNING: if you change a title you need to change it in [Available.get] as well */
            WeatherCardGrid(
                weatherInfoList = listOf(
                    WeatherInfo(
                        "Ground Wind",
                        homeScreenUiState.weatherPointInTime.groundWind.speed,
                        "m/s",
                        painterResource(id = R.drawable.groundwind2)
                    ),
                    WeatherInfo(
                        "Max Wind",
                        homeScreenUiState.weatherPointInTime.maxWind.speed,
                        "m/s",
                        painterResource(id = R.drawable.wind)
                    ),
                    WeatherInfo(
                        "Max Shear",
                        homeScreenUiState.weatherPointInTime.maxWindShear.speed,
                        "m/s",
                        painterResource(id = R.drawable.shearwind)
                    ),
                    WeatherInfo(
                        "Temperature",
                        homeScreenUiState.weatherPointInTime.temperature,
                        "℃",
                        painterResource(id = R.drawable.temperature)
                    ),
                    WeatherInfo(
                        "Cloudiness",
                        homeScreenUiState.weatherPointInTime.cloudFraction,
                        "%",
                        painterResource(id = R.drawable.cloud)
                    ),
                    WeatherInfo(
                        "Rain",
                        homeScreenUiState.weatherPointInTime.rain.median,
                        "mm",
                        painterResource(id = R.drawable.rain)
                    ),
                    WeatherInfo(
                        "Humidity",
                        homeScreenUiState.weatherPointInTime.humidity,
                        "%",
                        painterResource(id = R.drawable.humidity)
                    ),
                    WeatherInfo(
                        "Fog",
                        homeScreenUiState.weatherPointInTime.fog,
                        "%",
                        painterResource(id = R.drawable.fog)
                    ),
                    WeatherInfo(
                        "Dew Point",
                        homeScreenUiState.weatherPointInTime.dewPoint,
                        "%",
                        painterResource(id = R.drawable.temperature)
                    ),
                ), homeScreenUiState.weatherPointInTime.available
            )
        }
    }
}

@Composable
fun LaunchClearanceCard1(trafficLightColor: TrafficLightColor) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Box(
            Modifier
                .background(trafficLightColor.color)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
        ) {
            if (trafficLightColor != TrafficLightColor.WHITE) {
                Image(
                    painter = painterResource(id = trafficLightColor.image),
                    contentDescription = "GreenLightIcon",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(60.dp)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    trafficLightColor.description,
                    Modifier.padding(vertical = 18.dp),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }
        }
    }
}

@Composable
fun CardItem(title: String, image: Painter, value: Double, unit: String) {
    ElevatedCard(
        modifier = Modifier
            .padding(3.dp)
            .padding(top = 5.dp, bottom = 5.dp)
            .size(120.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified
        )

    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)

        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(35.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$value $unit",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


//cloud coverage, lazy row, liste med alle objektene, sorteres etter
@Composable
fun WeatherCardGrid(weatherInfoList: List<WeatherInfo>, available: Available) {
    LazyHorizontalGrid(
        GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(weatherInfoList) { weatherInfo ->
            if (available.get(weatherInfo.title)) {
                CardItem(
                    title = weatherInfo.title,
                    value = weatherInfo.value,
                    unit = weatherInfo.unit,
                    image = weatherInfo.image,
                )
            }
        }
    }
}

data class WeatherInfo(
    val title: String,
    val value: Double,
    val unit: String,
    val image: Painter
)


@Preview
@Composable
fun Prehs() {
    HomeScreen(homeScreenUiState = HomeScreenUiState())
}

