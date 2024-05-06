package no.uio.ifi.in2000.team_17.ui.data_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.AvailableIndexes
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import kotlin.math.round

@SuppressLint("ResourceAsColor")
@Composable
fun ThresholdGraph(
    uiState: DataScreenUiState,
    windowSizeClass: WindowSizeClass,
    showInfoBox: Boolean,
    closeInfoBox: () -> Unit,
    backgroundSwitch: Boolean,
    onFlip: () -> Unit,
    setTimeIndex: (Int) -> Unit
) {
    val weatherDataLists = uiState.weatherDataLists
    val thresholds = uiState.thresholds
    val size = weatherDataLists.time.size
    val lastUpdated: String = uiState.weatherDataLists.updated

    val pointsGroundWind: List<Point> = List(weatherDataLists.groundWind.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.groundWind[index].speed,
                thresholds.groundWindSpeed
            )).toFloat()
        )
    }
    val pointsMaxAirWind: List<Point> = List(weatherDataLists.maxWind.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.maxWind[index].speed,
                thresholds.maxWindSpeed
            )).toFloat()
        )
    }
    val pointsMaxWindShear: List<Point> = List(weatherDataLists.maxWindShear.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.maxWindShear[index].speed,
                thresholds.maxWindShear
            )).toFloat()
        )
    }
    val pointsCloudFraction: List<Point> =
        List(weatherDataLists.cloudFraction.size) { index ->
            Point(
                x = index.toFloat(),
                y = (rescalePoint(
                    weatherDataLists.cloudFraction[index] / 100,
                    thresholds.cloudFraction / 100
                )).toFloat()
            )
        }
    val pointsMedianRain: List<Point> = List(weatherDataLists.rain.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.rain[index].max,
                thresholds.rain
            )).toFloat()
        )
    }
    val pointsFog: List<Point> = List(weatherDataLists.fog.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(weatherDataLists.fog[index], thresholds.fog)).toFloat()
        )
    }
    val pointsHumidity: List<Point> = List(weatherDataLists.humidity.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.humidity[index] / 100,
                thresholds.humidity / 100
            )).toFloat()
        )
    }
    val absMinDew = absMinDew(weatherDataLists)
    val pointsDewPoint: List<Point> = List(weatherDataLists.dewPoint.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.dewPoint[index] + absMinDew,
                thresholds.dewPoint + absMinDew
            )).toFloat()
        )
    }
    val thresholdLine = List(weatherDataLists.date.size) { index ->
        Point(
            x = index.toFloat(),
            y = 1f,
        )
    }
    val upperLine = List(weatherDataLists.date.size) { index ->
        Point(
            x = index.toFloat(),
            y = 2f,
        )
    }

    val pointsTime: List<Point> = List(weatherDataLists.time.size) { index ->
        Point(
            x = index.toFloat(),
            y = ("${
                weatherDataLists.time[index].subSequence(
                    0,
                    2
                )
            }.${weatherDataLists.time[index].subSequence(3, 5)}").toFloat()
        )
    }
    val pointsDate: List<Point> = List(weatherDataLists.date.size) { index ->
        Point(
            x = index.toFloat(),
            y = ("${
                weatherDataLists.date[index].subSequence(8, 10)
            }.${weatherDataLists.date[index].subSequence(5, 7)}").toFloat()
        )
    }

    val xAxisData = AxisData.Builder()
        .backgroundColor(color = Color.Transparent)
        .axisStepSize(30.dp)
        .topPadding(2.dp)
        .bottomPadding(2.dp)
        .steps(size - 1)
        //.labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLabelDescription { "Time indexes" }
        .axisLabelAngle(90.toFloat())
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .build()
    val yAxisData = AxisData.Builder()
        .backgroundColor(color = Color.Transparent)
        .steps(2)
        .labelAndAxisLinePadding(20.dp)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .labelData { i ->
            val yMax = 2f
            val yScale = yMax / 2
            ((i * yScale)).formatToSinglePrecision()
        }
        .build()

    //Builds colors for background
    //@Author Hedda

    var colors: List<Color> =
        listOf(
            Color.White,
            Color.White
        )
    if (backgroundSwitch) {
        val nGreen = round(uiState.thresholds.margin * 9).toInt()
        colors =
            (1..9).map { TrafficLightColor.RED.color.copy(1f) } +
                    (1..(9 - nGreen)).map { TrafficLightColor.YELLOW.color.copy(1f) } +
                    (1..nGreen).map { TrafficLightColor.GREEN.color.copy(1f) }
    }
    //builds the list of lines displayed on the chart
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = upperLine,
                    LineStyle(
                        color = Color.Transparent,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(radius = 0.1.dp, color = MaterialTheme.colorScheme.tertiary),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.inversePrimary),
                    ShadowUnderLine(
                        alpha = 0.75f,
                        brush = Brush.verticalGradient(
                            colors = colors
                        )
                    ),
                    SelectionHighlightPopUp(
                        popUpLabel =
                        { x, _ ->
                            val index = x.toInt()
                            setTimeIndex(index)
                            val date =
                                pointsDate[index].y
                            val time =
                                pointsTime[index].y
                            val dateAndTime = "Date: $date \nTime: ${time}0"
                            "$dateAndTime"
                        },
                        paddingBetweenPopUpAndPoint = 1.dp,
                        labelAlignment = android.graphics.Paint.Align.LEFT,
                        labelColor = Color.Black,
                        backgroundColor = Color.Transparent
                        //labelAlignment = android.graphics.Paint.Align.LEFT,
                    )
                ),
                Line(
                    dataPoints = thresholdLine,
                    LineStyle(
                        color = Color.Red,
                        lineType = LineType.SmoothCurve(isDotted = true)
                    ),
                    IntersectionPoint(radius = 0.1.dp, color = MaterialTheme.colorScheme.tertiary),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.inversePrimary),
                    ShadowUnderLine(
                        color = Color.Green,
                        alpha = 0.005f,
                    ),
                    SelectionHighlightPopUp(
                        popUpLabel = { _, _ ->
                            "Treshold Line"
                        },
                        paddingBetweenPopUpAndPoint = 2.dp,
                        labelAlignment = android.graphics.Paint.Align.LEFT,
                        backgroundColor = Color.Transparent
                        //labelAlignment = android.graphics.Paint.Align.LEFT,
                    )
                ),
                createLine(
                    pointsGroundWind,
                    Color.Black,
                    false,
                    stringResource(R.string.ground_wind)
                ),
                createLine(
                    pointsMaxAirWind,
                    Color(0XFFFFE500),
                    false,
                    stringResource(R.string.max_air_wind)
                ),
                createLine(
                    pointsMaxWindShear,
                    Color(0XFFFF7A00),
                    false,
                    stringResource(R.string.shear_wind)
                ),
                createLine(
                    pointsCloudFraction,
                    Color.Cyan,
                    false,
                    stringResource(R.string.could_coverage)
                ),
                createLine(
                    pointsMedianRain,
                    Color.Magenta,
                    false,
                    stringResource(R.string.rain)
                ),
                createLine(
                    pointsFog,
                    Color(0XFF9E00FF),
                    false,
                    stringResource(R.string.fog)
                ),
                createLine(
                    pointsHumidity,
                    Color.Blue,
                    false,
                    stringResource(R.string.humidity)

                ),
                createLine(
                    pointsDewPoint,
                    Color.Green,
                    false,
                    stringResource(R.string.dew_point)
                ),
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.background.copy(1f),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        isZoomAllowed = true,
        paddingTop = 11.dp,//just enough space to display the date and time when clicking on the graph
        bottomPadding = 5.dp,
        paddingRight = 2.dp,
        containerPaddingEnd = 2.dp,
    )

    BoxWithConstraints(
        Modifier.padding(bottom = 10.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        val horizontal = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
        val graphHeight = if (!horizontal) (maxHeight.value - 60) else (maxHeight.value - 30)
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background.copy(0f),
                                MaterialTheme.colorScheme.background.copy(1f)
                            )
                        )
                    )
            ) {
                Row(
                    Modifier
                        .height(25.dp)
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    var text = stringResource(R.string.NoGraphBackground)
                    if (backgroundSwitch) text = stringResource(R.string.GraphBackground)
                    Text(text, Modifier.scale(0.75f, 0.75f))
                    Spacer(modifier = Modifier.width(5.dp))
                    BackgroundSwitch(backgroundSwitch, onFlip)
                }
            }
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(graphHeight.dp)
                    .background(Color.Transparent),
                lineChartData = data,
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background.copy(1f),
                                MaterialTheme.colorScheme.background.copy(0f),
                            )
                        )
                    )
            )
        }
        val bottomPadding = if (!horizontal) 55.dp else 45.dp
        val alignment = if (horizontal) Alignment.CenterEnd else Alignment.BottomCenter
        if (showInfoBox) {
            Box(Modifier.fillMaxSize(), contentAlignment = alignment) {
                InfoBox(
                    lastUpdated = lastUpdated,
                    bottomPadding = bottomPadding,
                    horizontal = horizontal
                ) {
                    closeInfoBox()
                }
            }
        }
    }
}

@Composable
fun BackgroundSwitch(checked: Boolean, onFlip: () -> Unit) {
    Switch(
        checked = checked,
        colors = SwitchDefaults.colors().copy(
            checkedTrackColor = MaterialTheme.colorScheme.primary,
        ),
        onCheckedChange = {
            onFlip()
        },
        modifier = Modifier.scale(0.5f)
    )
}

@Composable
fun GraphInfoDialog(
    onDismiss: () -> Unit,
    onDontShowAgain: () -> Unit,
    painter: Painter,
    text: String
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            securePolicy = SecureFlagPolicy.SecureOn,
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(
                containerColor = Color.White.copy(0.8f),
                contentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified,
                disabledContentColor = Color.Unspecified
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(15.dp),
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painter,
                    contentDescription = text,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(70.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.dismiss))
                    }
                    TextButton(
                        onClick = { onDontShowAgain() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.dont_show_again))
                    }
                }
            }
        }
    }
}

@Composable
fun LastUpdated(lastUpdated: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            stringResource(R.string.last_updated_at_utc_2, lastUpdated),
            textAlign = TextAlign.Right
        )
    }
}


@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    lastUpdated: String,
    bottomPadding: Dp,
    horizontal: Boolean,
    onDismiss: () -> Unit
) {
    ElevatedCard(
        modifier
            .padding(bottom = bottomPadding),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified
        )
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Column(Modifier.padding(16.dp)) {
                if (!horizontal) {
                    LastUpdated(lastUpdated)
                    Row {
                        InfoBoxContent()
                    }
                } else {
                    InfoBoxContent()
                }
            }
            IconButton(
                modifier = Modifier.offset(x = 10.dp, y = (-10).dp),
                onClick = { onDismiss() }) {
                Icon(Icons.Outlined.Close, "Close")
            }
        }
    }
}

@Composable
fun InfoBoxContent() {
    Column() {
        Row(Modifier.padding(top = 5.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.Black)
            })
            Text(
                text = stringResource(R.string.max_wind_ground),
                style = TextStyle(color = Color.Black)
            )
        }
        Row(Modifier.padding(vertical = 10.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color(0XFFFFE500))
            })
            Text(
                text = stringResource(R.string.max_wind_altitude),
                style = TextStyle(color = Color.Black)
            )
        }
        Row {
            Canvas(modifier = Modifier
                .size(15.dp)
                .padding(bottom = 2.dp), onDraw = {
                drawCircle(color = Color(0XFFFF7A00))
            })
            Text(
                text = stringResource(R.string.max_shear_wind),
                style = TextStyle(color = Color.Black)
            )
        }
    }

    Column() {
        Row(Modifier.padding(top = 5.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.Green)
            })
            Text(text = " Dew Point ", style = TextStyle(color = Color.Black))
        }
        Row(Modifier.padding(vertical = 10.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.Cyan)
            })
            Text(
                text = stringResource(R.string.cloud_coverage),
                style = TextStyle(color = Color.Black)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(2.dp)
                    .background(Color.Red)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(2.dp)
                    .background(Color.Red)
            )
            Text(
                text = stringResource(R.string.threshold_line),
                style = TextStyle(color = Color.Black)
            )
        }
    }

    Column() {
        Row(Modifier.padding(top = 5.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.Blue)
            })
            Text(text = " Humidity ", style = TextStyle(color = Color.Black))
        }
        Row(Modifier.padding(vertical = 10.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.Magenta)
            })
            Text(text = stringResource(R.string.rain), style = TextStyle(color = Color.Black))
        }
        Row {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.DarkGray)
            })
            Text(text = " Fog ", style = TextStyle(color = Color.DarkGray))
        }
    }

}

//generates all variable lines in the chart
@Composable
fun createLine(
    points: List<Point>,
    lineColor: Color,
    fillColor: Boolean,
    text: String
): Line {
    var colorStops: Array<Pair<Float, Color>> =
        arrayOf(0.0f to Color.White, 0.0f to Color.White)
    if (fillColor) {
        colorStops = arrayOf(
            0.0f to Color.Red,
            0.5f to Color.Yellow,
            1.0f to Color.Green
        )
    }

    return Line(
        dataPoints = points,
        LineStyle(
            color = lineColor,
            lineType = LineType.SmoothCurve(isDotted = false)
        ),
        IntersectionPoint(radius = 0.05.dp, color = MaterialTheme.colorScheme.tertiary),
        SelectionHighlightPoint(color = MaterialTheme.colorScheme.inversePrimary),
        ShadowUnderLine(
            alpha = 0.15f,
            brush = Brush.verticalGradient(
                colorStops = colorStops
            )
        ),
        SelectionHighlightPopUp(
            popUpLabel = { _, _ ->
                text
            },
            paddingBetweenPopUpAndPoint = 2.dp,
            labelAlignment = android.graphics.Paint.Align.LEFT,
            backgroundColor = Color.Transparent
        )
    )
}

// rescales the dew point so that if the list contains a negative value,
// the entire scale is moved up in order to avoid dealing with negative values
fun absMinDew(weatherDataLists: WeatherDataLists): Double {
    val min = weatherDataLists.dewPoint.minOrNull()
    if (min is Double)
        return kotlin.math.abs(min)
    return 0.0
}

//calculates each point on the line as percentage of its own threshold
//purpose: create a common threshold line for all points
// to show visually if the values are below or above their respective threshold.
fun rescalePoint(realValue: Double?, threshold: Double?): Double {
    if (threshold is Double && realValue is Double) {
        var rescaled = ((realValue) / (threshold + 0.000001))
        if (rescaled > 1.95)
            rescaled = 1.95 //so that extremes are still visible on the graph
        if (rescaled <= 0.05)
            rescaled = 0.05 //so that extremes are still visible on the graph
        return rescaled
    }
    return 0.0
}

val dummyData: WeatherDataLists = WeatherDataLists(
    date = listOf(
        "2024-04-18",
        "2024-04-19",
        "2024-04-20",
        "2024-04-21",
        "2024-04-22",
        "2024-04-23",
        "2024-04-24",
        "2024-04-25",
        "2024-04-26",
        "2024-04-27"
    ),
    time = listOf(
        "08:38",
        "09:00",
        "10:00",
        "11.00",
        "12.00",
        "13.00",
        "14.00",
        "15.00",
        "16.00",
        "17.00"
    ),
    groundWind = listOf(
        WindLayer(1.2, 10.0, 123.5),
        WindLayer(1.4, 10.0, 123.5),
        WindLayer(3.4, 10.0, 123.5),
        WindLayer(4.6, 10.0, 123.5),
        WindLayer(6.5, 10.0, 123.5),
        WindLayer(3.7, 10.0, 123.5),
        WindLayer(8.5, 10.0, 123.5),
        WindLayer(9.2, 10.0, 123.5),
        WindLayer(10.4, 10.0, 123.5),
        WindLayer(13.5, 10.0, 123.5)
    ),
    maxWindShear = listOf(WindShear()),
    maxWind = listOf(
        WindLayer(1.2, 10.0, 123.5),
        WindLayer(1.4, 12.0, 123.5),
        WindLayer(3.4, 18.0, 123.5),
        WindLayer(4.6, 100.0, 123.5),
        WindLayer(6.5, 120.0, 123.5),
        WindLayer(3.7, 130.0, 123.5),
        WindLayer(8.5, 140.0, 123.5),
        WindLayer(9.2, 160.0, 123.5),
        WindLayer(10.4, 189.0, 123.5),
        WindLayer(13.5, 270.0, 123.5)
    ),
    cloudFraction = listOf(0.0, 10.0, 20.0, 30.0, 15.0, 40.0, 13.0, 34.0, 11.0),
    rain = listOf(
        Rain(0.0, 0.0, 0.1),
        Rain(0.0, 0.0, 0.0),
        Rain(0.0, 0.0, 0.3),
        Rain(0.0, 0.0, 0.1),
        Rain(0.0, 0.0, 0.2),
        Rain(0.0, 0.0, 0.0),
        Rain(0.0, 0.0, 0.4),
        Rain(0.0, 0.0, 0.0),
        Rain(0.0, 0.0, 0.9),
        Rain(0.0, 0.0, 0.11)
    ),
    humidity = listOf(51.0, 57.0, 59.9, 60.00, 62.00, 68.0, 70.0, 71.0, 53.0, 54.0),
    fog = listOf(0.1, 0.2, 0.4, 0.5, 1.4, 1.2, 1.6, 1.7, 0.9, 0.85),
    temperature = listOf(-3.0, -2.0, 0.0, 12.0, 3.0, 19.0, 30.0, 14.0, 5.0, -15.0),
    updated = "08:38",
    availableIndexes = AvailableIndexes()
)