package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InputScreen(modifier: Modifier = Modifier, uiState: UIState, onValidate:(String, String)->Unit){
    var maxHeightText by remember { mutableStateOf(uiState.maxHeight.toString()) }
    var latLngString by remember { mutableStateOf("${uiState.latLng.latitude}, ${uiState.latLng.longitude}") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = maxHeightText,
            onValueChange ={ maxHeightText = it },
            label = { Text("Maximum height in km") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                })
            )
        OutlinedTextField(
            value = latLngString,
            onValueChange ={ latLngString = it },
            label = { Text("Coordinates") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                })
        )
        Button(
            onClick = { onValidate(latLngString, maxHeightText) }) {
            Text("Validate")
        }
        Spacer(modifier.size(16.dp))
    }
}