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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherParameter.*
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.Rocket
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase.Companion.calculateColor

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    windowSizeClass: WindowSizeClass
) {
    val uiState by homeScreenViewModel.uiState.collectAsState()
    SelectedDateAndTime(modifier, uiState)

    val offsetMap = mapOf(
        WindowHeightSizeClass.Medium to 100.dp, //portriat pad
        WindowHeightSizeClass.Expanded to -300.dp, //landscape pad
        WindowHeightSizeClass.Compact to -100.dp //phone portriat - does not render on phone landscape
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .offset(y = (offsetMap[windowSizeClass.heightSizeClass] ?: 0.dp)),
        contentAlignment = Alignment.TopCenter
    ) {
    } // Responsive with screensize
    if(windowSizeClass.heightSizeClass == WindowHeightSizeClass.Medium) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().offset(y= (100.dp)),
            contentAlignment = Alignment.TopCenter){
            Rocket(Modifier)
        }
    }
    //landscape pad
    else if(windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().offset(y= (-300.dp)),
            contentAlignment = Alignment.BottomCenter){
            Rocket(Modifier)
        }
    }
    //phone portriat - does not render on phone landscape
    else if(windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
        Box(modifier = Modifier.fillMaxWidth().offset(y= (-100.dp)),
            contentAlignment = Alignment.TopCenter){
            Rocket(Modifier)
        }
    }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        BottomCard(uiState, windowSizeClass)
    }
}

@Composable
fun SelectedDateAndTime(modifier: Modifier = Modifier, uiState: HomeScreenUiState) {
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {
            var date = uiState.weatherPointInTime.date
            if (date.length < 10) date = stringResource(R.string.empty_Date)
            Text(
                text = uiState.weatherPointInTime.time,
                style = TextStyle(fontSize = 35.sp),
                color = MaterialTheme.colorScheme.inverseSurface
            )
            Text(
                text = "${date.slice(8..9)}.${date.slice(5..6)}.${date.slice(0..3)}",
                style = TextStyle(fontSize = 19.sp),
                color = MaterialTheme.colorScheme.inverseSurface
            )
        }
    }
}

@Composable
fun BottomCard(
    uiState: HomeScreenUiState,
    windowSizeClass: WindowSizeClass
) { //weatherInfoList: List<Triple<String, Double, String>>

    Box(
        modifier = Modifier
            .padding(16.dp, 5.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Text(
            text = (uiState.weatherPointInTime.temperature.toString() + stringResource(R.string.celsiusDegree)),
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
                generateWeatherInfoList(uiState),
                uiState.weatherPointInTime.available,
                uiState.thresholds
            )
        }
    }
}
@Composable
fun generateWeatherInfoList(uiState: HomeScreenUiState ): List<WeatherInfo> {
    return uiState.weatherPointInTime.iterator().map {(type, value) ->
        when(type){
            WeatherParameter.DATE -> null
            WeatherParameter.TIME -> null
            else -> {
                val num = when (type) {
                    WeatherParameter.MAXWIND -> (value as WindLayer).speed
                    WeatherParameter.GROUNDWIND -> (value as WindLayer).speed
                    WeatherParameter.MAXWINDSHEAR -> (value as WindShear).speed
                    WeatherParameter.RAIN -> (value as Rain).probability
                    else -> value as Double
                }
                val direction = when (type) {
                    WeatherParameter.MAXWIND -> (value as WindLayer).direction.toFloat()
                    WeatherParameter.GROUNDWIND -> (value as WindLayer).direction.toFloat()
                    else -> null
                }
                WeatherInfo(
                    type = type,
                    title = stringResource(type.titleId),
                    value = num,
                    unit = stringResource(id = type.unitId!!),
                    image = painterResource(id = type.imageID!!),
                    direction = direction
                )
            }
        }
    }
        .filterNotNull()
        .sortedBy { calculateColor(it.type, it.value.toString(), uiState.thresholds).ordinal }
}
@Composable
fun LaunchClearanceCard(trafficLightColor: TrafficLightColor, windowSizeClass: WindowSizeClass) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        LaunchClearanceCardCompactWidth(trafficLightColor)
    } else {
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
fun CardItem(weatherInfo: WeatherInfo, thresholds: Thresholds) {
    val color = calculateColor(
        weatherInfo.type,
        weatherInfo.value.toString(),
        thresholds
    ).color
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
            contentAlignment = Alignment.BottomCenter
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(bottomStart = 9.dp, bottomEnd = 9.dp),
                        color = color
                    )
                    .height(12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = weatherInfo.title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                )
                Image(
                    painter = weatherInfo.image,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Row {
                    Text(
                        text = "${weatherInfo.value} ${weatherInfo.unit}",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    if(weatherInfo.type in listOf(GROUNDWIND, MAXWIND)) Spacer(modifier = Modifier.width(40.dp))
                    
                }

            }
            if(weatherInfo.type in listOf(GROUNDWIND, MAXWIND)&& weatherInfo.direction != null){
                Column(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 12.dp), horizontalAlignment = Alignment.End) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        modifier = Modifier.rotate(90f + weatherInfo.direction),
                        contentDescription = null
                    )
                    val text = when(weatherInfo.direction){
                        in 0f..90f -> "NE"
                        in 90f..180f -> "SE"
                        in 180f..270f -> "SW"
                        else -> "NW"
                    }
                    Text(text = text, fontWeight = FontWeight.SemiBold)
                }
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
fun WeatherCardRow(
    weatherInfoList: List<WeatherInfo>,
    available: Available,
    thresholds: Thresholds
) {
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
                    weatherInfo,
                    thresholds
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
    val image: Painter,
    val direction: Float? = null
)