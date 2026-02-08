package ru.sicampus.bootcamp2026.ui.theme.components


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.InvitationNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.ResponseInvitationDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.screens.Invitation.InvitationViewModel
import kotlin.String




@Composable
fun InvitationListItem(
    meetingName: String,
    dateAndTime: String,
    appViewModel: AppViewModel,
    invitationId: String,
    usersPreferences: UserPreferences
) {
    val viewModel: InvitationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InvitationViewModel(appViewModel) as T
            }
        }
    )
    var showDialog by remember { mutableStateOf(false) }
    val data = ResponseInvitationDataSource()

    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            onClick = {  showDialog = true  },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Рассмотреть запрос"
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Ответить на приглашение",
                    style = Typography.headlineSmall
                )
            },
            text = {
                Column {
                    Text("Тема: $meetingName", style = Typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Время: $dateAndTime", style = Typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Вы хотите принять это приглашение?", style = Typography.bodyMedium)
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.sendAcceptedResponse(meetingName, dateAndTime, invitationId, usersPreferences )
                            showDialog = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Принять")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            viewModel.sendDeclinedResponse(invitationId, usersPreferences)
                            showDialog = false
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        )
                    ) {
                        Text("Отклонить")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отмена")
                }
            }
        )


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
                val dateAndTime = if (index < datesAndTimes.size) {
                    datesAndTimes[index]
                } else {
                    "Время не указано"
                }
                val invitationId = if (index < invitationsIds.size) {
                    invitationsIds[index]
                } else {
                    ""
                }

                InvitationListItem(
                    meetingName = meetingName,
                    dateAndTime = dateAndTime,
                    appViewModel = appViewModel,
                    invitationId = invitationId,
                    userPreferences
                )

            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }


