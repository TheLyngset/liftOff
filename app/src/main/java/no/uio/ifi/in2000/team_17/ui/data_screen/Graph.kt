package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.model.AvailableIndexes
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear

data class DataPoint(
    val y: Double = 0.0,
    val x: Int = 0
)

data class TimePoint(
    val y: String = "00:00",
    val x: Int = 0
)

val data = listOf(
    Point(0f, 40f),
    Point(1f, 20f),
    Point(2f, 50f),
    Point(3f, 10f),
    Point(4f, 20f),
    Point(5f, 30f),
    Point(6f, 40f),
    Point(7f, 50f),
    Point(8f, 60f),
    Point(9f, 70f),
    Point(10f, 80f),
    //Point(3f, 10f),
    Point(11f, 90f),
    Point(12f, 80f),
    Point(13f, 70f),
    Point(14f, 60f),
    Point(6f, 10f),
    Point(7f, 60f),
    Point(8f, 20f),
    Point(9f, 30f),
    Point(10f, 60f),
    Point(11f, 80f),
    Point(12f, 50f),
    Point(13f, 30f),
    Point(14f, 10f),
    Point(15f, 90f)
)

val data1 = listOf(
    Point(0f, 10f),
    Point(1f, 20f),
    Point(2f, 30f),
    Point(3f, 40f),
    Point(4f, 50f),
    Point(5f, 20f),
    Point(6f, 66f),
    Point(7f, 30f),
    Point(8f, 10f),
    Point(9f, 79f),
    Point(10f, 40f),
    Point(11f, 20f),
    Point(12f, 10f),
    Point(13f, 20f),
    Point(14f, 30f),
    Point(15f, 40f),
)


@Composable
private fun SingleLineChartWithGridLines(pointsData: List<Point>, pointsData1: List<Point>) {
    val steps = 11
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .topPadding(105.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData[i].x.toInt().toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
        .build()

    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    val data1 = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData1,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    LineChart(
        modifier = Modifier

            .height(300.dp),
        lineChartData = data
    )

    LineChart(
        modifier = Modifier
            .height(300.dp),
        lineChartData = data1
    )
}

@Composable
fun ThresholdGraph(
    dataScreenUiState: DataScreenUiState,
    height: Int,
    selectedTimeIndex: Int,
    setTimeIndex: (Int) -> Unit
) {
    val weatherDataLists = dataScreenUiState.weatherDataLists
    val thresholds = dataScreenUiState.thresholds
    val size = weatherDataLists.time.size
    val lastUpdated: String = dataScreenUiState.weatherDataLists.updated

    val pointsGroundWind: List<Point> = weatherDataLists.groundWind.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.groundWind[index].speed,
                thresholds.groundWindSpeed
            )).toFloat()
        )
    }
    val pointsMaxAirWind: List<Point> = weatherDataLists.maxWind.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.maxWind[index].speed,
                thresholds.maxWindSpeed
            )).toFloat()
        )
    }
    val pointsMaxWindShear: List<Point> = weatherDataLists.maxWindShear.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.maxWindShear[index].speed,
                thresholds.maxWindShear
            )).toFloat()
        )
    }
    val pointsCloudFraction: List<Point> =
        weatherDataLists.cloudFraction.mapIndexed { index, value ->
            Point(
                x = index.toFloat(),
                y = (rescalePoint(
                    weatherDataLists.cloudFraction[index] / 100,
                    thresholds.cloudFraction / 100
                )).toFloat()
            )
        }
    val pointsMedianRain: List<Point> = weatherDataLists.rain.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.rain[index].max,
                thresholds.rain + 0.00001
            )).toFloat()
        )
    }
    val pointsFog: List<Point> = weatherDataLists.fog.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(weatherDataLists.fog[index], thresholds.fog + 0.00001)).toFloat()
        )
    }
    val pointsHumidity: List<Point> = weatherDataLists.humidity.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.humidity[index] / 100,
                thresholds.humidity / 100
            )).toFloat()
        )
    }
    val absMinDew = absMinDew(weatherDataLists)
    val pointsDewPoint: List<Point> = weatherDataLists.dewPoint.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                weatherDataLists.dewPoint[index] + absMinDew,
                thresholds.dewPoint + absMinDew
            )).toFloat()
        )
    }
    val thresholdLine = weatherDataLists.date.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = 1f,
        )
    }
    val upperLine = weatherDataLists.date.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = 2f,
        )
    }

    val pointsTime: List<Point> = weatherDataLists.time.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = ("${
                weatherDataLists.time[index].subSequence(
                    0,
                    1
                )
            }.${weatherDataLists.time[index].subSequence(3, 4)}").toFloat()
        )
    }
    val pointsDate: List<Point> = weatherDataLists.date.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = ("${
                weatherDataLists.date[index].subSequence(8, 9)
            }.${weatherDataLists.date[index].subSequence(5, 6)}").toFloat()
        )
    }

    val xAxisData = AxisData.Builder()
        .backgroundColor(color = Color.White)
        .axisStepSize(10.dp)
        .topPadding(5.dp)
        .bottomPadding(5.dp)
        .steps(size - 1)
        //.labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLabelDescription { "Time indexes" }
        .axisLabelAngle(90.toFloat())
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .build()
    val yAxisData = AxisData.Builder()
        .backgroundColor(color = Color.White)
        .steps(5)
        .labelAndAxisLinePadding(15.dp)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .labelData { i ->
            val yMax = 1f
            val yScale = yMax / 5
            ((i * yScale)).formatToSinglePrecision()
        }
        .build()


    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                createLine(
                    pointsGroundWind,
                    Color.Black,
                    false,
                    false
                ),
                createLine(
                    pointsMaxAirWind,
                    Color.Gray,
                    false,
                    false
                ),
                createLine(
                    pointsMaxWindShear,
                    Color.LightGray,
                    false,
                    false
                ),
                createLine(
                    pointsCloudFraction,
                    Color.Cyan,
                    false,
                    false
                ),
                createLine(
                    pointsMedianRain,
                    Color.Magenta,
                    false,
                    false
                ),
                createLine(
                    pointsFog,
                    Color.DarkGray,
                    false,
                    false
                ),
                createLine(
                    pointsHumidity,
                    Color.Blue,
                    false,
                    false

                ),
                createLine(
                    pointsDewPoint,
                    Color.Red,
                    false,
                    false
                ),
                /*    createLine(
                        thresholdLine,
                        Color.Green,
                        false,
                        true
                    ),

                 */
                createLine(
                    upperLine,
                    Color.Transparent,
                    true,
                    false
                ),
                Line(
                    dataPoints = thresholdLine,
                    LineStyle(
                        color = Color.Green,
                        lineType = LineType.SmoothCurve(isDotted = true)
                    ),
                    IntersectionPoint(radius = 0.1.dp, color = MaterialTheme.colorScheme.tertiary),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.inversePrimary),
                    ShadowUnderLine(
                        alpha = 0.4f,
                    ),
                    SelectionHighlightPopUp(popUpLabel =
                    { x, y ->
                        val index = x.toInt()
                        val date =
                            pointsDate[index].y
                        val time =
                            pointsTime[index].y
                        val dateAndTime = "Date: $date Time: $time"
                        "$dateAndTime"
                    })
                )
            )
        ),
        backgroundColor = Color.White,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        isZoomAllowed = true,
        paddingTop = 5.dp,
        bottomPadding = 5.dp,
        paddingRight = 2.dp,
        containerPaddingEnd = 2.dp,

        )

    LastUpdated(lastUpdated)

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp),
        //.fillMaxSize(1f),
        lineChartData = data

    )
    ChartHistory()
}

@Composable
fun LastUpdated(lastUpdated: String) {
    Row(horizontalArrangement = Arrangement.Center) {
        Text("Last updated at $lastUpdated UTC+2")
    }
}

@Composable
fun ChartHistory() {
    Column(
        Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = " - Max Wind (ground) ", style = TextStyle(color = Color.Black))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = " - Max Wind (altitude) ", style = TextStyle(color = Color.Gray))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = " - Shear Max Wind ", style = TextStyle(color = Color.LightGray))
            Spacer(modifier = Modifier.width(2.dp))
        }
        Row {
            Text(text = " - Cloud Coverage ", style = TextStyle(color = Color.Cyan))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = " - Rain ", style = TextStyle(color = Color.Magenta))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = " - Fog ", style = TextStyle(color = Color.DarkGray))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = " - Humidity ", style = TextStyle(color = Color.Blue))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = " - Dew Point ", style = TextStyle(color = Color.Red))
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = " --- Threshold Line ", style = TextStyle(color = Color.Green))
            Spacer(modifier = Modifier.width(2.dp))
        }
    }
}


@Composable
fun createLine(
    points: List<Point>,
    lineColor: Color,
    fillColor: Boolean,
    dotted: Boolean
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
            lineType = LineType.SmoothCurve(isDotted = dotted)
        ),
        IntersectionPoint(radius = 0.1.dp, color = MaterialTheme.colorScheme.tertiary),
        SelectionHighlightPoint(color = MaterialTheme.colorScheme.inversePrimary),
        ShadowUnderLine(
            alpha = 0.4f,
            brush = Brush.verticalGradient(
                colorStops = colorStops
            )
        ),
        SelectionHighlightPopUp(popUpLabel = { x, y ->
            ""
        })
    )
}

fun absMinDew(weatherDataLists: WeatherDataLists): Double {
    val min = weatherDataLists.dewPoint.minOrNull()
    if (min is Double)
        return kotlin.math.abs(min)
    return 0.0
}

fun rescalePoint(realValue: Double?, threshold: Double?): Double {
    if (threshold is Double && realValue is Double) {
        var rescaled = ((realValue) / threshold)
        if (rescaled > 2.0)
            rescaled = 2.0
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
val dummyThreasholds: Thresholds = Thresholds.getDefaultInstance()

/*
@Composable
@Preview
fun pre() {
    SingleLineChartWithGridLines(data, data1)
}

 */

@Composable
@Preview(showBackground = true)
fun PreD() {
    //ThresholdGraph(dummyData, dummyThreasholds)
}
