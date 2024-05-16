package no.uio.ifi.in2000.team_17.ui.data_screen

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


/**
 * The TresholdsGraph fun creates the ui of the graph in data screen
 * @param uiState is used to load relevant data
 * @param  windowSizeClass is used find the heightSizeClass and set the height of the graph
 * @param showInfoBox is boolean determining whether the info box with names and colours of variables is going to be displayed
 * @param closeInfoBox is a lambda used close the infobox
 * @param backgroundSwitch is boolean used determined the background pallets of the graph
 * @param onFlip is a lambda used swap graph background between coloured vs. colourblind friendly background
 * @param setTimeIndex is a lambda used pin the chosen date/time/location to homescreen
 */
@Composable
fun ThresholdGraph(
    uiState: DataScreenUiState,
    screenReaderOn: Boolean,
    windowSizeClass: WindowSizeClass,
    showInfoBox: Boolean,
    closeInfoBox: () -> Unit,
    backgroundSwitch: Boolean,
    onFlip: () -> Unit,
    setTimeIndex: (Int) -> Unit
) {
    // generating the data for the graph
    val data = generateLineChartData(uiState, backgroundSwitch) { setTimeIndex(it) }


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
            if (!screenReaderOn) {
                Box(contentAlignment = Alignment.TopCenter) {
                    GraphEdgeGradient(top = true)
                    Row(
                        Modifier
                            .height(25.dp)
                            .fillMaxWidth()
                            .padding(end = 8.dp, bottom = 1.dp),
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
                GraphEdgeGradient(top = false)
            }
        }
        if (showInfoBox) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = if (horizontal) Alignment.CenterEnd else Alignment.BottomCenter
            ) {
                InfoBox(
                    lastUpdated = uiState.weatherDataLists.updated,
                    bottomPadding = if (horizontal) 45.dp else 55.dp,
                    horizontal = horizontal
                ) {
                    closeInfoBox()
                }
            }
        }
    }
}

/**
 * Generates a color gradient for the graph based on the margin and the background switch status.
 *
 * @param margin The margin used to determine the number of green colors in the gradient.
 * @param backgroundSwitch A boolean value indicating whether the background switch is on or off. If it's on, the gradient will include colors for red, yellow, and green. If it's off, the gradient will only include white.
 * @return A list of colors representing the gradient.
 */
fun generateColorGradient(margin: Double, backgroundSwitch: Boolean): List<Color> {
    var colors: List<Color> =
        listOf(
            Color.White,
            Color.White
        )
    if (backgroundSwitch) {
        val nGreen = round(margin * 5).toInt()
        colors =
            (1..nGreen).map { TrafficLightColor.RED.color.copy(1f) } +
                    (1..((5 - nGreen) * 2)).map { TrafficLightColor.YELLOW.color.copy(1f) } +
                    (1..nGreen).map { TrafficLightColor.GREEN.color.copy(1f) }
    }
    return colors
}

/**
 * Generates a list of points for a graph based on the provided data and threshold.
 *
 * @param data The list of data values to be plotted on the graph.
 * @param threshold The threshold value used to determine the y-coordinate of each point.
 * @param absMinDew The absolute minimum dew point used to adjust the y-coordinate of each point. Default value is 0.0.
 * @return A list of points representing the data to be plotted on the graph.
 */
fun generatePoints(data: List<Double>, threshold: Double, absMinDew: Double = 0.0): List<Point> {
    return List(data.size) { index ->
        Point(
            x = index.toFloat(),
            y = (rescalePoint(
                data[index] + absMinDew, threshold + absMinDew
            )).toFloat()
        )
    }
}

/**
 * A composable function that creates a gradient at the edge of the graph.
 *
 * @param modifier The modifier to be applied to this function.
 * @param top A boolean value indicating whether the gradient is at the top of the graph. If true, the gradient starts from transparent and ends at the background color. If false, the gradient starts from the background color and ends at transparent.
 */
@Composable
fun GraphEdgeGradient(modifier: Modifier = Modifier, top: Boolean) {
    val colors = if (top) {
        listOf(
            MaterialTheme.colorScheme.background.copy(0f),
            MaterialTheme.colorScheme.background.copy(1f)
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.background.copy(1f),
            MaterialTheme.colorScheme.background.copy(0f)
        )
    }
    Box(
        modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(
                brush = Brush.verticalGradient(colors)
            )
    )
}

/**
* rescales the dew point so that if the list contains a negative value,
* the entire scale is moved up in order to avoid dealing with negative values
 * @param weatherDataLists the list of weather data
 * @return the absolute minimum dew point
 */
fun absMinDew(weatherDataLists: WeatherDataLists): Double {
    val min = weatherDataLists.dewPoint.minOrNull()
    if (min is Double)
        return kotlin.math.abs(min)
    return 0.0
}

/**
*calculates each point on the line as percentage of its own threshold
* purpose: create a common threshold line for all points
* to show visually if the values are below or above their respective threshold.
 * @param realValue the real value of the point
 * @param threshold the threshold value of the point
 * @return the rescaled value of the point
 */
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

/**
 * the function called when toggling the switch graph background switch. Switches between coloured vs. colourblind friendly background
 * @param checked the current state of the switch
 * @param onFlip the function to be called when the switch is toggled
 */
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
        modifier = Modifier.scale(0.8f)
    )
}

/**
* Retrieves the date and time when the data was last updated
* follows the lastUpdated that comes from the Locationforecast API
 * @param lastUpdated the last updated time
 */
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


/**
* Shows a tutorial informing the user that the graph is scrollable horizontally
* and that it is possible to zoom in/out on the graph
 * @param onDismiss the function to be called when the dialog is dismissed
 * @param onDontShowAgain the function to be called when the dialog is dismissed and the user does not want to see it again
 * @param painter the painter used to display the image in the dialog
 * @param text the text to be displayed in the dialog
 */
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
                    contentDescription = null,
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

/**
* Defines the UI and constraints used to show the infobox with graph lines name and colours
 * @param modifier the modifier to be applied to the infobox
 * @param lastUpdated the last updated time
 * @param bottomPadding the padding at the bottom of the infobox
 * @param horizontal a boolean value indicating whether the infobox is horizontal or not
 * @param onDismiss the function to be called when the infobox is dismissed
 */
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

/**
* The content of the infobox, which is shown when the user clicks on the info button in the graph
 */
@Composable
fun InfoBoxContent() {
    Column(modifier = Modifier.padding(start = 7.dp)) {
        Row(Modifier.padding(top = 5.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color.Black)
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.groundWind_title),
                style = TextStyle(color = Color.Black)
            )
        }
        Row(Modifier.padding(vertical = 10.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(color = Color(0XFFC71585))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.maxAirWind_title),
                style = TextStyle(color = Color.Black)
            )
        }
        Row {
            Canvas(modifier = Modifier
                .size(15.dp)
                .padding(bottom = 2.dp), onDraw = {
                drawCircle(color = Color(0XFFCC5500))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.maxShear_titleLong),
                style = TextStyle(color = Color.Black)
            )
        }
    }

    Column(modifier = Modifier.padding(start = 7.dp)) {
        Row(Modifier.padding(top = 5.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(Color(0XFF000080))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.humidity_title),
                style = TextStyle(color = Color.Black)
            )
        }
        Row(Modifier.padding(vertical = 10.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(Color(0XFF8B4513))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = stringResource(R.string.rain_title), style = TextStyle(color = Color.Black))
        }
        Row {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(Color(0XFF800080))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.fog_title),
                style = TextStyle(color = Color.DarkGray)
            )
        }
    }
    Column(modifier = Modifier.padding(start = 7.dp)) {
        Row(Modifier.padding(top = 5.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(Color(0XFF006600))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.dewPoint_title),
                style = TextStyle(color = Color.Black)
            )
        }
        Row(Modifier.padding(vertical = 10.dp)) {
            Canvas(modifier = Modifier.size(15.dp), onDraw = {
                drawCircle(Color(0XFF008080))
            })
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.cloudFraction_title),
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
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.threshold_line),
                style = TextStyle(color = Color.Black)
            )
        }
    }
}

/**
 * Creates a line for the graph with the given points, line color, and text.
 * @param points The list of points to be plotted on the line.
 * @param lineColor The color of the line.
 * @param fillColor A boolean value indicating whether the line should be filled with color.
 */
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

/**
 * Generates the data for the graph based on the provided UI state.
 * @param uiState The UI state containing the weather data lists and thresholds.
 * @param backgroundSwitch A boolean value indicating whether the background switch is on or off.
 * @param setTimeIndex The function to be called when a point on the graph is selected.
 * @return LineChartData object containing the data for the graph.
 */
@Composable
fun generateLineChartData(
    uiState: DataScreenUiState,
    backgroundSwitch: Boolean,
    setTimeIndex: (Int) -> Unit
): LineChartData {
    //creating necessary values for the graph
    val weatherDataLists = uiState.weatherDataLists
    val thresholds = uiState.thresholds
    val size = weatherDataLists.time.size

    // retrieving data for the graph lines
    // parsing the data in a way that can be displayed by the graph
    //all values are calculated by rescale() as percentage of their respective threshold
    val pointsGroundWind: List<Point> =
        generatePoints(weatherDataLists.groundWind.map { it.speed }, thresholds.groundWindSpeed)
    val pointsMaxAirWind: List<Point> =
        generatePoints(weatherDataLists.maxWind.map { it.speed }, thresholds.maxWindSpeed)
    val pointsMaxWindShear: List<Point> =
        generatePoints(weatherDataLists.maxWindShear.map { it.speed }, thresholds.maxWindShear)
    val pointsCloudFraction: List<Point> =
        generatePoints(weatherDataLists.cloudFraction, thresholds.cloudFraction)
    val pointsRain: List<Point> =
        generatePoints(weatherDataLists.rain.map { it.probability }, thresholds.rain)
    val pointsFog: List<Point> = generatePoints(weatherDataLists.fog, thresholds.fog)
    val pointsHumidity: List<Point> = generatePoints(weatherDataLists.humidity, thresholds.humidity)
    val pointsDewPoint: List<Point> =
        generatePoints(weatherDataLists.dewPoint, thresholds.dewPoint, absMinDew(weatherDataLists))
    val thresholdLine = List(weatherDataLists.date.size) { Point(x = it.toFloat(), y = 1f) }
    val upperLine = List(weatherDataLists.date.size) { Point(x = it.toFloat(), y = 2f) }

    val pointsTime: List<Point> = List(weatherDataLists.time.size) { index ->
        Point(
            x = index.toFloat(),
            y = ("${
                weatherDataLists.time[index].subSequence(0, 2)
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

    //generating the x and y-axis of the graph
    val xAxisData = AxisData.Builder()
        .backgroundColor(color = Color.Transparent)
        .axisStepSize(30.dp)
        .topPadding(2.dp)
        .bottomPadding(2.dp)
        .steps(size - 1)
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
            (i * yScale).formatToSinglePrecision()
        }
        .build()

    val colors = generateColorGradient(uiState.thresholds.margin, backgroundSwitch)
    return LineChartData(
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
                            dateAndTime
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
                    stringResource(R.string.groundWind_title)
                ),
                createLine(
                    pointsMaxAirWind,
                    Color(0XFFC71585),
                    false,
                    stringResource(R.string.maxAirWind_title)
                ),
                createLine(
                    pointsMaxWindShear,
                    Color(0XFFCC5500),
                    false,
                    stringResource(R.string.maxShear_titleLong)
                ),
                createLine(
                    pointsCloudFraction,
                    Color(0XFF008080),
                    false,
                    stringResource(R.string.cloudFraction_title)
                ),
                createLine(
                    pointsRain,
                    Color(0XFF8B4513),
                    false,
                    stringResource(R.string.rain_title)
                ),
                createLine(
                    pointsFog,
                    Color(0XFF800080),
                    false,
                    stringResource(R.string.fog_title)
                ),
                createLine(
                    pointsHumidity,
                    Color(0XFF000080),
                    false,
                    stringResource(R.string.humidity_title)

                ),
                createLine(
                    pointsDewPoint,
                    Color(0XFF006600),
                    false,
                    stringResource(R.string.dewPoint_title)
                ),
            )
        ),
        //defining constraints for the graph
        backgroundColor = MaterialTheme.colorScheme.background.copy(1f),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        isZoomAllowed = true,
        paddingTop = 13.dp,//just enough space to display the date and time when clicking on the graph
        bottomPadding = 5.dp,
        paddingRight = 2.dp,
        containerPaddingEnd = 2.dp,
    )
}