package no.uio.ifi.in2000.team_17.ui.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team_17.ui.BackGroundImage
import no.uio.ifi.in2000.team_17.ui.Background

@Composable
fun NoDataScreen(viewModel: SplashScreenViewModel, retry:()->Unit) {
    BackGroundImage()
    Background()
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val uiState by viewModel.uiState.collectAsState()
        val missingDataFrom = if(!uiState.hasLocationforecast && !uiState.hasIsobaric) {
            "Location forecast and Isobaric api's"
        }
        else if(!uiState.hasLocationforecast){
            "Location forecast api"
        }
        else{
            "Isobaric api"
        }
        ElevatedCard {
            Box() {
                Column(Modifier.padding(16.dp)) {
                    Text(text = "We ar not receiving data from the $missingDataFrom. Make sure you are connected to the internet and try again")
                }
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    TextButton(onClick = { retry() }) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}