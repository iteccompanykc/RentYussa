package com.example.yussarent.ui.views
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yussarent.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val alpha = animateFloatAsState(targetValue = 1f, animationSpec = TweenSpec(durationMillis = 2000))
    val translationX = animateFloatAsState(
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = TweenSpec(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val typography = MaterialTheme.typography.titleMedium
    var textVisible by remember { mutableStateOf(false) }
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(180.dp)
                    .width(180.dp),
            )
            AnimatedVisibility(
                visible = textVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Row( modifier = Modifier
                    .padding(bottom = 16.dp, end = 32.dp)
                    .alpha(alpha.value)
                    .offset(translationX.value.dp, 0.dp)
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.BottomCenter)) {
                    Text(
                        text = "Powered by",
                        style = typography
                    )
                    Text(
                        text = "ITEC",
                        color = Color(android.graphics.Color.parseColor("#5F021F")),
                        style = typography
                    )
                }

            }
        }
    }
    LaunchedEffect(Unit) {
        delay(1000L)
        textVisible = true
        delay(2000L)
        onAnimationFinished()
    }
}




