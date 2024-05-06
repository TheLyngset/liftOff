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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

@Composable
fun Table(
    scrollToItem: Int? = null,
    uiState: DataScreenUiState,
    selectedIndex: Int,
    setIndex: (Int) -> Unit,
    boxWidth: Int,
    dividerPadding: Int,
) {
    BoxWithConstraints {
        val boxHeight = (maxHeight.value - (dividerPadding * 19 + 25 * 2)) / 8 * 0.9
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
            selectedIndex = selectedIndex,
            setIndex = { setIndex(it) }
        )
    }
}

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
    var showDescrption by remember { mutableStateOf( false ) }
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
                    .clickable { showDescrption = true },
                painter = painterResource(id = image.id),
                contentDescription = image.type.title
            )
            if(showDescrption){
                Card(
                    modifier.clickable { showDescrption = false },
                    colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ){
                    Box(Modifier.fillMaxSize(),contentAlignment = Alignment.CenterStart) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = image.type.title,
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
    modifier: Modifier,
    uiState: DataScreenUiState,
    state: LazyListState,
    index: Int,
    dates: List<String>,
    times: List<String>,
    boxWidth: Int
) {
    LazyRow(state = state, userScrollEnabled = false) {
        items(index + 1) {
            Spacer(modifier = modifier)
        }
        item {
            Box(
                modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .background(Color.White.copy(0.3f))
                    .offset(x = (boxWidth.times(0.19)).dp)
                    .semantics {
                        contentDescription = "${uiState.weatherDataLists.get(0).iterator()}"
                               },
                ) {
                val date = dates.getOrElse(index) { "            " }
                val time = times.getOrElse(index) { "00:00" }

                if (time != "00:00") {
                    Text(
                        "${date.subSequence(8, 10)}.${date.subSequence(5, 7)}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(0.57f),
                    )
                }
            }
        }
        items(maxOf((dates.size - index - 1), 0)) {
            Spacer(modifier = modifier)
        }
    }
}

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    info: String? = null,
    colors: List<Color>,
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
    LazyColumn(modifier) {

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
                        "    ${row.type.title}",
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
    thresholds: Thresholds
) {
    //Column of gradient rows
    LazyColumn(modifier.offset(x = 70.dp)) {
        items(rows) { row ->
            LazyRow(
                state = state,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                userScrollEnabled = false
            ) {
                item {
                    when (row.type) {
                        WeatherParameter.TIME -> {
                            InfoBox(
                                dateTimeModifier,
                                "",
                                listOf(Color.Unspecified, Color.Unspecified)
                            )
                        }

                        WeatherParameter.DATE -> {
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
                itemsIndexed(rows[0].data) { i, _ ->
                    if (i < row.data.size) {
                        val data = row.data[i]
                        when (row.type) {
                            WeatherParameter.DATE -> {
                                val info = if (rows[1].data[i] == "00:00") {
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

                            WeatherParameter.TIME -> {
                                InfoBox(
                                    dateTimeModifier,
                                    data,
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
                                InfoBox(rowModifier, data, colorsNow + colorsAfter, bold = false)
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

    GradientRowsColumn(
        modifier = modifier,
        rowModifier = rowModifier,
        dateTimeModifier = dateTimeModifier,
        dividerModifier = dividerModifier,
        state = state,
        rows = rows,
        thresholds = thresholds
    )

    //SelectedTimeIndex box
    Column(
        modifier
            .offset(x = 70.dp)
            .fillMaxSize()
    ) {
        SelectedBox(overlayModifier, uiState, state, selectedIndex, rows[0].data, rows[1].data, boxWidth)
    }
    //Making all rows scroll together by adding big boxes on top
    val mainState = rememberLazyListState()
    LazyRow(
        state = mainState, modifier = Modifier
            .offset(x = 70.dp)
    ) {
        rows[1].data.forEachIndexed { i, _ ->
            if (i == 0) {
                item {
                    InfoBox(
                        overlayModifier,
                        colors = listOf(Color.White.copy(0.0f), Color.White.copy(0.0f))
                    )
                }
            } else {
                item {
                    InfoBox(
                        modifier = overlayModifier
                            .clickable {
                                setIndex(i - 1)
                            },
                        colors = listOf(Color.White.copy(0.0f), Color.White.copy(0.0f))
                    )
                }
            }
        }
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
