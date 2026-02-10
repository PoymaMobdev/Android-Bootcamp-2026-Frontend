package ru.sicampus.bootcamp2026.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

val CardBlue = Color(0xFF6A82FB)

@Composable
fun MeetingListItem(
    meetingName: String,
    dateAndTime: String,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 10.dp, top = 20.dp, bottom = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = meetingName,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = dateAndTime,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }

        IconButton(
            onClick = onInfoClick
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Информация",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun MeetingList(
    meetingNames: List<String>,
    datesAndTimes: List<String>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp, top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        itemsIndexed(meetingNames) { index, meetingName ->
            val time = datesAndTimes.getOrElse(index) { "" }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBlue,
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                MeetingListItem(
                    meetingName = meetingName,
                    dateAndTime = time,
                    onInfoClick = { onItemClick(meetingName) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMeetingList() {
    val meetingNames: List<String> = listOf("Планерка", "Ревью кода")
    val datesAndTimes: List<String> = listOf("08.02.2026 09:00", "08.02.2026 10:00")

    AndroidBootcamp2026FrontendTheme {
        MeetingList(meetingNames, datesAndTimes, onItemClick = {})
    }
}