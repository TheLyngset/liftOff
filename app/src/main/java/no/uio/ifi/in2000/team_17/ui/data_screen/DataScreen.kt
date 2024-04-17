package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.model.WindLayer

const val INDEXES = 40
@Composable
fun DataScreen(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState
){
    val timeState = rememberLazyListState()
    val groundWindState = rememberLazyListState()

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollableState{delta ->
        scope.launch{
            timeState.scrollBy(-delta)
            groundWindState.scrollBy(-delta)
        }
        delta
    }

    Row(
        modifier
            .fillMaxSize()
    ) {
        Column(Modifier.scrollable(scrollState, Orientation.Horizontal, flingBehavior = ScrollableDefaults.flingBehavior())) {
            Text(text = "Time:")
            Text(text = "GroundWind:")
            Text("Max Wind Shear:")
            Text("Cloud fraction:")
            Text("Median rain:")
            Text("Humidity:")
            Text("Dew point")
            Text("Fog fraction")
        }
        Column(Modifier.horizontalScroll(rememberScrollState())) {
            Row{for(i in 0..INDEXES){ InfoBox(info = dataScreenUiState.weatherDataLists.time.getOrElse(i){"n/a"})} }
            Row{for(i in 0..INDEXES){ InfoBox(info = dataScreenUiState.weatherDataLists.groundWind.getOrElse(i){ WindLayer("n/a") }.speed)} }
        }
    }
}

@Composable
fun InfoBox(info: String){
    Box(modifier = Modifier
        .width(50.dp),
        contentAlignment = Alignment.Center){
        Text(text = info)
    }
}