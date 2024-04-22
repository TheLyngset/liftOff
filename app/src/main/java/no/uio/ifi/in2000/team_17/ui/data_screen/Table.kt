package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase

@Composable
fun Table(
    uiState: DataScreenUiState,
    setIndex: (Int) -> Unit,
    boxWidth: Int,
    boxHeight: Int,
    dividerPadding: Int,
    dateTimeBoxHeight: Int,
){
    var selectedIndex by remember { mutableStateOf(uiState.selectedTimeIndex) }
    Box {
        GradientRows(
            boxWidth = boxWidth,
            dividerModifier = Modifier.padding(vertical = dividerPadding.dp),
            rowModifier = Modifier.size(width = boxWidth.dp, height = boxHeight.dp),
            dateTimeModifier = Modifier.size(width = boxWidth.dp, height = dateTimeBoxHeight.dp),
            overlayModifier = Modifier
                .size(
                    width = boxWidth.dp,
                    height = (dividerPadding * 18 + 14 + dateTimeBoxHeight * 2 + 8 * boxHeight).dp
                ) //(dividerPadding*8 + 8 + dateTimeBoxHeight * 2 - 4 + 8* boxHeight)
                .offset(x = -(boxWidth.times(0.18)).dp),
            rows = uiState.weatherDataLists.iterator()
                .map { GradientRow(it.second.map { it.toString() }, it.first) },
            thresholds = uiState.thresholds,
            selectedIndex = selectedIndex,
            setIndex = {
                setIndex(it)
                selectedIndex = it
            }
        )
    }
}
data class GradientRow(
    val data: List<String>,
    val type: WeatherParameter
)
@Composable
fun GradientRows(
    boxWidth: Int,
    dividerModifier: Modifier,
    rowModifier: Modifier,
    dateTimeModifier: Modifier,
    overlayModifier: Modifier,
    rows: List<GradientRow>,
    thresholds: Thresholds,
    selectedIndex: Int,
    setIndex:(Int) -> Unit
    ) {
    LazyColumn{
        items(rows.subList(0, 2)){row ->
            HorizontalDivider(dividerModifier)
            val color = Color.White.copy(0.0f)
            InfoBox1(dateTimeModifier,row.type.title,listOf(color, color))
        }
        items(rows.subList(2, rows.size)) { row ->
            HorizontalDivider(dividerModifier)
            val color = Color.White.copy(0.0f)
            InfoBox1(rowModifier,row.type.title,listOf(color, color))
            }
        item { HorizontalDivider(dividerModifier) }
    }

    val state = rememberLazyListState()

    LazyColumn(Modifier.offset(x = 70.dp)){
        itemsIndexed(rows){i, row ->
            HorizontalDivider(dividerModifier)
            LazyRow(state = state, verticalAlignment = Alignment.CenterVertically, userScrollEnabled = false) {
                item {
                    when (row.type) {
                        WeatherParameter.TIME -> {
                            InfoBox1(
                                dateTimeModifier,
                                "",
                                listOf(Color.Unspecified, Color.Unspecified)
                            )
                        }

                        WeatherParameter.DATE -> {
                            InfoBox1(
                                dateTimeModifier,
                                "",
                                listOf(Color.Unspecified, Color.Unspecified)
                            )
                        }

                        else -> {
                            InfoBox1(
                                rowModifier,
                                "",
                                listOf(
                                    Color.White.copy(0.0f),
                                    calculateColor(row.type, row.data.first(), thresholds)
                                )
                            )
                        }
                    }
                }
                itemsIndexed(rows[0].data) { i, _ ->
                    if(i < row.data.size){
                        val data = row.data[i]
                        when (row.type) {
                            WeatherParameter.DATE -> {
                                val info = if (rows[1].data[i] == "00:00") {
                                    "${data.subSequence(8, 10)}.${data.subSequence(5, 7)}"
                                } else {
                                    ""
                                }
                                InfoBox1(
                                    modifier = dateTimeModifier,
                                    info = info,
                                    colors = listOf(Color.Unspecified, Color.Unspecified)
                                )
                            }
                            WeatherParameter.TIME -> {
                                InfoBox1(dateTimeModifier, data, listOf(Color.Unspecified, Color.Unspecified))
                            }
                            else -> {
                                val colorNow = calculateColor(row.type, data, thresholds)
                                val colorsNow = listOf(colorNow, colorNow)
                                val colorsAfter = if (i < row.data.size - 1) {
                                    val colorAfter =
                                        calculateColor(row.type, row.data[i + 1], thresholds)
                                    listOf(colorAfter)
                                } else {
                                    listOf(Color.White.copy(0.0f))
                                }
                                InfoBox1(rowModifier, data, colorsNow + colorsAfter)
                            }
                        }
                    }
                    else{
                        InfoBox1(modifier = rowModifier, colors = listOf(Color.Unspecified, Color.Unspecified))
                    }
                }
            }
        }
        item { HorizontalDivider(dividerModifier) }
    }
    Column(
        Modifier
            .offset(x = 70.dp)
            .fillMaxSize()) {
        HorizontalDivider(dividerModifier, color = Color.Unspecified)
        SelectedBox1(overlayModifier, state, selectedIndex, rows[0].data, boxWidth)
    }
    val mainState = rememberLazyListState()
    LazyRow(state = mainState, modifier = Modifier
        .offset(x = 70.dp)){
        rows[1].data.forEachIndexed { i, _ ->
            if(i == 0){
                item{
                    InfoBox1(overlayModifier, colors = listOf(Color.White.copy(0.0f), Color.White.copy(0.0f)))
                }
            }
            else{
                item {
                    InfoBox1(
                        modifier = overlayModifier
                            .clickable { setIndex(i - 1) },
                        colors = listOf(Color.White.copy(0.0f), Color.White.copy(0.0f))
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit){
        mainState.scrollToItem(selectedIndex)
        mainState.scrollBy(-60f)
    }
    LaunchedEffect(mainState.firstVisibleItemScrollOffset) {
        state.scrollToItem(
            mainState.firstVisibleItemIndex,
            mainState.firstVisibleItemScrollOffset
        )
    }
}
@Composable
fun SelectedBox1(modifier: Modifier,state: LazyListState,index: Int, dates: List<String>, boxWidth: Int) {
    LazyRow(state = state, userScrollEnabled = false) {
        items(index + 1){
            Spacer(modifier = modifier)
        }
        item {
            Box(
                modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .background(Color.White.copy(0.3f))
                    .offset(x = (boxWidth.times(0.18)).dp),


                ){
                val date = dates.getOrElse(index){"            "}
                Text("${date.subSequence(8, 10)}.${date.subSequence(5, 7)}")
            }
        }
        items(maxOf((dates.size - index - 1), 0)){
            Spacer(modifier = modifier)
        }
    }
}

@Composable
fun InfoBox1(modifier: Modifier = Modifier, info: String? = null, colors: List<Color>) {
    Box(
        modifier = modifier
            .background(brush = Brush.horizontalGradient(colors)),
        contentAlignment = Alignment.CenterStart
    ) {
        if (info != null) {
            Text(text = info)
        }
    }
}

fun calculateColor(type: WeatherParameter, value: String, thresholds: Thresholds): Color {
    return when(type){
        WeatherParameter.CLOUDFRACTION-> WeatherUseCase.canLaunch(WeatherPointInTime(cloudFraction = value.toDouble()), thresholds)
        WeatherParameter.GROUNDWIND -> WeatherUseCase.canLaunch(WeatherPointInTime(groundWind = WindLayer(value.toDouble())), thresholds)
        WeatherParameter.MAXWINDSHEAR -> WeatherUseCase.canLaunch(WeatherPointInTime(maxWindShear = WindShear(value.toDouble())), thresholds)
        WeatherParameter.MAXWIND -> WeatherUseCase.canLaunch(WeatherPointInTime(maxWind = WindLayer(value.toDouble())), thresholds)
        WeatherParameter.RAIN -> WeatherUseCase.canLaunch(WeatherPointInTime(rain = Rain(median = value.toDouble())), thresholds)
        WeatherParameter.HUMIDITY -> WeatherUseCase.canLaunch(WeatherPointInTime(humidity = value.toDouble()), thresholds)
        WeatherParameter.DEWPOINT -> WeatherUseCase.canLaunch(WeatherPointInTime(dewPoint = value.toDouble()), thresholds)
        WeatherParameter.FOG -> WeatherUseCase.canLaunch(WeatherPointInTime(fog = value.toDouble()), thresholds)
        WeatherParameter.DATE -> TrafficLightColor.WHITE
        WeatherParameter.TIME -> TrafficLightColor.WHITE
    }.color
}

val testPoints = listOf(1.0, 3.0,14.0,24.0, 34.0)
val testTimes = listOf("23.00", "00.00", "01.00", "02.00", "03.00")
val testDates = listOf("21.04", "22.04", "22.04", "22.04", "22.04")
@Preview(showBackground = true)
@Composable
fun GradientRowPreview() {
    GradientRows(
        70,
        Modifier.padding(vertical = 8.dp),
        Modifier.size(height = 35.dp, width = 70.dp),
        Modifier.size(height = 35.dp, width = 70.dp),
        Modifier.size(width = 70.dp, height = 586.dp),
        rows = listOf(
            GradientRow(testDates.map { it.toString() }, WeatherParameter.DATE),
            GradientRow(testTimes.map { it.toString() }, WeatherParameter.TIME),
            GradientRow(testPoints.map { it.toString() }, WeatherParameter.GROUNDWIND),
            GradientRow(testPoints.map { it.toString() }, WeatherParameter.MAXWIND),
            GradientRow(testPoints.map { it.toString() }, WeatherParameter.MAXWINDSHEAR),
            GradientRow(testPoints.map { it.toString() }, WeatherParameter.CLOUDFRACTION)
        ),
        thresholds = ThresholdsSerializer.defaultValue,
        selectedIndex = 0,
        setIndex = {}
        )
}
