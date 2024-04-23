package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team_17.R


@Composable
fun Rocket() {
    Box(
        Modifier.fillMaxSize(1f)
            .padding(vertical = 20.dp)
            .offset(y = (-40).dp),
        contentAlignment = Alignment.TopCenter
    ){
        Image(
            painter = painterResource(id = R.drawable.rakett),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = 0.27f,
                    scaleY = 0.47f,
                    translationY = -132f
                )
                .alpha(0.85f)
        )
    }
}
@Composable
fun BackGroundImage() {
    Box(modifier = Modifier.fillMaxSize() ){
        Image(
            painter = painterResource(id = R.drawable.sky),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(
                    scaleX = 2.4f,
                    scaleY = 1.4f,
                    translationX = 100f,
                    translationY = 150f
                )
            )
    }
}

@Composable
fun Background() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background))
}