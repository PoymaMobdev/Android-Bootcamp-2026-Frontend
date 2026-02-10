package ru.sicampus.bootcamp2026.ui.theme.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.ScheduleNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.components.MeetingList
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
) {
    val today = LocalDate.now()
    val weeks = remember { getWeeksFromToday(today, 52) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    var scheduleItems by remember { mutableStateOf<List<ScheduleNetworkDataSource.ScheduleItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        val data = ScheduleNetworkDataSource()
        scheduleItems = data.getSchedule(userPreferences)
    }

    val filteredItems = scheduleItems.filter { item ->
        try {
            val itemDateTime = LocalDateTime.parse(item.dateTime)
            itemDateTime.toLocalDate() == selectedDate
        } catch (e: Exception) {
            false
        }
    }

    val displayNames = filteredItems.map { it.topic }
    val displayTimes = filteredItems.map { item ->
        try {
            val startTime = LocalDateTime.parse(item.dateTime)
            val endTime = startTime.plusHours(1)
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            "${startTime.format(formatter)} - ${endTime.format(formatter)}"
        } catch (e: Exception) {
            item.dateTime
        }
    }

    WeekViewContent(
        weeks = weeks,
        selectedDate = selectedDate,
        meetingNames = displayNames,
        times = displayTimes,
        onAddMeetingClick = { appViewModel.NavigateTo(ViewModelState.CreateMeeting) },
        onDateSelected = { newDate -> selectedDate = newDate },
        onMeetingClick = { name ->
            val item = scheduleItems.find { it.topic == name }

            if (item != null) {
                val realId = item.id.toString()
                val dateStr = try {
                    val startTime = LocalDateTime.parse(item.dateTime)
                    val endTime = startTime.plusHours(1)
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    "${startTime.format(formatter)} - ${endTime.format(formatter)}"
                } catch (e: Exception) { item.dateTime }

                appViewModel.openMeetingInfo(realId, item.topic, dateStr)
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekViewContent(
    weeks: List<List<LocalDate>>,
    selectedDate: LocalDate,
    meetingNames: List<String>,
    times: List<String>,
    onAddMeetingClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onMeetingClick: (String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { weeks.size }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 8.dp),
        ) {
            listOf("вс", "пн", "вт", "ср", "чт", "пт", "сб").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
        ) { page ->
            val weekDates = weeks[page]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                weekDates.forEach { date ->
                    val isSelected = date == selectedDate

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable {
                                onDateSelected(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            textAlign = TextAlign.Center,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Button(
            onClick = onAddMeetingClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Surface,
            ),
            shape = RoundedCornerShape(20.dp),
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Назначить встречу",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    "Назначить встречу",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (meetingNames.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "На этот день встреч нет",
                    color = Color.Gray
                )
            }
        } else {
            MeetingList(
                meetingNames = meetingNames,
                datesAndTimes = times,
                onItemClick = onMeetingClick
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getWeeksFromToday(today: LocalDate, weeksCount: Int): List<List<LocalDate>> {
    val weeks = mutableListOf<List<LocalDate>>()
    var currentStartOfWeek = today
    while (currentStartOfWeek.dayOfWeek != DayOfWeek.SUNDAY) {
        currentStartOfWeek = currentStartOfWeek.minusDays(1)
    }
    repeat(weeksCount) {
        val week = (0 until 7).map { currentStartOfWeek.plusDays(it.toLong()) }
        weeks.add(week)
        currentStartOfWeek = currentStartOfWeek.plusWeeks(1)
    }
    return weeks
}

@SuppressLint("NewApi")
@Preview(showBackground = true, name = "Расписание (Неделя)")
@Composable
fun WeekViewPreview() {
    val today = LocalDate.now()
    val mockWeeks = getWeeksFromToday(today, 1)
    val selectedDate = today

    val mockNames = listOf("Daily Standup")
    val mockTimes = listOf("10:00 - 11:00")

    AndroidBootcamp2026FrontendTheme {
        WeekViewContent(
            weeks = mockWeeks,
            selectedDate = selectedDate,
            meetingNames = mockNames,
            times = mockTimes,
            onAddMeetingClick = {},
            onDateSelected = {},
            onMeetingClick = {}
        )
    }
}