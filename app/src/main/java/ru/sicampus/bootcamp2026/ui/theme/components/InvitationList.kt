package ru.sicampus.bootcamp2026.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.Typography

@Composable
fun InvitationListItem(
    meetingName: String,
    dateAndTime: String,
    appViewModel: AppViewModel,
    invitationId: String
) {

    InvitationListItemContent(
        meetingName = meetingName,
        dateAndTime = dateAndTime,
        onClick = {
            appViewModel.openMeetingResponse(invitationId, meetingName, dateAndTime)
        }
    )
}

@Composable
fun InvitationListItemContent(
    meetingName: String,
    dateAndTime: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_avatar),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(39.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = meetingName,
                style = Typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateAndTime,
                style = Typography.labelSmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Рассмотреть запрос"
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun InvitationList(
    meetingNames: List<String>,
    datesAndTimes: List<String>,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    invitationsIds: List<String>,
    userPreferences: UserPreferences
) {
    LazyColumn(modifier = modifier.widthIn(min = 412.dp).heightIn(min = 445.dp)) {
        itemsIndexed(meetingNames) { index, meetingName ->
            val dateAndTime = if (index < datesAndTimes.size) datesAndTimes[index] else "Время не указано"
            val invitationId = if (index < invitationsIds.size) invitationsIds[index] else ""

            InvitationListItem(
                meetingName = meetingName,
                dateAndTime = dateAndTime,
                appViewModel = appViewModel,
                invitationId = invitationId
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@Preview(showBackground = true)
@Composable
fun InvitationListItemPreview() {
    InvitationListItemContent(
        meetingName = "Обсуждение проекта Android",
        dateAndTime = "12 Октября, 14:00",
        onClick = {}
    )
}