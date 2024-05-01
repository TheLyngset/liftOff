package no.uio.ifi.in2000.team_17.ui.input_sheet

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import no.uio.ifi.in2000.team_17.ui.ConditionalText
import no.uio.ifi.in2000.team_17.ui.thresholds.InfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSheet(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    viewModel: InputSheetViewModel,
    toThresholdsScreen: () -> Unit,
    setMaxHeight: (String) -> Unit,
    setLat: (String) -> Unit,
    setLng: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: Boolean,

) {
    val uiState by viewModel.uiState.collectAsState()
    if (sheetState) {
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            InputSheetContent(
                uiState = uiState,
                toAdvancedSettings = toThresholdsScreen,
                setMaxHeight = { setMaxHeight(it) },
                setLat = {setLat(it)},
                setLng = {setLng(it)},
                snackbarHostState = snackbarHostState,
                onDismiss = {
                    onDismiss()
                },
            )
        }
    }
}

@Composable
fun InputSheetContent(
    modifier: Modifier = Modifier,
    uiState: InputSheetUiState,
    toAdvancedSettings:()->Unit,
    setMaxHeight:(String) -> Unit,
    setLat:(String) -> Unit,
    setLng:(String) -> Unit,
    snackbarHostState: SnackbarHostState,
    onDismiss:() -> Unit
) {
    var maxHeightText by remember { mutableStateOf(uiState.maxHeight.toString()) }
    var latString by remember { mutableStateOf(uiState.latLng.latitude.toString()) }
    var lngString by remember { mutableStateOf(uiState.latLng.longitude.toString()) }
    var showInfoCard by remember { mutableStateOf(false) }
    var changeValue by remember { mutableStateOf(false) }

    if (changeValue) {
        LaunchedEffect(key1 = changeValue) {
            snackbarHostState.showSnackbar("Value updated")
            changeValue = false
        }
    }

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
                    label = "Maximum height in km",

                ) { setMaxHeight(maxHeightText); }

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
                    modifier = Modifier.weight(1f),

                ) { setLat(latString) }
                InputTextField(
                    value = lngString,
                    onValueChange = { lngString = it },
                    label = "Longitude",
                    modifier = Modifier.weight(1f),

                ) { setLng(lngString) }
            }
            if (changeValue) {
                LaunchedEffect(key1 = changeValue) {
                    snackbarHostState.showSnackbar("Value updated")
                    changeValue = false
                }
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
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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

