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
import androidx.compose.ui.draw.rotate
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherParameter.*
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.ui.AutoHeightText
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase.Companion.calculateColor
import kotlin.math.round


/**
 * Table is the main composable for the Table screen. It contains the [GradientRows]
 */
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
            boxHeight = boxHeight,
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
                .map {(type, value) ->
                    GradientRow(value, type)
                     },
            thresholds = uiState.thresholds,
            selectedIndex = index
        ) { setIndex(it) }
    }
}

@Immutable
data class GradientRow(
    val data: List<Any>,
    val type: WeatherParameter,
)

data class Image(
    val type: WeatherParameter,
    val id: Int
)

/**
 * IconBox is a box that contains an icon and a description of the icon. The icon can be clicked to show the description.
 * @param modifier the modifier for the box
 * @param image the image that the box should contain
 */
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
    boxHeight: Double,
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
                                    DATE -> InfoBox(dateTimeModifier, "${text.subSequence(8, 10)}.${text.subSequence(5, 7)}")
                                    TIME -> InfoBox(dateTimeModifier, text)
                                    else -> {
                                        if(uiState.weatherDataLists.get(i-1).available.get(type)){
                                            when(type){
                                                MAXWIND, GROUNDWIND -> WindInfoBox(
                                                    rowModifier,
                                                    boxHeight = boxHeight,
                                                    data = value as WindLayer,
                                                    text,
                                                    listOf(Color.White.copy(0.0f), Color.White.copy(0.0f))
                                                )
                                                else -> InfoBox(rowModifier, text)
                                            }
                                        }else InfoBox(rowModifier)
                                    }
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

/**
 * ScrollLayer is basically a LazyRow that is overlaying all other rows. Making it cleanly scroll. This is also what supports TalkBack
 *
 * @param modifier the modifier for the column
 * @param overlayModifier the modifier for the overlaying row
 * @param uiState the current uiState
 * @param state the state of the lazy row
 * @param index the index of the selected row
 * @param setIndex the function that sets the index of the selected row
 */
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

/**
 * InfoBox are boxes that purely contain the information of the weather data and their respective colors.
 * @param modifier the modifier for the box
 * @param info the information that the box should contain, usually weather data
 * @param colors the colors that the box should have. Usually based on thresholds and weather data
 * @param bold if the text should be bold or not
 */
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

/**
 * TitleAndIconColumn is a column that contains all the titles and icons of the weather data.
 * @param modifier the modifier for the column
 * @param rowModifier the modifier for the rows
 * @param dateTimeModifier the modifier for the date and time
 * @param dividerModifier the modifier for the dividers
 * @param rows the list of rows that contains the weather data
 */
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
                        "    ${stringResource(row.type.titleId)}",
                        listOf(Color.Transparent, Color.Transparent)
                    )
                }
            }
            HorizontalDivider(dividerModifier)
        }
    }
}

/**
 * GradientRowsColumn is a column that contains all the weather data in rows. Calls the InfoBox and WindInfoBox for the different types of weather data.
 * @param modifier the modifier for the column
 * @param boxHeight the height of the boxes
 * @param rowModifier the modifier for the rows
 * @param dateTimeModifier the modifier for the date and time
 * @param dividerModifier the modifier for the dividers
 * @param state the state of the lazy list
 * @param rows the list of rows that contains the weather data
 * @param selectedIndex the index of the selected row
 * @param thresholds the thresholds for the weather data
 */
@Composable
fun GradientRowsColumn(
    modifier: Modifier,
    boxHeight: Double,
    rowModifier: Modifier,
    dateTimeModifier: Modifier,
    dividerModifier: Modifier,
    state: LazyListState,
    rows: List<GradientRow>,
    selectedIndex: Int,
    thresholds: Thresholds
) {
    val context = LocalContext.current
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
                        TIME, DATE -> {
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
                                    calculateColor(row.type, row.data.first().toString(), thresholds).color
                                ),
                            )
                        }
                    }
                }
                items((0..<size).toList()){i->
                    if (i < row.data.size) {
                        val data = row.data[i]
                        val text =  if(i != selectedIndex) data.toString() else context.getString(R.string.long_empty_string)
                        when (row.type) {
                            DATE -> {
                                val info = if (rows[1].data[i] == stringResource(R.string.empty_time)) {
                                    "${text.subSequence(8, 10)}.${text.subSequence(5, 7)}"
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
                                val colorNow = calculateColor(row.type, data.toString(), thresholds).color
                                val colorsNow = listOf(colorNow, colorNow)
                                val colorsAfter = if (i < row.data.size - 1) {
                                    val colorAfter =
                                        calculateColor(row.type, row.data[i + 1].toString(), thresholds).color
                                    listOf(colorAfter)
                                } else {
                                    listOf(Color.White.copy(0.0f))
                                }
                                when(row.type){
                                    GROUNDWIND, MAXWIND -> WindInfoBox(
                                        rowModifier,
                                        boxHeight,
                                        data as WindLayer,
                                        text,
                                        colorsNow + colorsAfter,
                                        bold = false
                                    )
                                    else -> InfoBox(rowModifier, text, colorsNow + colorsAfter, bold = false)
                                }
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

/**
 * WindInfoBox is a box that contains the wind data and the wind arrow. Calls the WindArrowText for the wind arrow.
 * @param modifier the modifier for the box
 * @param boxHeight the height of the box
 * @param data the wind data
 * @param text the text that the box should contain
 * @param colors the colors that the box should have. Usually based on thresholds and weather data
 * @param bold if the text should be bold or not
 */
@Composable
fun WindInfoBox(modifier: Modifier = Modifier, boxHeight: Double, data: WindLayer, text: String, colors:List<Color>, bold: Boolean = true){
    if(boxHeight >= 40) Box {
        InfoBox(modifier,"", colors, bold = bold)
        if(text != "") WindArrowText(
            modifier = modifier.offset(x = -(70/4).dp),
            value = data.speed,
            direction = data.direction.toFloat()
        )
    }
    else InfoBox(modifier, text, colors, bold = bold)
}

/**
 * GradientRows is a column that contains all the weather data in rows. Calls the TitleAndIconColumn and GradientRowsColumn for the different types of weather data.
 * This the main column that is called by the Table.
 * @param modifier the modifier for the column
 * @param boxHeight the height of the boxes
 * @param scrollToItem the item that should be scrolled to
 * @param boxWidth the width of the boxes
 * @param dividerModifier the modifier for the dividers
 * @param rowModifier the modifier for the rows
 * @param dateTimeModifier the modifier for the date and time
 * @param overlayModifier the modifier for the overlaying row
 * @param uiState the current uiState
 * @param rows the list of rows that contains the weather data
 * @param thresholds the thresholds for the weather data
 * @param selectedIndex the index of the selected row
 * @param setIndex the function that sets the index of the selected row
 */
@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun GradientRows(
    modifier: Modifier = Modifier,
    boxHeight: Double,
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
        boxHeight = boxHeight,
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
        boxWidth,
        boxHeight,
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
        if(!state.isScrollInProgress){
            currentDateIndex = mainState.firstVisibleItemIndex - 1
            state.scrollToItem(
                mainState.firstVisibleItemIndex,
                mainState.firstVisibleItemScrollOffset
            )
        }
    }
}

/**
 * WindArrowText is a box that contains the wind arrow and the wind data. The wind arrow is rotated based on the wind direction.
 * @param modifier the modifier for the box
 * @param value the wind data
 * @param direction the wind direction, used for rotation of the wind arrow

 */
@Composable
fun WindArrowText(modifier: Modifier = Modifier, value: Double, direction: Float) {
    Box(modifier,contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.wind_arrow),
            contentDescription = null,
            modifier = Modifier
                .rotate(90f + direction)
        )
        Text(
            text = round(value).toInt().toString()
        )
    }
}
@Preview(showBackground = true)
@Composable
fun WindArrowPreview() {
    WindArrowText(value = 11.34, direction = 45f)
}
