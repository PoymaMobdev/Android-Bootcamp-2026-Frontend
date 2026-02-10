package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.components.userList.UserList

@Composable
fun MeetingInfoScreen(
    appViewModel: AppViewModel,
) {
    val viewModel: MeetingInfoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MeetingInfoViewModel(appViewModel) as T
            }
        }
    )
    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is MeetingInfoState.Error -> MeetingInfoError(currentState, onRefresh = {viewModel.getData()})
        is MeetingInfoState.Loading -> MeetingInfoLoading()
        is MeetingInfoState.Content -> MeetingInfoContent(
            users = currentState.users,
            meetingTitle = currentState.title,
            meetingDate = currentState.date,
            onClose = { viewModel.closeInfo() }
        )
    }
}

@Composable
private fun MeetingInfoError(
    state: MeetingInfoState.Error,
    onRefresh: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(onClick = onRefresh) {
                Text("refresh")
            }
        }
    }
}

@Composable
private fun MeetingInfoLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MeetingInfoContent(
    users: List<UserEntity>,
    meetingTitle: String,
    meetingDate: String,
    onClose: () -> Unit
){
    Scaffold{
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
                        text = meetingTitle,
                        style = Typography.bodyLarge
                    )
                    Text(
                        text = "Дата и время",
                        style = Typography.labelSmall,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                    )
                    Text(
                        text = meetingDate,
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
                        modifier = Modifier
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
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd).padding(7.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = Color.White,
                    contentDescription = "закрыть"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingInfoPreview() {
    val mockUsers = listOf(
        UserEntity(fullName = "Иван Иванов", jobTitle = "Android Developer", email = "ivan@example.com", avatarUrl = ""),
        UserEntity(fullName = "Мария Петрова", jobTitle = "Product Manager", email = "maria@example.com", avatarUrl = "")
    )

    AndroidBootcamp2026FrontendTheme {
        MeetingInfoContent(
            users = mockUsers,
            meetingTitle = "Дейли митинг",
            meetingDate = "08.02.2026 18:00",
            onClose = {}
        )
    }
}