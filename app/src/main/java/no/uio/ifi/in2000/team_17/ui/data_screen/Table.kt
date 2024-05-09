package no.uio.ifi.in2000.team_17.ui.data_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherParameter.*
import no.uio.ifi.in2000.team_17.ui.AutoHeightText
import no.uio.ifi.in2000.team_17.ui.calculateColor
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase

@Composable
fun Table(
    scrollToItem: Int? = null,
    uiState: DataScreenUiState,
    selectedIndex: Int?,
    setIndex: (Int) -> Unit,
    boxWidth: Int,
    dividerPadding: Int,
) {
    BoxWithConstraints {
        val boxHeight = (maxHeight.value - (dividerPadding * 19 + 25 * 2)) / 8 * 0.9
        val index = selectedIndex?: 0
        GradientRows(
            scrollToItem = scrollToItem,
            boxWidth = boxWidth,
            dividerModifier = Modifier.padding(vertical = dividerPadding.dp),
            rowModifier = Modifier.size(width = boxWidth.dp, height = boxHeight.dp),
            dateTimeModifier = Modifier.size(width = boxWidth.dp, height = 25.dp),
            overlayModifier = Modifier
                .size(
                    width = boxWidth.dp,
                    height = ((dividerPadding * 19 + 9) + (25 * 2) + 8 * boxHeight + 3).dp
                )
                .offset(x = -(boxWidth.times(0.25)).dp),
            uiState = uiState,
            rows = uiState.weatherDataLists.iterator()
                .map { GradientRow(it.second.map { it.toString() }, it.first) },
            thresholds = uiState.thresholds,
            selectedIndex = index
        ) { setIndex(it) }
    }
}

@Immutable
data class GradientRow(
    val data: List<String>,
    val type: WeatherParameter
)

data class Image(
    val type: WeatherParameter,
    val id: Int
)
@Composable
fun IconBox(modifier: Modifier, image: Image) {
    var showDescription by remember { mutableStateOf( false ) }
    Column(
        modifier, horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BoxWithConstraints {
            val width = if (maxWidth.value < 20) {
                maxWidth
            } else if (maxWidth.value < 100) {
                maxWidth.times(0.5f)
            } else {
                100.dp
            }
            val height = if (maxHeight.value < 20) {
                maxHeight
            } else if (maxHeight.value < 100) {
                maxHeight.times(0.5f)
            } else {
                100.dp
            }
            Image(
                modifier = Modifier
                    .size(width, height)
                    .clickable { showDescription = true },
                painter = painterResource(id = image.id),
                contentDescription = stringResource(image.type.titleId)
            )


            if(showDescription){
                Card(
                    modifier.clickable { showDescription = false },
                    colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.background.copy(1f))
                ){
                    Box(Modifier.fillMaxSize(),contentAlignment = Alignment.CenterStart) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = stringResource(image.type.titleId),
                            fontSize = 13.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedBox(
    modifier:Modifier,
    overlayModifier: Modifier,
    dateTimeModifier: Modifier,
    rowModifier: Modifier,
    dividerModifier: Modifier,
    uiState: DataScreenUiState,
    state: LazyListState,
    index: Int,
    boxWidth: Int,
    setIndex:(Int) -> Unit
) {
    Column(
        modifier
            .offset(x = 70.dp)
            .fillMaxSize()
    )  {
        LazyRow(state = state, userScrollEnabled = false) {
            items((0..uiState.weatherDataLists.date.size).toList()) { i ->
                val weatherPointInTime = uiState.weatherDataLists.get(i - 1)
                if (i - 1 != index) {

                    Spacer(modifier = overlayModifier
                        .clickable{
                            if (i != 0) setIndex(i - 1)
                        }
                    )
                } else {
                    Box(
                        overlayModifier
                            .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                            .background(Color.White.copy(0.3f))
                    ) {
                        LazyColumn(
                            Modifier.offset((boxWidth / 4).dp),
                            userScrollEnabled = false
                        ) {
                            items(weatherPointInTime.iterator()) { (type, value) ->
                                val text = value.toString()
                                when (type) {
                                    DATE -> InfoBox(
                                        dateTimeModifier,
                                        "${text.subSequence(8, 10)}.${text.subSequence(5, 7)}",
                                        bold = true
                                    )

                                    TIME -> InfoBox(dateTimeModifier, text, bold = true)
                                    else -> InfoBox(rowModifier, text, bold = true)
                                }
                                HorizontalDivider(dividerModifier, color = Color.Transparent)
                            }
                        }
                    }
                }
            }
        }
    }
}
// comment what this does
@Composable
fun ScrollLayer(
    modifier: Modifier,
    overlayModifier: Modifier,
    uiState: DataScreenUiState,
    state: LazyListState,
    index: Int,
    setIndex:(Int) -> Unit
) {
    Column(
        modifier
            .offset(x = 70.dp)
            .fillMaxSize()
    ) {
        LazyRow(state = state) {
            items((0..uiState.weatherDataLists.date.size).toList()) { i ->
                val weatherPointInTime = uiState.weatherDataLists.get(i - 1)
                if (i - 1 != index) {
                    val onClickLabel = stringResource(R.string.select) +
                            "${
                                weatherPointInTime
                                    .iterator().map { (type, value) ->
                                        if (weatherPointInTime.available.get(type)) {
                                            stringResource(type.titleId) + stringResource(R.string.empty_string) +
                                                    value.toString() + stringResource(R.string.empty_string)  +
                                                    when (calculateColor(
                                                        type,
                                                        value.toString(),
                                                        uiState.thresholds
                                                    )) {
                                                        TrafficLightColor.RED -> stringResource(R.string.over_threshold)
                                                        TrafficLightColor.YELLOW -> stringResource(R.string.close_to_threshold)
                                                        TrafficLightColor.GREEN -> stringResource(R.string.under_threshold)
                                                        TrafficLightColor.WHITE -> stringResource(R.string.empty_string)
                                                    }
                                        } else {
                                            when (type) {
                                                DATE -> value.toString()
                                                TIME -> {
                                                    value.toString() +
                                                            when (WeatherUseCase.canLaunch(
                                                                weatherPointInTime,
                                                                uiState.thresholds
                                                            )) {
                                                                TrafficLightColor.RED -> stringResource(R.string.over_threshold)
                                                                TrafficLightColor.YELLOW -> stringResource(R.string.close_to_threshold)
                                                                TrafficLightColor.GREEN -> stringResource(R.string.under_threshold)
                                                                TrafficLightColor.WHITE -> stringResource(R.string.empty_string)
                                                            }
                                                }

                                                else -> stringResource(type.titleId) + stringResource(R.string.no_data)
                                            }

                                        }
                                    }
                            }"

                    Spacer(modifier = overlayModifier
                        .clickable(
                            onClickLabel = onClickLabel
                        ) {
                            if (i != 0) setIndex(i - 1)
                        }
                    )
                } else {
                    val context = LocalContext.current
                    Spacer(overlayModifier.semantics {
                        contentDescription =
                            context.getString(R.string.selected_box_sematics)
                    })
                }
            }
        }
    }
}

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    info: String? = null,
    colors: List<Color> = listOf(Color.Transparent, Color.Transparent),
    bold: Boolean = true
) {
    Box(
        modifier = modifier
            .background(brush = Brush.horizontalGradient(colors)),
        contentAlignment = Alignment.CenterStart
    ) {
        if (info != null) {
            val newInfo = when (info.length) {
                3 -> "  $info"
                4 -> " $info"
                5 -> info
                else -> info
            }
            if (bold) {
                AutoHeightText(text = newInfo, style = TextStyle(fontWeight = FontWeight.SemiBold))
            } else {
                AutoHeightText(text = newInfo, style = TextStyle())
            }
        }
    }
}

@Composable
fun TitleAndIconColumn(
    modifier: Modifier,
    rowModifier: Modifier,
    dateTimeModifier: Modifier,
    dividerModifier: Modifier,
    rows: List<GradientRow>
) {
    //Titles and icons Column
    LazyColumn(modifier.clearAndSetSemantics { }) {

        items(rows) { row ->
            when (row.type) {
                GROUNDWIND -> IconBox(
                    modifier = rowModifier,
                    image = Image(GROUNDWIND,R.drawable.groundwind2)
                )

                MAXWINDSHEAR -> IconBox(
                    modifier = rowModifier,
                    image = Image(MAXWINDSHEAR, R.drawable.shearwind)
                )

                MAXWIND -> IconBox(
                    modifier = rowModifier,
                    image = Image(MAXWIND, R.drawable.wind)
                )
                CLOUDFRACTION -> IconBox(
                    modifier = rowModifier,
                    image = Image(CLOUDFRACTION,R.drawable.cloud)
                )

                RAIN -> IconBox(
                    modifier = rowModifier,
                    image = Image(RAIN,R.drawable.rain)
                )

                HUMIDITY -> IconBox(
                    modifier = rowModifier,
                    image = Image(HUMIDITY,R.drawable.humidity)
                )

                DEWPOINT -> IconBox(
                    modifier = rowModifier,
                    image = Image(DEWPOINT,R.drawable.dewpoint)
                )

                FOG -> IconBox(
                    modifier = rowModifier,
                    image = Image(FOG,R.drawable.fog)
                )
                else -> {
                    InfoBox(
                        dateTimeModifier,
                        "    ${row.type.titleId}",
                        listOf(Color.Transparent, Color.Transparent)
                    )
                }
            }
            HorizontalDivider(dividerModifier)
        }
    }
}

@Composable
fun GradientRowsColumn(
    modifier: Modifier,
    rowModifier: Modifier,
    dateTimeModifier: Modifier,
    dividerModifier: Modifier,
    state: LazyListState,
    rows: List<GradientRow>,
    selectedIndex: Int,
    thresholds: Thresholds
) {
    val size = rows[0].data.size
    //Column of gradient rows
    LazyColumn(modifier.offset(x = 70.dp)) {
        items(rows) { row ->
            LazyRow(
                state = state,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                userScrollEnabled = false,
            ) {
                item {
                    when (row.type) {
                        TIME -> {
                            InfoBox(
                                dateTimeModifier,
                                "",
                                listOf(Color.Unspecified, Color.Unspecified)
                            )
                        }

                        DATE -> {
                            InfoBox(
                                dateTimeModifier,
                                "",
                                listOf(Color.Unspecified, Color.Unspecified)
                            )
                        }

                        else -> {
                            InfoBox(
                                rowModifier,
                                "",
                                listOf(
                                    Color.White.copy(0.0f),
                                    calculateColor(row.type, row.data.first(), thresholds).color
                                ),
                            )
                        }
                    }
                }
                items((0..<size).toList()){i->
                    if (i < row.data.size) {
                        val data = row.data[i]
                        val text =  if(i != selectedIndex) data else ""
                        when (row.type) {
                            DATE -> {
                                val info = if (rows[1].data[i] == stringResource(R.string.empty_time)) {
                                    "${data.subSequence(8, 10)}.${data.subSequence(5, 7)}"
                                } else {
                                    ""
                                }
                                InfoBox(
                                    modifier = dateTimeModifier,
                                    info = info,
                                    colors = listOf(Color.Unspecified, Color.Unspecified)
                                )
                            }

                            TIME -> {
                                InfoBox(
                                    dateTimeModifier,
                                    text,
                                    listOf(Color.Unspecified, Color.Unspecified)
                                )
                            }

                            else -> {
                                val colorNow = calculateColor(row.type, data, thresholds).color
                                val colorsNow = listOf(colorNow, colorNow)
                                val colorsAfter = if (i < row.data.size - 1) {
                                    val colorAfter =
                                        calculateColor(row.type, row.data[i + 1], thresholds).color
                                    listOf(colorAfter)
                                } else {
                                    listOf(Color.White.copy(0.0f))
                                }
                                InfoBox(rowModifier, text, colorsNow + colorsAfter, bold = false)
                            }
                        }
                    } else {
                        InfoBox(
                            modifier = rowModifier,
                            colors = listOf(Color.Unspecified, Color.Unspecified)
                        )
                    }
                }
            }
            HorizontalDivider(dividerModifier)
        }
    }
}


@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun GradientRows(
    modifier: Modifier = Modifier,
    scrollToItem: Int? = null,
    boxWidth: Int,
    dividerModifier: Modifier,
    rowModifier: Modifier,
    dateTimeModifier: Modifier,
    overlayModifier: Modifier,
    uiState: DataScreenUiState,
    rows: List<GradientRow>,
    thresholds: Thresholds,
    selectedIndex: Int,
    setIndex: (Int) -> Unit
) {
    TitleAndIconColumn(
        modifier = modifier,
        rowModifier = rowModifier,
        dateTimeModifier = dateTimeModifier,
        dividerModifier = dividerModifier,
        rows = rows
    )
    val state = rememberLazyListState()
    var currentDateIndex by remember { mutableIntStateOf(0) }
    val currentDate = uiState.weatherDataLists.date.getOrElse(currentDateIndex){""}
    val info = if(currentDate.isNotEmpty()) "${currentDate.subSequence(8, 10)}.${currentDate.subSequence(5, 7)}" else ""

    InfoBox(modifier = dateTimeModifier.offset(x = 70.dp), info = info, bold = true)

    GradientRowsColumn(
        modifier = modifier,
        rowModifier = rowModifier,
        dateTimeModifier = dateTimeModifier,
        dividerModifier = dividerModifier,
        state = state,
        rows = rows,
        thresholds = thresholds,
        selectedIndex = selectedIndex
    )

    //SelectedTimeIndex box
    //Making all rows scroll together by adding big boxes on top
    val mainState = rememberLazyListState()

    SelectedBox(
        modifier,
        overlayModifier,
        dateTimeModifier,
        rowModifier,
        dividerModifier,
        uiState,
        state,
        selectedIndex,
        boxWidth
    ){
        setIndex(it)
    }
    ScrollLayer(
        modifier = modifier,
        overlayModifier = overlayModifier,
        uiState = uiState,
        state = mainState,
        index = selectedIndex
    ) {
        setIndex(it)
    }
    //scrolls to now or selected
    if (scrollToItem != null) {
        LaunchedEffect(scrollToItem) {
            mainState.animateScrollToItem(scrollToItem)
        }
    } else {
        LaunchedEffect(Unit) {
            mainState.scrollToItem(selectedIndex)
            mainState.scrollBy(-60f)
        }
    }
    //scrolls all rows when overlaying row is scrolled
    LaunchedEffect(mainState.firstVisibleItemScrollOffset) {
        currentDateIndex = mainState.firstVisibleItemIndex - 1
        state.scrollToItem(
            mainState.firstVisibleItemIndex,
            mainState.firstVisibleItemScrollOffset
        )
    }
}


/*
@Preview(showBackground = true)
@Composable
fun GradientRowPreview() {
    val testPoints = listOf(1.0, 3.0,14.0,24.0, 34.0)
    val testTimes = listOf("23.00", "00.00", "01.00", "02.00", "03.00")
    val testDates = listOf("2024-04-21", "2024-04-21", "2024-04-21", "2024-04-21", "2024-04-21")
    var selectedIndex by remember {
        mutableStateOf(0)
    }
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
        selectedIndex = selectedIndex,
        setIndex = {
            selectedIndex = it
        }
        )
}

*/
