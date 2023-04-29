package com.example.yussarent.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yussarent.data.models.User
import com.example.yussarent.ui.theme.RentTheme
import com.example.yussarent.util.ThemeManager
import com.example.yussarent.R


@Composable
fun ProfileAndSettings(user: User, onLogout:  () -> Unit) {
    val cardBackgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Profile and Settings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        // User Profile section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = cardBackgroundColor,
                contentColor = textColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Profile details
                ProfileDetails(
                    user,
                    onLogout
                    )
            }
        }

        // Settings section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = cardBackgroundColor,
                contentColor = textColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Dark mode switch
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val switchTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    val switchThumbColor = if (ThemeManager.isDarkTheme) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                    val switchIcon = if (ThemeManager.isDarkTheme) {
                        painterResource(id = R.drawable.dark)
                    } else {
                        painterResource(id = R.drawable.light)
                    }
                    Text(text = "Theme",modifier=Modifier.padding(end = 12.dp))
                    Spacer(modifier = Modifier.padding(end = 12.dp))
                    Switch(
                        checked = ThemeManager.isDarkTheme,
                        onCheckedChange = { ThemeManager.isDarkTheme = it },
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = switchTrackColor, shape = CircleShape)
                            .padding(5.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = switchThumbColor,
                            uncheckedThumbColor = switchThumbColor
                        ),
                        thumbContent = {
                            Image(
                                painter = switchIcon,
                                contentDescription = "Dark Mode"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingPreview(){
    RentTheme {
        ProfileAndSettings(user = User("1","Kwizera","Kwizera","claudekwiera003@gmail.com","2","1","3","1","0788722091","2"),{})
    }
}