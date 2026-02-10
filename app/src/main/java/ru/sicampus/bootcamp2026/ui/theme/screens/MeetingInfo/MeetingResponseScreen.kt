package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.components.userList.UserList

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MeetingResponseScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
) {
    val viewModel: MeetingInfoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MeetingInfoViewModel(appViewModel) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()

    var currentUsers: List<UserEntity> = emptyList()
    var currentTitle = appViewModel.selectedMeetingName
    var currentDate = appViewModel.selectedMeetingDate
    var currentDescription = viewModel.meetingDescription.ifEmpty { "Загрузка описания..." }

    if (uiState is MeetingInfoState.Content) {
        val content = uiState as MeetingInfoState.Content
        currentUsers = content.users
        currentTitle = content.title
        currentDate = content.date
        currentDescription = viewModel.meetingDescription
    }

    MeetingResponseContent(
        title = currentTitle,
        date = currentDate,
        users = currentUsers,
        description = currentDescription,
        onAcceptClick = {
            viewModel.acceptInvitation(userPreferences)
        },
        onDeclineClick = {
            viewModel.declineInvitation(userPreferences)
        },
        onCloseClick = { appViewModel.NavigateTo(ViewModelState.Invitations) }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MeetingResponseContent(
    title: String,
    date: String,
    users: List<UserEntity>,
    description: String,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Surface,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp).fillMaxWidth(),
                    ) {
                        Text(
                            text = "Название",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 150.dp, bottom = 5.dp)
                        )
                        Text(
                            text = title,
                            style = Typography.bodyLarge
                        )

                        Text(
                            text = "Описание",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        Text(
                            text = description,
                            style = Typography.bodyLarge
                        )

                        Text(
                            text = "Дата и время",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        Text(
                            text = date,
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = "Список участников",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        UserList(
                            fios = users.map { it.fullName },
                            jobTitles = users.map { it.jobTitle },
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.meeting_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.meeting_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.align(Alignment.TopEnd).padding(7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.White,
                        contentDescription = "закрыть"
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
            ) {
                Button(
                    onClick = onAcceptClick,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f).padding(start = 40.dp, end = 12.dp)
                        .fillMaxWidth(),
                ) {
                    Text("Принять")
                }
                OutlinedButton(
                    onClick = onDeclineClick,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f).padding(start = 12.dp, end = 40.dp)
                        .fillMaxWidth(),
                    border = BorderStroke(2.dp, Blue)
                ) {
                    Text("Отклонить", color = Blue)
                }
            }
        }
    }
}