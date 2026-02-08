package ru.sicampus.bootcamp2026.ui.theme.screens.Invitation


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.InvitationResponseDTO
import ru.sicampus.bootcamp2026.data.source.InvitationNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.components.BottomNavBar
import ru.sicampus.bootcamp2026.ui.theme.components.InvitationList


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InvitationListScreen(
    appViewModel: AppViewModel,
    usersPreferences: UserPreferences
) {
    val viewModel: InvitationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InvitationViewModel(appViewModel) as T
            }
        }
    )
    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is InvitationState.Error -> InvitationListError(currentState,  onRefresh = {viewModel.getData()})
        is InvitationState.Loading -> InbitationListLoading()
        is InvitationState.Meetings -> InvitationListContent(appViewModel, toProfile = {viewModel.toProfile()}, toTimeTable = {viewModel.toTimeTable()},usersPreferences, viewModel)
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun InvitationListContent(
    appViewModel: AppViewModel,
    toProfile: () -> Unit,
    toTimeTable: () -> Unit,
    usersPreferences: UserPreferences,
    viewModel: InvitationViewModel
) {
    val data = InvitationNetworkDataSource()

    var meetingNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var times by remember { mutableStateOf<List<String>>(emptyList()) }
    var invitationsIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var fullInvitations by remember { mutableStateOf<List<InvitationResponseDTO>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedInvitation by remember { mutableStateOf<InvitationResponseDTO?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            fullInvitations = data.getInvitationsFull(usersPreferences)
            meetingNames = fullInvitations.map { it.topic }
            times = fullInvitations.map { it.dateTime }
            invitationsIds = fullInvitations.map { it.invitationId }

            if (fullInvitations.isNotEmpty()) {
                selectedInvitation = fullInvitations[0]
                showDialog = true
            }
        } catch (e: Exception) {
            fullInvitations = emptyList()
        } finally {
            isLoading = false
        }
    }

    var selectedItem by remember { mutableStateOf("Приглашения") }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                appViewModel = appViewModel
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.invitation_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.invitation_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Приглашения",
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.Center),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Card(
                modifier = Modifier
                    .widthIn(min = 412.dp)
                    .heightIn(min = 445.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Surface,
                ),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    InvitationList(
                        meetingNames = meetingNames,
                        datesAndTimes = times,
                        modifier = Modifier,
                        appViewModel = appViewModel,
                        invitationsIds = invitationsIds,
                        usersPreferences
                    )
                }
            }
        }
    }
}


@Composable
fun InvitationListError(
    state: InvitationState.Error,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ) {
                Text("refresh")
            }
        }
    }
}


@Composable
fun InbitationListLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview
@Composable
fun InvitationListScreenPreview(
    appViewModel: AppViewModel,
    usersPreferences: UserPreferences
) {
    AndroidBootcamp2026FrontendTheme() {
        InvitationListScreen(appViewModel, usersPreferences)
    }
}