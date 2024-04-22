package no.uio.ifi.in2000.team_17.ui.data_screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.BackGroundImage
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase

enum class Toggle {
    TABLE,
    GRAPH
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState,
    setTimeIndex: (Int) -> Unit
) {
    var toggleState by remember { mutableStateOf(Toggle.TABLE) }
    var selectedTimeIndex by remember { mutableStateOf(dataScreenUiState.selectedTimeIndex) }
    if (dataScreenUiState.weatherDataLists.date.size > 1) {
        selectedTimeIndex = dataScreenUiState.selectedTimeIndex
    }
    val configuration = LocalConfiguration.current

    BackGroundImage(0.82f)

    Column(
        modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (toggleState) {
            Toggle.TABLE -> {
                Table(
                    uiState = dataScreenUiState,
                    setIndex ={ setTimeIndex(it) },
                    boxWidth = 70,
                    boxHeight = 60,
                    dividerPadding = 4,
                    dateTimeBoxHeight = 25
                )
                /*GradientTable(
                    Modifier,
                    dataScreenUiState,
                    60,
                    selectedTimeIndex) {
                    selectedTimeIndex = it
                    setTimeIndex(it)
                }*/
            }

                Toggle.GRAPH -> {
                    var heigth = 250
                    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                        heigth = 500
                    ThresholdGraph(dataScreenUiState, heigth, selectedTimeIndex) {
                        selectedTimeIndex = it
                        setTimeIndex(it)
                    }
                }
            }
        }

    Column(
        modifier
            .fillMaxSize()
            .offset(y = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ToggleButton {
            when (it) {
                0 -> toggleState = Toggle.TABLE
                1 -> toggleState = Toggle.GRAPH
            }
        }
    }
}

@Composable
fun GradientTable(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState,
    rowHeight: Int,
    selectedTimeIndex: Int,
    setTimeIndex: (Int) -> Unit
) {
    Column(
        modifier
            .padding(top = 8.dp)
    ) {
        Row {

            Column(
                Modifier.horizontalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val dividerWidth = 95.dp
                TitleBox(text = "")
                HorizontalDivider(Modifier.width(dividerWidth))
                TitleBox(text = "Time")
                HorizontalDivider(Modifier.width(dividerWidth))
                TitleBox(Modifier.height(rowHeight.dp), text = "Ground \nWind")
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
            LazyRow {
                item {
                    Box {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val width = 50 * dataScreenUiState.weatherDataLists.time.size
                            DateRow(
                                dataScreenUiState.weatherDataLists.date,
                                dataScreenUiState.weatherDataLists.time
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            TimeRow(
                                dataScreenUiState.weatherDataLists.time,
                            ) { setTimeIndex(it) }
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.groundWind.map { it.speed.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.GROUNDWIND
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.maxWind.map { it.speed.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.MAXWIND
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.maxWindShear.map { it.speed.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.MAXWINDSHEAR
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.cloudFraction.map { it.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.CLOUDFRACTION
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.rain.map { it.median.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.RAIN
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.humidity.map { it.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.HUMIDITY
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.dewPoint.map { it.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.DEWPOINT
                            )
                            HorizontalDivider(Modifier.width(width.dp))
                            GradientBox(
                                Modifier.height(rowHeight.dp),
                                dataScreenUiState.weatherDataLists.fog.map { it.toString() },
                                dataScreenUiState.thresholds,
                                WeatherParameter.FOG
                            )
                            HorizontalDivider(Modifier.width(width.dp))

                        }
                        SelectedBox(selectedTimeIndex, dataScreenUiState)
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedBox(index: Int, dataScreenUiState: DataScreenUiState) {
    val state = rememberLazyListState()
    Row {
        repeat(index) { InfoBox(info = "") }
        val date = dataScreenUiState.weatherDataLists.date.getOrElse(index) { "           " }
        Box {
            InfoBox(info = "${date.subSequence(8, 10)}.${date.subSequence(5, 7)}")
            InfoBox(
                Modifier
                    .offset(y = 32.dp)
                    .height(586.dp)
                    .background(Color.White.copy(0.3f))
                    .clip(shape = RoundedCornerShape(5.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                ""
            )
        }
    }
}

@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    infoList: List<String>,
    thresholds: Thresholds,
    weatherParameter: WeatherParameter
) {

var colorList = listOf(Color.Unspecified, Color.Unspecified)
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
                }, thresholds
            ).color
    }



    if (!colorList.isEmpty() && !infoList.isEmpty()) {
        Box {
            Row {
                //making half a box in the start
                Box(
                    modifier = modifier
                        .width(35.dp)
                        .background(
                            //color = colorList.first()
                            brush = Brush.horizontalGradient(
                                listOf(
                                    colorList
                                        .first()
                                        .copy(alpha = 0.0f), colorList.first(), colorList.first()
                                )
                            )
                        )
                ) {
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
                Box(
                    modifier = modifier
                        .width(35.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    colorList.last(),
                                    colorList.last(),
                                    colorList
                                        .last()
                                        .copy(0.0f)
                                )
                            )
                        )
                ) {
                    Text(text = "")
                }
            }
            Box {
                Row {
                    infoList.forEach {
                        InfoBox(modifier, info = it)
                    }
                }
            }

        }
    }
}

@Composable
fun DateRow(
    dateList: List<String>,
    timeList: List<String>
) {
    Row {
        dateList.forEachIndexed { index, date ->
            if (timeList.getOrElse(index) { "" } == "00:00" || index == 0) {
                InfoBox(
                    info = "${
                        dateList.getOrElse(index + 2) { date }.subSequence(8, 10)
                    }.${date.subSequence(5, 7)}"
                )
            } else {
                InfoBox(info = "")
            }
        }
    }
}

@Composable
fun TimeRow(
    timeList: List<String>,
    setTimeIndex: (Int) -> Unit
) {
    Row {
        timeList.forEachIndexed { index, time ->
            InfoBox(Modifier.clickable { setTimeIndex(index) }, info = time)
        }
    }
}


@Composable
fun TitleBox(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier = modifier
            .padding(start = 15.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

/**
 * Warning: [InfoBox] and [SelectedBox] needs to have the same width
 */
@Composable
fun InfoBox(modifier: Modifier = Modifier, info: String) {
    Box(
        modifier = modifier
            .width(70.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = info)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier.height(45.dp),
    onFlip: (Int) -> Unit
) {
    val options = remember { mutableStateListOf("Table", "Graph") }
    var selectedIndex by remember { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                modifier = Modifier
                    .padding(bottom = 12.dp),
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onFlip(index)
                },
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