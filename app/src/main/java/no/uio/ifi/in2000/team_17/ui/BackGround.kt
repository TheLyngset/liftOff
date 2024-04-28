package no.uio.ifi.in2000.team_17.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import no.uio.ifi.in2000.team_17.R


@Composable
fun Rocket() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rakett))
    var isPlaying by remember {
        mutableStateOf(true)
    }
    val progress by animateLottieCompositionAsState(composition = composition, isPlaying)
    LaunchedEffect(key1 = progress) {
        if (progress ==0f){
            isPlaying = true
        }
        if (progress ==1f){

            isPlaying = false
        }
    }
    LottieAnimation(composition = composition, progress = {progress})

    if (isPlaying == false){
        isPlaying = true
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