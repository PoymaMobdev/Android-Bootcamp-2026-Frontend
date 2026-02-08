package ru.sicampus.bootcamp2026.ui.theme.screens.Invitation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.dto.InvitationResponseDTO
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.source.InvitationNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingCreateNetDataSource
import ru.sicampus.bootcamp2026.data.source.ResponseInvitationDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileState


class InvitationViewModel(private val appViewModel: AppViewModel): ViewModel() {

    private val _uiState: MutableStateFlow<InvitationState> = MutableStateFlow(InvitationState.Meetings)

    val uiState = _uiState.asStateFlow()



    init {
        getData()
    }

    fun toProfile() {
        appViewModel.NavigateTo(ViewModelState.Profile)
    }

    fun toTimeTable() {
        appViewModel.NavigateTo(ViewModelState.TimeTable)
    }
    val data = ResponseInvitationDataSource()




    val meetingSource = MeetingCreateNetDataSource()
    private val _invitations = MutableStateFlow<List<InvitationResponseDTO>>(emptyList())
    val invitations: StateFlow<List<InvitationResponseDTO>> = _invitations.asStateFlow()

    fun loadInvitations(userPreferences: UserPreferences) {
        viewModelScope.launch {
            val data = InvitationNetworkDataSource()
            _invitations.value = data.getInvitationsFull(userPreferences)
        }
    }
    fun sendDeclinedResponse(invitationId: String, userPreferences: UserPreferences) {
        viewModelScope.launch {
            data.sendResponse(
                invitationId = invitationId,
                status = "DECLINED",
                usersPreferences = userPreferences
            )
        }
    }



    fun sendAcceptedResponse(invitationTopic: String, invitationDateTime: String, invitationId: String, userPreferences: UserPreferences) {
        viewModelScope.launch {
            data.sendResponse(
                invitationId = invitationId,
                status = "ACCEPTED",
                usersPreferences = userPreferences
            )

            deleteInvite(invitationId, userPreferences)



        }
    }

    val invitationsSource = InvitationNetworkDataSource()
    fun deleteInvite(invitationId: String, userPreferences: UserPreferences) {
        viewModelScope.launch {
            _invitations.update { currentList ->
                currentList.filter { it.invitationId != invitationId }
            }
        }

    }


    fun getData(){
        viewModelScope.launch {
            _uiState.emit(InvitationState.Meetings)
            delay(2000L)
        }
    }
}