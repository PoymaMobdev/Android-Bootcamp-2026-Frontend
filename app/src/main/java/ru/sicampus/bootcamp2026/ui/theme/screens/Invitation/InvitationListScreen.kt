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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
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
                @Suppress("UNCHECKED_CAST")
                return InvitationViewModel(appViewModel) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is InvitationState.Error -> InvitationListError(
            state = currentState,
            onRefresh = { viewModel.getData() }
        )
        is InvitationState.Loading -> InvitationListLoading()
        is InvitationState.Meetings -> {
            InvitationListContent(
                appViewModel = appViewModel,
                usersPreferences = usersPreferences
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InvitationListContent(
    appViewModel: AppViewModel,
    usersPreferences: UserPreferences
) {
    var meetingNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var times by remember { mutableStateOf<List<String>>(emptyList()) }
    var invitationsIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedItem by remember { mutableStateOf("Приглашения") }

    val dataSource = remember { InvitationNetworkDataSource() }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            val fullInvitations = dataSource.getInvitationsFull(usersPreferences)
            meetingNames = fullInvitations.map { it.topic }
            times = fullInvitations.map { it.dateTime }
            invitationsIds = fullInvitations.map { it.invitationId }
        } catch (e: Exception) {
            meetingNames = emptyList()
        } finally {
            isLoading = false
        }
    }

    InvitationScreenUI(
        isLoading = isLoading,
        hasInvitations = meetingNames.isNotEmpty(),
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                appViewModel = appViewModel
            )
        },
        content = {
            InvitationList(
                meetingNames = meetingNames,
                datesAndTimes = times,
                modifier = Modifier,
                appViewModel = appViewModel,
                invitationsIds = invitationsIds,
                userPreferences = usersPreferences
            )
        }
    )
}

@Composable
fun InvitationScreenUI(
    isLoading: Boolean,
    hasInvitations: Boolean,
    bottomBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.invitation_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.invitation_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
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
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Surface,
                ),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (!hasInvitations) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "У вас пока нет активных приглашений",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        content()
                    }
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
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefresh) {
                Text("Обновить")
            }
        }
    }
}

@Composable
fun InvitationListLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun InvitationScreenContentPreview() {
    AndroidBootcamp2026FrontendTheme {
        InvitationScreenUI(
            isLoading = false,
            hasInvitations = true,
            bottomBar = {
                Box(Modifier.height(56.dp).fillMaxWidth().padding(16.dp)) {
                    Text("Bottom Navigation Bar", Modifier.align(Alignment.Center))
                }
            },
            content = {
                Column(Modifier.padding(16.dp)) {
                    repeat(3) {
                        Text("Приглашение на встречу #$it", Modifier.padding(8.dp))
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InvitationScreenEmptyPreview() {
    AndroidBootcamp2026FrontendTheme {
        InvitationScreenUI(
            isLoading = false,
            hasInvitations = false,
            bottomBar = {},
            content = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InvitationScreenLoadingPreview() {
    AndroidBootcamp2026FrontendTheme {
        InvitationScreenUI(
            isLoading = true,
            hasInvitations = false,
            bottomBar = {},
            content = {}
        )
    }
}