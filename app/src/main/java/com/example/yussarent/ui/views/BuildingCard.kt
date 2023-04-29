package com.example.yussarent.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.yussarent.data.models.Building

@Composable
fun BuildingCard(building: Building,isSelected: Boolean,onClick:()->Unit){
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    }
    else{
        MaterialTheme.colorScheme.onSurface
    }
    Box(
        modifier = Modifier
            .padding(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable {
                    onClick()
                },
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = textColor
            )
        ) {
        Column {
                Text(
                    text = building.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor,
                    modifier = Modifier.padding(12.dp)
                )

        }
        }
    }
}


