package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InputScreen(modifier: Modifier = Modifier){
    var maxHeightText by remember { mutableStateOf("") }
    var latLngString by remember { mutableStateOf("") }
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = maxHeightText,
            onValueChange ={ maxHeightText = it },
            label = { Text("Maximum height") }
            )
        OutlinedTextField(
            value = latLngString,
            onValueChange ={ latLngString = it },
            label = { Text("Coordinates") }
        )
        Spacer(modifier.size(16.dp))
        Spacer(modifier.size(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun InputScreenPreview(){
    InputScreen()
}