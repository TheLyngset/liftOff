package no.uio.ifi.in2000.team_17.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.Available
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenUiState
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor

@Composable
fun newHomeScreen(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState
) {
    Box(modifier = modifier
        .fillMaxSize(1f)


    ){
        Image(painter = painterResource(id = R.drawable.sky),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(
                    scaleX = 2.4f,
                    scaleY = 1.6f,
                    translationX = 100f
                )

        )
        Image(painter = painterResource(id = R.drawable.rakett),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier.graphicsLayer (
                scaleX = 0.27f,
                scaleY = 0.47f,
                translationY = -250f
            )
            .alpha(0.85f)
        )


    }
    Box(modifier = Modifier
        .fillMaxSize(1f),
        contentAlignment = Alignment.BottomCenter
    ){
        BottomCard(homeScreenUiState)

    }
}
//skriften er satt, må endre til om den er true eller ikke
//jeg satte også noen verdier, i stedenfor å legge til uiState
@Composable
fun BottomCard(homeScreenUiState: HomeScreenUiState) { //weatherInfoList: List<Triple<String, Double, String>>
    Card(
        Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()

                .padding(top = 10.dp, bottom = 10.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchClearanceCard1(homeScreenUiState.canLaunch)
            /**
            //TODO: finne ikoner, alle tre verdier på rain?
            //WARNING: if you change a title you need to change it in [Available.get] as well */
            WeatherCardGrid(weatherInfoList = listOf(
                WeatherInfo("Ground wind", homeScreenUiState.weatherPointInTime.groundWind.speed, "m/s", painterResource(id = R.drawable.rainicon)),
                WeatherInfo("Max wind", homeScreenUiState.weatherPointInTime.maxWind.speed, "m/s", painterResource(id = R.drawable.windicon)),
                WeatherInfo("Max Shear", homeScreenUiState.weatherPointInTime.maxWindShear.speed, "m/s", painterResource(id = R.drawable.windicon)),
                WeatherInfo("Temperature", homeScreenUiState.weatherPointInTime.temperature, "℃", painterResource(id = R.drawable.rainicon)),
                WeatherInfo("Cloudiness", homeScreenUiState.weatherPointInTime.cloudFraction, "%", painterResource(id = R.drawable.windicon)),
                WeatherInfo("Rain", homeScreenUiState.weatherPointInTime.rain.median, "mm", painterResource(id = R.drawable.rainicon)),
                WeatherInfo("Humidity", homeScreenUiState.weatherPointInTime.humidity, "%", painterResource(id = R.drawable.rainicon)),
                WeatherInfo("Fog", homeScreenUiState.weatherPointInTime.fog, "%", painterResource(id = R.drawable.rainicon)),
            ), homeScreenUiState.weatherPointInTime.available)

        }
    }
}
//la trafikklyset inn her, må gjøre så det blir riktig, i tillegg til å velge grønn farge
@Composable
fun LaunchClearanceCard1(trafficLightColor: TrafficLightColor) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .background(trafficLightColor.color)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                        ) {
                Image(
                    painter = painterResource(id = trafficLightColor.image),
                    contentDescription = "GreenLightIcon",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(60.dp)
                )

                Spacer(modifier = Modifier.width(38.dp))
                Text(trafficLightColor.description, Modifier.padding(vertical = 18.dp), style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedButton(){
    val options = remember{ mutableStateListOf<String>("Home", "Data", "Juridisk") }
    var selectedIndex by remember { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index},
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {}
            )
            {
                Text(text = option)
            }
        }
    }
}

@Composable
fun CardItem(title: String, image: Painter, value: Double, unit: String) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .padding(top = 5.dp, bottom = 5.dp)
            .size(120.dp)

    ) {
        Column(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.3f))
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
fun WeatherCardGrid(weatherInfoList: List<WeatherInfo>, available : Available) {
    Card {
        LazyHorizontalGrid(
            GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            items(weatherInfoList) { weatherInfo ->
                if(available.get(weatherInfo.title)) {
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
}

data class WeatherInfo(
    val title: String,
    val value: Double,
    val unit: String,
    val image: Painter
)


@Preview
@Composable
fun Prehs(){
    newHomeScreen(homeScreenUiState = HomeScreenUiState())
}

