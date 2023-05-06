package com.itec.yussarent.ui.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun CardInfoView(itemCount:Int, itemName:String, root:String, navController: NavHostController, icon: @Composable () -> Unit){

    val cardBackgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable {
                    navController.navigate(root)
                },
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = cardBackgroundColor,
                contentColor = textColor
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.height(15.dp)) {
                    icon()
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = " $itemName",
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = itemCount.toString(),
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 14.dp)
                )
            }

        }
    }
}

