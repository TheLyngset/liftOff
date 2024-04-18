package no.uio.ifi.in2000.team_17.ui.data_screen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team17.AdvancedSettings
import no.uio.ifi.in2000.team_17.data.WeatherUseCase
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.BackGroundImage

enum class Toggle{
    TABLE,
    GRAPH
}
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState,
    setTimeIndex: (Int) -> Unit
){
    var toggleState by remember { mutableStateOf(Toggle.TABLE) }

    BackGroundImage(0.82f)

    Column(modifier) {
        when (toggleState) {
            Toggle.TABLE -> {
                GradientTable(Modifier,dataScreenUiState,60) { setTimeIndex(it) }
            }

            Toggle.GRAPH -> {
                Text("Definitely a graph")
            }
        }
        ToggleButton {
            when (it) {
                0 -> toggleState = Toggle.TABLE
                1 -> toggleState = Toggle.GRAPH
            }
        }
    }
}

@Composable
fun GradientTable(modifier: Modifier = Modifier, dataScreenUiState:DataScreenUiState, rowHeight: Int, setTimeIndex: (Int) -> Unit ){
    Row(
        modifier.padding(top = 24.dp)
    ) {
        Column(Modifier.horizontalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dividerWidth = 100.dp
            TitleBox(text = "Time")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp),text = "Ground \nWind")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Max \nWind")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Max Wind \nShear")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Cloud \nfraction")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Rain")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Humidity")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Dew \nPoint")
            HorizontalDivider(Modifier.width(dividerWidth))
            TitleBox(Modifier.height(rowHeight.dp), text = "Fog \nfraction")
            HorizontalDivider(Modifier.width(dividerWidth))
        }

        Column(Modifier.horizontalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val width = 50 * dataScreenUiState.weatherDataLists.time.size
            TimeRow(dataScreenUiState.weatherDataLists.time,dataScreenUiState.selectedTimeIndex) { setTimeIndex(it) }
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.groundWind.map { it.speed.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.GROUNDWIND
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox( Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.maxWind.map { it.speed.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.MAXWIND
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.maxWindShear.map { it.speed.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.MAXWINDSHEAR
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.cloudFraction.map { it.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.CLOUDFRACTION
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.rain.map { it.median.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.RAIN
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.humidity.map { it.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.HUMIDITY
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.dewPoint.map { it.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.DEWPOINT
            )
            HorizontalDivider(Modifier.width(width.dp))
            GradientBox(Modifier.height(rowHeight.dp),
                dataScreenUiState.weatherDataLists.fog.map { it.toString() },
                dataScreenUiState.advancedSettings,
                WeatherParameter.FOG
            )
            HorizontalDivider(Modifier.width(width.dp))
        }
    }
}

@Composable
fun GradientBox(modifier: Modifier = Modifier, infoList : List<String>, advancedSettings: AdvancedSettings, weatherParameter: WeatherParameter){

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
                Box(modifier = modifier
                    .width(35.dp)
                    .background(colorList.first())
                ){
                    Text(text = "")
                }
                Box(
                    modifier = modifier
                        .background(brush = Brush.horizontalGradient(colorList))
                ) {
                    Row {
                        infoList.subList(0, infoList.size - 1).forEach {
                            InfoBox(info = "")
                        }
                    }

                }
                Box(modifier = modifier
                    .width(35.dp)
                    .background(colorList.last())
                ){
                    Text(text = "")
                }
            }
            Box(
            ) {
                Row(
                ) {
                    infoList.forEach {
                        InfoBox(modifier, info = it)
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
                InfoBox(
                    Modifier
                        .clip(shape = RoundedCornerShape(5.dp))
                        .background(Color.LightGray),info = time)
            }else{
                InfoBox(Modifier.clickable { setTimeIndex(index) },info = time)
            }
        }
    }
}

/**
 * Warning: [InfoBox] and [TitleBox] needs to have the same height
 */
@Composable
fun TitleBox(modifier: Modifier = Modifier, text : String){
    Column(modifier = modifier
        .padding(start = 15.dp),
         verticalArrangement = Arrangement.Center
    ){
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun InfoBox(modifier: Modifier = Modifier ,info: String){
    Box(modifier = modifier
        .width(70.dp),
        contentAlignment = Alignment.Center
        ){
        Text(text = info)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleButton(
    onFlip:(Int)-> Unit
){
    val options = remember{ mutableStateListOf("Table", "Graph") }
    var selectedIndex by remember { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                modifier = Modifier
                    .padding(bottom = 15.dp),
                selected = selectedIndex == index,
                onClick = { selectedIndex = index
                    onFlip(index)},
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {},
                colors = SegmentedButtonColors(
                    activeContainerColor = Color(0xFF9DDDF9),
                    activeBorderColor = Color.DarkGray,
                    activeContentColor = Color.Black,
                    inactiveBorderColor = Color.DarkGray,
                    inactiveContainerColor = Color.Unspecified,
                    inactiveContentColor = Color.Black,
                    disabledActiveBorderColor = Color.DarkGray,
                    disabledActiveContainerColor = Color.Unspecified,
                    disabledActiveContentColor = Color.Black,
                    disabledInactiveBorderColor = Color.DarkGray,
                    disabledInactiveContainerColor = Color.Unspecified,
                    disabledInactiveContentColor = Color.Black
                ),
            )
            {
                Text(text = option)
            }
        }
    }
}