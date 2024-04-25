package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenUiState
import no.uio.ifi.in2000.team_17.ui.thresholds.InfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSheet(
    homeScreenUiState: HomeScreenUiState,
    toThresholdsScreen: () -> Unit,
    setMaxHeight: (String) -> Unit,
    setLat: (String) -> Unit,
    setLng: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState : Boolean
) {
    if (sheetState) {
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            InputSheetContent(
                homeScreenUiState = homeScreenUiState,
                toAdvancedSettings = toThresholdsScreen,
                setMaxHeight = { setMaxHeight(it) },
                setLat = {setLat(it)},
                setLng = {setLng(it)},
            )
        }
    }
}

@Composable
fun InputSheetContent(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings:()->Unit,
    setMaxHeight:(String) -> Unit,
    setLat:(String) -> Unit,
    setLng:(String) -> Unit,
) {
    var maxHeightText by remember { mutableStateOf(homeScreenUiState.maxHeight.toString()) }
    var latString by remember { mutableStateOf(homeScreenUiState.latLng.latitude.toString()) }
    var lngString by remember { mutableStateOf(homeScreenUiState.latLng.longitude.toString()) }
    var showInfoCard by remember { mutableStateOf(false) }
    Box{
        Column(
            modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier.padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                InputTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = maxHeightText,
                    onValueChange = { maxHeightText = it },
                    label = "Maximum height in km"
                ) { setMaxHeight(maxHeightText) }

                Box(
                    Modifier.fillMaxWidth(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        modifier = Modifier.clickable {
                            showInfoCard = !showInfoCard
                        })
                }
            }
            Row(
                modifier.padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputTextField(
                    value = latString,
                    onValueChange = { latString = it },
                    label = "Latitude",
                    modifier = Modifier.weight(1f)
                ) { setLat(latString) }
                InputTextField(
                    value = lngString,
                    onValueChange = { lngString = it },
                    label = "Longitude",
                    modifier = Modifier.weight(1f)
                ) { setLng(lngString) }
            }
            ListItem(
                modifier = modifier.padding(top = 15.dp),
                colors = ListItemDefaults.colors(MaterialTheme.colorScheme.primaryContainer),
                headlineContent = {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 7.dp),
                        fontWeight = FontWeight.SemiBold,
                        text = "Thresholds",
                        style = TextStyle(
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                },
                supportingContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ConditionalText(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 7.dp),
                            text = "When you can launch is determined by appropriate thresholds set specifically for the needs of the Kon-tiki project by Portal Space. If your rocket can launch in different conditions you can alter them by pressing the button under:"
                        )
                        Button(onClick = { toAdvancedSettings() }) {
                            Text(text = "Change Thresholds")
                        }
                    }
                }
            )
        }
        if (showInfoCard) {
            Box(
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                ElevatedCard(
                    colors = CardDefaults.cardColors().copy(
                        containerColor = MaterialTheme.colorScheme.background.copy(0.95f)
                    )
                ) {
                    InfoSection(
                        title = "Max height",
                        description = "Max height is the maximum possible height the rocket can reach. This wil be the maximum height we wil consider when calculating if the rocket can launch or not."
                    )
                    Box(
                        Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { showInfoCard = !showInfoCard }) {
                            Text(text = "Close")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onDone: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDone(value)
            }
        ),
        modifier = modifier
    )
}




@Composable
fun ConditionalText(modifier: Modifier = Modifier, text: String){

    val minimumLineLength = 1   //Change this to your desired value

    //Adding States
    var expandedState by remember { mutableStateOf(false) }
    var showReadMoreButtonState by remember { mutableStateOf(false) }
    val maxLines = if (expandedState) 200 else minimumLineLength

    Column(modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,                   //Make sure to add this line
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                if (textLayoutResult.lineCount > minimumLineLength-1) {           //Adding this check to avoid ArrayIndexOutOfBounds Exception
                    if (textLayoutResult.isLineEllipsized(minimumLineLength-1)) showReadMoreButtonState = true
                }
            }
        )
        if (showReadMoreButtonState) {
            Text(
                text = if (expandedState) "Read Less" else "Read More",
                color = MaterialTheme.colorScheme.secondary,

                modifier = Modifier.clickable {
                    expandedState = !expandedState
                },

                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )

            )
        }

    }
}