package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.Background

enum class Toggle {
    TABLE,
    GRAPH
}

/**
 * The Data screen displays the weatherData using the composable
 * [Table] and [ThresholdGraph] which present the data in different ways
 * @param windowSizeClass is used to make the screen reactive
 * @param dataScreenUiState contains the relevant data
 * @param dontShowAgain is a lambda used to not show the tutorial again
 * @param setTimeIndex is a lambda used to set a time for the home screen
 */
@Composable
fun DataScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: DataScreenViewModel,
    setTimeIndex: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var toggleState by rememberSaveable { mutableStateOf(Toggle.TABLE) }
    var selectedTimeIndex by rememberSaveable { mutableIntStateOf(uiState.selectedTimeIndex) }
    var showingTimeIndex by rememberSaveable { mutableIntStateOf(uiState.selectedTimeIndex) }

    var scrollToItem by remember { mutableStateOf<Int?>(null) }
    //var selectedTimeLocked by remember { mutableStateOf(true) }
    var graphTutorialIsDismissed by remember { mutableStateOf(false) }
    var tableTutorialIsDismissed by remember { mutableStateOf(false) }
    var waitingForSettings by remember { mutableStateOf(true) }

    var showInfoBox by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(500)
        waitingForSettings = false

    }
    if (uiState.weatherDataLists.date.size > 1) {
        selectedTimeIndex = uiState.selectedTimeIndex
        //showSwipe = dataScreenUiState.showSwipe.value
    }
    //val configuration = LocalConfiguration.current

    Background()
    Box(
        modifier
            .fillMaxSize()
    ) {
        Column {
            SelectTimeCard(
                dataScreenUiState = uiState,
                indexToPin = showingTimeIndex
            ) {
                selectedTimeIndex = it
                setTimeIndex(it)
            }
            when (toggleState) {
                Toggle.TABLE -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(Modifier.padding(bottom = 30.dp)) {
                            Table(
                                scrollToItem = scrollToItem,
                                uiState = uiState,
                                selectedIndex = showingTimeIndex,
                                setIndex = {
                                    showingTimeIndex = it
                                },
                                boxWidth = 70,
                                dividerPadding = 4,
                            )
                            if (!tableTutorialIsDismissed && uiState.showTableTutorial && !waitingForSettings) {
                                GraphInfoDialog(
                                    onDismiss = { tableTutorialIsDismissed = true },
                                    onDontShowAgain = {
                                        tableTutorialIsDismissed = true
                                        viewModel.dontShowTableTurotialAgain()
                                    },
                                    painter = painterResource(id = R.drawable.swipe),
                                    text = "Scroll left to see more weather data."
                                )
                            }
                        }
                    }
                }

                Toggle.GRAPH -> {
                    ThresholdGraph(
                        dataScreenUiState = uiState,
                        windowSizeClass = windowSizeClass,
                        showInfoBox = showInfoBox,
                        closeInfoBox = { showInfoBox = false }
                    ) {
                        showingTimeIndex = it
                    }
                    if (!graphTutorialIsDismissed && uiState.showGraphTutorial && !waitingForSettings) {
                        GraphInfoDialog(
                            onDismiss = { graphTutorialIsDismissed = true },
                            onDontShowAgain = {
                                graphTutorialIsDismissed = true
                                viewModel.dontShowGraphTurotialAgain()
                            },
                            painter = painterResource(id = R.drawable.swipe),
                            text = "Scroll left to see more weather data.\nPinch to zoom."
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            Modifier.height(45.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (toggleState == Toggle.TABLE) {
                TextButton(modifier = Modifier.width(80.dp), onClick = { scrollToItem = 0 }) {
                    Text(text = "Now")
                }
            } else {
                IconButton(
                    modifier = Modifier.width(50.dp),
                    onClick = { showInfoBox = !showInfoBox }) {
                    Icon(Icons.Outlined.Info, "info")
                }
            }
            ToggleButton {
                when (it) {
                    0 -> toggleState = Toggle.TABLE
                    1 -> toggleState = Toggle.GRAPH
                }
            }
            if (toggleState == Toggle.TABLE) {
                TextButton(
                    modifier = Modifier.width(80.dp),
                    onClick = { scrollToItem = selectedTimeIndex }) {
                    Text("Selected")
                }
            } else {
                Box(modifier = Modifier.width(50.dp))
            }
            if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) {
                Box(Modifier.size(50.dp)) {}
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    onFlip: (Int) -> Unit
) {
    val options = remember { mutableStateListOf("Table", "Graph") }
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                modifier = modifier,
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onFlip(index)
                },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {},
                colors = SegmentedButtonColors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeBorderColor = Color.DarkGray,
                    activeContentColor = Color.Black,
                    inactiveBorderColor = Color.DarkGray,
                    inactiveContainerColor = MaterialTheme.colorScheme.background.copy(1.0f),
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
