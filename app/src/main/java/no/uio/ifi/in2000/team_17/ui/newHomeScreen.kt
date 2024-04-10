package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import no.uio.ifi.in2000.team_17.R

@Composable
fun newHomeScreen(modifier: Modifier = Modifier) {
    val cameraPositionState = rememberCameraPositionState {

    }
    Box(modifier = Modifier
        .fillMaxSize(1f)
        .background(Color.Cyan)


    ){
        Image(painter = painterResource(id = R.drawable.sky),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(
                    scaleX = 2.4f,
                    scaleY = 1.6f,
                    translationX = 100f
                )

            )
    }


}

@Preview
@Composable
fun prehs(){
    newHomeScreen()
}

