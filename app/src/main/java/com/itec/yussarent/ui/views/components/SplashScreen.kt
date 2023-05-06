package com.itec.yussarent.ui.views.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.itec.yussarent.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onButtonClick: () -> Unit) {
    val alpha = animateFloatAsState(targetValue = 1f, animationSpec = TweenSpec(durationMillis = 2000))

    val typography = MaterialTheme.typography.titleMedium
    var textVisible by remember { mutableStateOf(false) }
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = MaterialTheme.colorScheme.surface
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1)),
                        startY = 0f,
                        endY = 500f
                    )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(70.dp)
                    .width(50.dp)
                    .padding(top = 16.dp, start = 16.dp),
                alignment = Alignment.TopStart
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                    Column(
                        modifier = Modifier
                            .alpha(alpha.value)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedVisibility(
                            visible = textVisible,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it })
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.itec_logo),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(180.dp)
                                    .width(200.dp)
                                    .padding(top = 32.dp)
                            )
                        }
                        Text(
                            text = "Rent management made easy",
                            style = typography,
                            color = Color.White,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to the app!",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(top = 48.dp)
                    )

                    AnimatedButton(
                        onClick = { onButtonClick() },
                        text = "Get started",
                        icon = Icons.Default.ArrowForward
                    )

                }
            }
        }
    }
    LaunchedEffect(Unit) {
        delay(500L)
        textVisible = true
    }
}





