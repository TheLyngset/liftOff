package no.uio.ifi.in2000.team_17.ui.judicial_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun JudicialScreen(modifier: Modifier) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("This is the Judicial Screen")
        Text(
            "Juridisk fra Håkon: \nKart over luftområder: https://www.ippc.no/ippc/index.jsp\n" +
                    "Api'et til kartet: https://ais.avinor.no/no/AIP/\n" +
                    "Modellrakett reglene: https://www.nar.org/safety-information/model-rocket-safety-code/"
        )
    }
}