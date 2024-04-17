package no.uio.ifi.in2000.team_17.ui.data_screen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team17.AdvancedSettings
import no.uio.ifi.in2000.team_17.data.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DataScreen(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState,
    setTimeIndex: (Int) -> Unit
){
    val timeState = rememberLazyListState()
    val groundWindState = rememberLazyListState()

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollableState{delta ->
        scope.launch{
            timeState.scrollBy(-delta)
            groundWindState.scrollBy(-delta)
        }
        delta
    }

    Row(
        modifier
            .fillMaxSize()
    ) {
        Column(Modifier.scrollable(scrollState, Orientation.Horizontal, flingBehavior = ScrollableDefaults.flingBehavior()),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dividerWidth = 130.dp
            Text(text = "Time:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text(text = "Ground Wind:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text(text = "Max Wind:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text("Max Wind Shear:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text("Cloud fraction:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text("Median rain:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text("Humidity:")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text("Dew point")
            HorizontalDivider(Modifier.width(dividerWidth))
            Text("Fog fraction")
            HorizontalDivider(Modifier.width(dividerWidth))
        }
        LazyRow {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val width = 50 * dataScreenUiState.weatherDataLists.time.size
                    TimeRow(dataScreenUiState.weatherDataLists.time,dataScreenUiState.selectedTimeIndex) { setTimeIndex(it) }
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.groundWind.map { it.speed.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.GROUNDWIND
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.maxWind.map { it.speed.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.MAXWIND
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.maxWindShear.map { it.speed.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.MAXWINDSHEAR
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.cloudFraction.map { it.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.CLOUDFRACTION
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.rain.map { it.median.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.RAIN
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.humidity.map { it.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.HUMIDITY
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.dewPoint.map { it.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.DEWPOINT
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                    GradientBox(
                        dataScreenUiState.weatherDataLists.fog.map { it.toString() },
                        dataScreenUiState.advancedSettings,
                        WeatherParameter.FOG
                    )
                    HorizontalDivider(Modifier.width(width.dp))
                }
            }
        }
    }
}

@Composable
fun GradientBox(infoList : List<String>, advancedSettings: AdvancedSettings, weatherParameter: WeatherParameter){

    var colorList = listOf(Color.Unspecified, Color.Unspecified)
    if(weatherParameter != WeatherParameter.TIME ) {
        colorList = infoList
            .map {
                WeatherUseCase.canLaunch(
                    when (weatherParameter) {
                        WeatherParameter.GROUNDWIND -> WeatherPointInTime(
                            groundWind = WindLayer(
                                speed = it.toDouble()
                            )
                        )

                        WeatherParameter.MAXWINDSHEAR -> WeatherPointInTime(
                            maxWindShear = WindShear(
                                speed = it.toDouble()
                            )
                        )

                        WeatherParameter.CLOUDFRACTION -> WeatherPointInTime(cloudFraction = it.toDouble())
                        WeatherParameter.DEWPOINT -> WeatherPointInTime(dewPoint = it.toDouble())
                        WeatherParameter.FOG -> WeatherPointInTime(fog = it.toDouble())
                        WeatherParameter.HUMIDITY -> WeatherPointInTime(humidity = it.toDouble())
                        WeatherParameter.MAXWIND -> WeatherPointInTime(maxWind = WindLayer(speed = it.toDouble()))
                        WeatherParameter.RAIN -> WeatherPointInTime(rain = Rain(median = it.toDouble()))
                        else -> WeatherPointInTime()
                    }, advancedSettings
                ).color
            }
    }



    if(!colorList.isEmpty()&& !infoList.isEmpty()){
        Box {
            Row {
                Box(modifier = Modifier
                    .width(35.dp)
                    .background(colorList.first())
                ){
                    Text(text = "")
                }
                Box(
                    modifier = Modifier
                        .background(brush = Brush.horizontalGradient(colorList))
                ) {
                    Row {
                        infoList.subList(0, infoList.size - 1).forEach {
                            InfoBox(info = "")
                        }
                    }

                }
                Box(modifier = Modifier
                    .width(35.dp)
                    .background(colorList.last())
                ){
                    Text(text = "")
                }
            }
            Box(
                modifier = Modifier
            ) {
                Row(
                    Modifier
                ) {
                    infoList.forEach {
                        InfoBox(info = it)
                    }
                }
            }
        }
    }
}

@Composable
fun TimeRow(
    timeList: List<String>,
    currentTimeIndex: Int,
    setTimeIndex:(Int) ->Unit
){
    Row {
        timeList.forEachIndexed { index, time ->
            if (index == currentTimeIndex){
                InfoBox(Modifier.background(Color.LightGray),info = time)
            }else{
                InfoBox(Modifier.clickable { setTimeIndex(index) },info = time)
            }
        }
    }
}

@Composable
fun InfoBox(modifier: Modifier = Modifier ,info: String){
    Box(modifier = modifier
        .width(70.dp)
        .clip(shape = RoundedCornerShape(3.dp)),
        ){
        Text(text = info)
    }
}