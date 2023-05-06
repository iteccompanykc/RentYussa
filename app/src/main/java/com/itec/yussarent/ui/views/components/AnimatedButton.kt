package com.itec.yussarent.ui.views.components
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector
) {
    var arrowAlpha by remember { mutableStateOf(1f) }

    val arrowOffsetX by animateFloatAsState(
        targetValue = if (arrowAlpha == 1f) 10f else 0f
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(16.dp),
        elevation = null,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
            )

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .alpha(arrowAlpha)
                    .offset(x = arrowOffsetX.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        var i=0
        while (i<5) {
            delay(1000)
            arrowAlpha = if (arrowAlpha == 1f) 0f else 1f
            i++
        }
        onClick()
    }
}

