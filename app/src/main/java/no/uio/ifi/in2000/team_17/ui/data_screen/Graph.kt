package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.PlotType
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
import no.uio.ifi.in2000.team_17.model.WeatherDataLists


val data = listOf(
    Point(0f, 40f),
    Point(1f, 20f),
    Point(2f, 50f),
    Point(3f, 10f),
    Point(4f, 20f),
    Point(5f, 30f),
    Point(6f, 10f),
    Point(7f, 60f),
    Point(8f, 20f),
    Point(9f, 30f),
    Point(10f, 60f),
    Point(11f, 80f),
    Point(12f, 50f),
    Point(13f, 30f),
    Point(14f, 10f),
    Point(15f, 90f))

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

    LineChart(modifier = Modifier
        .height(300.dp),
        lineChartData = data1 )
}

@Composable
@Preview
fun pre(){
    SingleLineChartWithGridLines(data, data1)
}
