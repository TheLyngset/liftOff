package no.uio.ifi.in2000.team_17.ui.home_screen


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.Available
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.ui.Rocket
import no.uio.ifi.in2000.team_17.ui.calculateColor

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    windowSizeClass: WindowSizeClass
) {
    val uiState by homeScreenViewModel.uiState.collectAsState()
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {
            var date = uiState.weatherPointInTime.date
            if (date.length < 10) date = stringResource(R.string.empty_Date)
            Text(text = uiState.weatherPointInTime.time, style = TextStyle(fontSize = 35.sp), color = MaterialTheme.colorScheme.inverseSurface)
            Text(text = "${date.slice(8..9)}.${date.slice(5..6)}.${date.slice(0..3)}", style = TextStyle(fontSize = 19.sp), color = MaterialTheme.colorScheme.inverseSurface)
        }
    }

    //portriat pad
    if(windowSizeClass.heightSizeClass == WindowHeightSizeClass.Medium) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .offset(y = (100.dp)),
            contentAlignment = Alignment.TopCenter){
            Rocket(Modifier)
        }
    }
    //landscape pad
    else if(windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .offset(y = (-300.dp)),
            contentAlignment = Alignment.BottomCenter){
            Rocket(Modifier)
        }
    }
    //phone portriat - does not render on phone landscape
    else if(windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-100.dp)),
            contentAlignment = Alignment.TopCenter){
            Rocket(Modifier)
        }
    }
    //if ()
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        BottomCard(uiState, windowSizeClass)
    }
}

@Composable
fun BottomCard(uiState: HomeScreenUiState, windowSizeClass: WindowSizeClass) { //weatherInfoList: List<Triple<String, Double, String>>

    Box(modifier = Modifier
        .padding(16.dp, 5.dp)
        .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd) {
        Text(
            text = (uiState.weatherPointInTime.temperature.toString() + "°"),
            style = TextStyle(fontSize = 35.sp),
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
    Card(
        Modifier
            .fillMaxWidth(1f),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.inverseSurface,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = Color.Unspecified
        ),
        //elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 70.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchClearanceCard(uiState.trafficLightColor, windowSizeClass)
            WeatherCardRow(
                weatherInfoList = mutableListOf(
                    WeatherInfo(
                        WeatherParameter.GROUNDWIND,
                        stringResource(R.string.groundWind_title),
                        uiState.weatherPointInTime.groundWind.speed,
                        stringResource(R.string.m_s),
                        painterResource(id = R.drawable.groundwind2)
                    ),
                    WeatherInfo(
                        WeatherParameter.MAXWIND,
                        stringResource(R.string.maxWind_title),
                        uiState.weatherPointInTime.maxWind.speed,
                        stringResource(R.string.m_s),
                        painterResource(id = R.drawable.wind)
                    ),
                    WeatherInfo(
                        WeatherParameter.MAXWINDSHEAR,
                        stringResource(R.string.maxShear_title),
                        uiState.weatherPointInTime.maxWindShear.speed,
                        stringResource(R.string.m_s),
                        painterResource(id = R.drawable.shearwind)
                    ),
                    WeatherInfo(
                        WeatherParameter.CLOUDFRACTION,
                        stringResource(R.string.cloudiness_title),
                        uiState.weatherPointInTime.cloudFraction,
                        "%",
                        painterResource(id = R.drawable.cloud)
                    ),
                    WeatherInfo(
                        WeatherParameter.RAIN,
                        stringResource(R.string.rain_title),
                        uiState.weatherPointInTime.rain.probability,
                        "%",
                        painterResource(id = R.drawable.rain)
                    ),
                    WeatherInfo(
                        WeatherParameter.HUMIDITY,
                        stringResource(R.string.humidity_title),
                        uiState.weatherPointInTime.humidity,
                        "%",
                        painterResource(id = R.drawable.humidity)
                    ),
                    WeatherInfo(
                        WeatherParameter.FOG,
                        stringResource(R.string.fog_title),
                        uiState.weatherPointInTime.fog,
                        "%",
                        painterResource(id = R.drawable.fog)
                    ),
                    WeatherInfo(
                        WeatherParameter.DEWPOINT,
                        stringResource(R.string.dewPoint_title),
                        uiState.weatherPointInTime.dewPoint,
                        "℃",
                        painterResource(id = R.drawable.dewpoint),
                    ),
                ).sortedBy { calculateColor(it.type, it.value.toString(), uiState.thresholds).ordinal },
                uiState.weatherPointInTime.available,
                uiState.thresholds
            )
        }
    }
}

@Composable
fun LaunchClearanceCard(trafficLightColor: TrafficLightColor, windowSizeClass: WindowSizeClass) {
    if(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact){
        LaunchClearanceCardCompactWidth(trafficLightColor)
    }
    else{
        LaunchClearanceCardMediumOrExpanded(trafficLightColor)
    }
}
@Composable
fun LaunchClearanceCardCompactWidth(trafficLightColor: TrafficLightColor) {
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
            Row(
                Modifier
                    .fillMaxWidth(),
            ) {
                if (trafficLightColor != TrafficLightColor.WHITE) {
                    Image(
                        painter = painterResource(id = trafficLightColor.image),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(60.dp)
                    )
                }
                Box(Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
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
}
@Composable
fun LaunchClearanceCardMediumOrExpanded(trafficLightColor: TrafficLightColor) {
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
                    contentDescription = null,
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
fun CardItem(title: String, image: Painter, value: Double, unit: String, color:Color) {
    OutlinedCard(
        modifier = Modifier
            .size(width = 130.dp, height = 130.dp)
            .shadow(3.dp, CardDefaults.outlinedShape),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(1f),
            contentColor = MaterialTheme.colorScheme.inverseSurface,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified
        ),
        border = BorderStroke(2.5.dp, color)

    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(2.5.dp),
            contentAlignment = Alignment.BottomCenter) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(bottomStart = 9.dp, bottomEnd = 9.dp),
                    color = color
                )
                .height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                )
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Text(
                    text = "$value $unit",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(5.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


//cloud coverage, lazy row, liste med alle objektene, sorteres etter

//https://stackoverflow.com/questions/66341823/jetpack-compose-scrollbars
@Composable
fun Modifier.simpleHorizontalScrollbar(
    state: LazyListState,
    height: Dp = 8.dp,
    offsetY: Dp = -6.dp,
    cornerRadius: Dp = 4.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementWidth = this.size.width / state.layoutInfo.totalItemsCount.toFloat()
            val scrollbarOffsetX = firstVisibleElementIndex * elementWidth
            val scrollbarWidth = state.layoutInfo.visibleItemsInfo.size * elementWidth

            val bottomOffset = this.size.height - height.toPx() - offsetY.toPx()
            drawRoundRect(
                color = Color.LightGray,
                topLeft = Offset(scrollbarOffsetX, bottomOffset),
                size = Size(scrollbarWidth, height.toPx()),
                alpha = alpha,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
}
@Composable
fun WeatherCardRow(weatherInfoList: List<WeatherInfo>, available: Available, thresholds: Thresholds) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        modifier = Modifier
            .simpleHorizontalScrollbar(state = listState, height = 8.dp)
            .fillMaxWidth(),
    ) {
        items(weatherInfoList) { weatherInfo ->
            if (available.get(weatherInfo.type)) {
                Spacer(modifier = Modifier.size(8.dp))
                CardItem(
                    title = weatherInfo.title,
                    value = weatherInfo.value,
                    unit = weatherInfo.unit,
                    image = weatherInfo.image,
                    color = calculateColor(weatherInfo.type, weatherInfo.value.toString(), thresholds).color
                )
            }
        }
    }
}

data class WeatherInfo(
    val type: WeatherParameter,
    val title: String,
    val value: Double,
    val unit: String,
    val image: Painter
)

