package no.uio.ifi.in2000.team_17.ui.thresholds
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.ui.ConditionalText

@Composable
fun ThresholdsInfo(modifier: Modifier = Modifier,show: Boolean, onDone:()->Unit) {
    Column(
        modifier
            .padding(6.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if (show) {
            ElevatedCard(
                Modifier.fillMaxWidth(),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified
                )
            )
            {
                Box(){
                    Column(
                        Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InfoSection(
                            title = "Max Ground Wind",
                            description = "Max Ground Wind is the maximum wind speed at ground layer for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max Wind",
                            description = "max wind is the maximum wind speed at any level for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max Wind Shear",
                            description = "Max Wind Shear is the maximum wind shear at any level for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max Cloud Fraction",
                            description = "Max Cloud Fraction is the maximum cloud coverage in % for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max Fog",
                            description = "Max Fog is the maximum fog fraction in % for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max allowed rain",
                            description = "Max allowed rain is the maximum amount of rain in mm/h for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max humidity",
                            description = "Max humidity is the maximum relative humidity in % for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Max Dew Point",
                            description = "Max Dew Point is the maximum dew point temperature in ℃ for which it is safe to launch the rocket"
                        )
                        InfoSection(
                            title = "Safety margin",
                            description = "Safety margin is how much of the threshold is colored Green and Yellow for each Threshold. For example a Threshold of 10.0 with the Margin set to 0.6 wil be colored green until 6.o, yellow until 10.0 and red for anything above 10 "
                        )

                    }
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Button(onClick = { onDone() }) {
                            Text(text = "Close")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoSection2(title: String, description: String) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var showDescription by remember { mutableStateOf(false) }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )
            Spacer(Modifier.size(10.dp))
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                Modifier.clickable {
                    showDescription = !showDescription
                }
            )
        }
        if(showDescription) Text(text = description)
    }
}

@Composable
fun InfoSection(title: String, description: String) {
    Column(
        Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
        Text(text = description)
    }
}
@Preview
@Composable
fun ThresholdsInfoPreview() {
    ThresholdsInfo(show = true){

    }
}