package no.uio.ifi.in2000.team_17.ui.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
        ElevatedCard {
            Box() {
                Column(Modifier.padding(16.dp)) {
                    Text(text = "No internet connection, make sure you are connected to the internet and relaunch the app")
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