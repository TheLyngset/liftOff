package no.uio.ifi.in2000.team_17.ui.thresholds
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
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
                Box(contentAlignment = Alignment.TopEnd){
                    Column(
                        Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InfoSection(
                            title = stringResource(R.string.safety_margin_title),
                            description = stringResource(R.string.safety_margin_description)
                        )
                        InfoSection(
                            title = stringResource(R.string.max_ground_wind_title),
                            description = stringResource(R.string.max_ground_wind_description)
                        )
                        InfoSection(
                            title = stringResource(id = R.string.maxAirWind_title),
                            description = stringResource(R.string.maxWind_description)
                        )
                        InfoSection(
                            title = stringResource(id = R.string.maxShear_titleLong),
                            description = stringResource(R.string.maxShear_description)
                        )
                        InfoSection(
                            title = stringResource(R.string.max_cloud_fraction_title),
                            description = stringResource(R.string.max_cloud_fraction_description)
                        )
                        InfoSection(
                            title = stringResource(R.string.max_fog_title),
                            description = stringResource(R.string.max_fog_description)
                        )
                        InfoSection(
                            title = stringResource(R.string.max_allowed_rain_title),
                            description = stringResource(R.string.max_allowed_rain_description)
                        )
                        InfoSection(
                            title = stringResource(R.string.max_humidity_title),
                            description = stringResource(R.string.max_humidity_description)
                        )
                        InfoSection(
                            title = stringResource(R.string.max_dew_point_title),
                            description = stringResource(R.string.max_dew_point_description)
                        )

                    }
                    IconButton(
                        onClick = { onDone() }) {
                        Icon(Icons.Outlined.Close, "Close")
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