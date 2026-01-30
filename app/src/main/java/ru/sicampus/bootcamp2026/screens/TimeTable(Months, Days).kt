package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun TimeTableMDScreen(
    onDayClick: () -> Unit

) {
    val calendar = remember { Calendar.getInstance() }

    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonthIndex = calendar.get(Calendar.MONTH)

    var displayYear by remember { mutableStateOf(currentYear) }
    var displayMonth by remember { mutableStateOf(currentMonthIndex) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var selectedOption by remember { mutableStateOf("Option 2") }
        Spacer(modifier = Modifier.height(58.dp))
        Row {
            listOf("Неделя", "Месяц").forEach { option ->
                FilterChip(
                    selected = selectedOption == option,
                    { selectedOption = option },
                    label = {
                        Text(option, style = MaterialTheme.typography.labelSmall)
                    },
                    modifier = Modifier
                        .width(207.dp)
                        .height(56.dp)
                        .padding(16.dp)

                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            Spacer(modifier = Modifier.height(58.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (displayMonth == 0) {
                            displayMonth = 11
                            displayYear--
                        } else {
                            displayMonth--
                        }
                    },
                    modifier = Modifier.size(48.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Month_ago"
                    )
                }

                Text(
                    text = "${MonthName(displayMonth + 1)} $displayYear",
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(
                    onClick = {
                        if (displayMonth == 11) {
                            displayMonth = 0
                            displayYear++
                        } else {
                            displayMonth++
                        }
                    },
                    modifier = Modifier.size(48.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Month_forward"
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            color = Color.Black
                        )
                    }
                }

            }
        }
    }
}
fun MonthName(currentMonth: Int): String {
    return when (currentMonth) {
        1 -> "январь"
        2 -> "февраль"
        3 -> "март"
        4 -> "апрель"
        5 -> "май"
        6 -> "июнь"
        7 -> "июль"
        8 -> "август"
        9 -> "сентябрь"
        10 -> "октябрь"
        11 -> "ноябрь"
        12 -> "декабрь"
        else -> ""
    }
}

@Preview(showBackground = true)
@Composable
fun TimeTablePreview() {
    _root_ide_package_.ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme {
        TimeTableMDScreen(
            onDayClick = {}
        )
    }
}

