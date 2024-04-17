package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSheet(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings: () -> Unit,
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
                toAdvancedSettings = toAdvancedSettings,
                setMaxHeight = { setMaxHeight(it) },
                setLat = {setLat(it)},
                setLng = {setLng(it)},
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
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings:()->Unit,
    setMaxHeight:(String) -> Unit,
    setLat:(String) -> Unit,
    setLng:(String) -> Unit,
    onDismiss:() -> Unit
) {
    var maxHeightText by remember { mutableStateOf("") }
    var latString by remember { mutableStateOf("") }
    var lngString by remember { mutableStateOf("") }
    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier.padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically) 
        {
            InputTextField(
                value = maxHeightText,
                onValueChange = { maxHeightText = it },
                label = "Maximum height in km"
            ) { setMaxHeight(maxHeightText) }
            Text(homeScreenUiState.maxHeight.toString())
        }
        Row(modifier.padding(horizontal = 15.dp), horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically) {
            InputTextField(
                value = latString,
                onValueChange = { latString = it },
                label = "Latitude",
                modifier = Modifier.weight(1f)
            ){ setLat(latString) }
            Text(homeScreenUiState.latLng.latitude.toString())
            InputTextField(
                value = lngString,
                onValueChange = { lngString = it },
                label = "Longitude",
                modifier = Modifier.weight(1f)
            ){ setLng(lngString) }
            Text(homeScreenUiState.latLng.longitude.toString())
        }
        ListItem(
            modifier = modifier.padding(top=15.dp),
            colors = ListItemDefaults.colors(MaterialTheme.colorScheme.primaryContainer),
            headlineContent = {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 7.dp)
                        .padding(top = 7.dp),
                    fontWeight = FontWeight.SemiBold,
                    text = "Advanced settings",
                    style = TextStyle(fontSize = 17.sp, color = Color.DarkGray)
                )
            },
            supportingContent = {
                Column(
                    Modifier.height(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp),
                        text = "The settings under are set with appropriate standard values, read more about why these values have been chosen here"
                    )
                    Button(onClick = {toAdvancedSettings()}) {
                        Text(text = "Advanced settings")
                    }
                }
            }
        )
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