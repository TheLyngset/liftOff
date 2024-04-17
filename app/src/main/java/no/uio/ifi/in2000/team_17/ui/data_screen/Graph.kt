package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import no.uio.ifi.in2000.team17.AdvancedSettings
import no.uio.ifi.in2000.team_17.model.WeatherDataLists


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

    )


@Composable
private fun SingleLineChartWithGridLines(pointsData: List<Point>) {
    val steps = 12
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
        }.build()
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
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}

@Composable
fun ThresholdGraph(weatherDataLists: WeatherDataLists, thresholds: AdvancedSettings) {

    val size = weatherDataLists.date.size

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
            y = (rescalePoint(weatherDataLists.rain[index].median, thresholds.rain + 1)).toFloat()
        )
    }
    val pointsFog: List<Point> = weatherDataLists.fog.mapIndexed { index, value ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(weatherDataLists.fog[index], thresholds.fog + 1)).toFloat()
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
            y = 1f
        )

    }

    val xAxisData = AxisData.Builder()
        .backgroundColor(color = Color.Transparent)
        .axisStepSize(40.dp)
        .topPadding(50.dp)
        .steps(size - 1)
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .backgroundColor(color = Color.Transparent)
        .steps(5)
        .labelAndAxisLinePadding(20.dp)
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
                    Color.Green,
                ),
                createLine(
                    pointsMaxAirWind,
                    Color.Gray,
                    Color.Green,
                ),
                createLine(
                    pointsMaxWindShear,
                    Color.Yellow,
                    Color.Green,
                ),
                createLine(
                    pointsCloudFraction,
                    Color.Cyan,
                    Color.Green,
                ),
                createLine(
                    pointsMedianRain,
                    Color.Magenta,
                    Color.Green,
                ),
                createLine(
                    pointsFog,
                    Color.DarkGray,
                    Color.Green,
                ),
                createLine(
                    pointsHumidity,
                    Color.Blue,
                    Color.Green,
                ),
                createLine(
                    pointsDewPoint,
                    Color.Red,
                    Color.Green,
                ),
                createLine(
                    thresholdLine,
                    Color.Green,
                    Color.Green,
                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        //gridLines = GridLines()
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}

@Composable
fun ChartHistory() {
    Column(Modifier.padding(8.dp)) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Max Wind (ground) ", style = TextStyle(color = Color.Black))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Max Wind (altitude) ", style = TextStyle(color = Color.Gray))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Max Wind Shear ", style = TextStyle(color = Color.Yellow))
            Spacer(modifier = Modifier.width(2.dp))
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Cloud Coverage ", style = TextStyle(color = Color.Cyan))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Rain ", style = TextStyle(color = Color.Magenta))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Fog ", style = TextStyle(color = Color.DarkGray))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Relative Humidity ", style = TextStyle(color = Color.Blue))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Dew Point ", style = TextStyle(color = Color.Red))
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Threshold Line ", style = TextStyle(color = Color.Green))
            Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

fun createLine(
    points: List<Point>,
    lineColour: Color,
    fillColour: Color
): Line {
    return Line(
        dataPoints = points,
        LineStyle(color = lineColour),
        IntersectionPoint(radius = 0.1.dp),
        SelectionHighlightPoint(),
        ShadowUnderLine(color = fillColour, alpha = 0.03f),
        SelectionHighlightPopUp()
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
        if (rescaled > 10.0)
            rescaled = 10.0
        return rescaled
    }

    return 0.0
}

@Composable
@Preview
fun pre() {
    SingleLineChartWithGridLines(data)
}
