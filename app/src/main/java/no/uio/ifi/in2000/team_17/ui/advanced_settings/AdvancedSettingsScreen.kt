package no.uio.ifi.in2000.team_17.ui.advanced_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.team_17.ui.home_screen.InputTextField

@Composable
fun AdvancedSettingsScreen(
    modifier: Modifier = Modifier,
    uiState: AdvancedSettingsUiState
){
    Column(modifier = modifier.fillMaxSize()) {
        InputTextField(value = "Test", onValueChange = {}, label = "Label")
    }
}