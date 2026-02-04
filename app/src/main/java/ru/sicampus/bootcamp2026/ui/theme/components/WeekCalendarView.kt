package ru.sicampus.bootcamp2026.ui.theme.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Surface
import java.time.DayOfWeek
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView() {
    val today = LocalDate.now()

    val weeks = getWeeksFromToday(today, 52)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { weeks.size }
    )

    var meetingNames: List<String> = listOf()
    var times: List<String> = listOf()

    //val interactionSource = remember { MutableInteractionSource() }

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
            modifier = Modifier.fillMaxWidth().background(Surface)
        )
        { page ->
            val weekDates = weeks[page]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                weekDates.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(CircleShape)
                            .clickable(
                            ) {

                                //TODO: заполнение meetingNames и datesAndTimes
                            },

                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(15.dp),
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
                Text("Назначить встречу",
                    color = MaterialTheme.colorScheme.onSurface)
            }
        }
        meetingNames = listOf("Название встречи", "Название встречи")
        times = listOf("9:00-10:00", "11:00-12:00")
        MeetingList(meetingNames, times)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun WeekCalendarViewPreview() {
    AndroidBootcamp2026FrontendTheme() {
        WeekView()
    }
}