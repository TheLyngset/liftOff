package no.uio.ifi.in2000.team_17.ui.theme.locationforecastTestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationforecastScreenTest(
    locationforecastViewModel: LocationforecastViewModel = viewModel(),
    //onNavigateToHomeScreen: () -> Unit,
) {
    val locationforecastUiState: LocationforecastUiState by locationforecastViewModel.locationforecastUiState.collectAsState()
    val windSpeed = locationforecastUiState.locationforecastData.windSpeed
    val windDirection = locationforecastUiState.locationforecastData.windFromDirection
    val rainNextHour = locationforecastUiState.locationforecastData.rain
    val clouds = locationforecastUiState.locationforecastData.cloudFraction
    //val dateTime = locationforecastUiState.locationforecastData.properties?.timeseries?.firstOrNull()?.time


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "LocationforecastScreenTEST")
                    }
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                item {
                    Row {
                        Column {
                            // Text(text = "Coordinates: ${geometry?.coordinates}")
                            // Text(text = "DateTime: ${dateTime}")
                        }
                    }
                    Row(Modifier.background(MaterialTheme.colorScheme.primary)) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "WindSpeed",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "WindFrom",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "RainNextH",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "Clouds",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
               }
                    Row(Modifier.background(MaterialTheme.colorScheme.primary)) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "${windSpeed}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "${windDirection}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "${rainNextHour}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "${clouds}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                }

            }
        }
    }

}
