package com.example.yussarent.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
//    var showDatePicker by remember { mutableStateOf(false) }
    val calendarState = rememberSheetState()
//    val today = LocalDate.now()
//    val maxYear = today.year

    val calendarConfig = CalendarConfig(
        monthSelection = true,
        yearSelection = true,
        minYear = 1900,
//        maxYear = maxYear,
        disabledDates = null,
//        disabledTimeline = CalendarTimeline.FUTURE
    )

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            onDateSelected(date)
        },
        config = calendarConfig,
        header = null,
        properties = DialogProperties()
    )


    Box(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.onTertiary)

    ) {
        Button(
            onClick = {  calendarState.show() },
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            content = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected date: ${selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    IconButton(
                        onClick = {
                            calendarState.show()
                        },
                        modifier = Modifier.size(48.dp),
                        content = {
                            Icon(
                                Icons.Filled.CalendarToday,
                                contentDescription = "Select date",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }
        )
    }

}
