package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.InvitationNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class MeetingInfoViewModel(private val appViewModel: AppViewModel): ViewModel() {

    private val _uiState: MutableStateFlow<MeetingInfoState> = MutableStateFlow(MeetingInfoState.Loading)
    val uiState = _uiState.asStateFlow()

    private val meetingDataSource = MeetingNetworkDataSource()
    private val invitationDataSource = InvitationNetworkDataSource()
    var meetingDescription: String = ""
    init {
        getData()
    }

    fun closeInfo() {
        appViewModel.NavigateTo(ViewModelState.TimeTable)
    }

    @SuppressLint("NewApi")
    fun convertToISO(dateStr: String, timeStr: String): String? {
        return try{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val localDateTime = LocalDateTime.parse("$dateStr $timeStr", formatter)
            localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".000Z"
        }
        catch (e: Exception) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(MeetingInfoState.Loading)
            try {
                val meetingId = appViewModel.selectedMeetingId
                if (meetingId.isBlank()) return@launch

                val details = meetingDataSource.getMeetingDetails(meetingId)

                meetingDescription = details.description ?: "Описание отсутствует"

                val userEntities = details.users.map { participant ->
                    val job = participant.jobTitle?.titleName ?: participant.status ?: "Участник"
                    UserEntity(
                        fullName = participant.fullName,
                        jobTitle = job,
                        email = "",
                        avatarUrl = ""
                    )
                }

                val formattedDate = try {
                    val parsedDate = LocalDateTime.parse(details.date)
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    parsedDate.format(formatter)
                } catch (e: Exception) { details.date }

                _uiState.emit(
                    MeetingInfoState.Content(
                        users = userEntities,
                        title = details.topic,
                        date = formattedDate
                    )
                )
            } catch (e: Exception) {
                _uiState.emit(MeetingInfoState.Error(e.message ?: "Error"))
            }
        }
    }

    fun acceptInvitation(userPreferences: UserPreferences) {
        viewModelScope.launch {
            try {
                val meetingId = appViewModel.selectedMeetingId
                invitationDataSource.sendResponse(meetingId, "ACCEPTED", userPreferences)

                appViewModel.NavigateTo(ViewModelState.Invitations)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun declineInvitation(userPreferences: UserPreferences) {
        viewModelScope.launch {
            try {
                val meetingId = appViewModel.selectedMeetingId
                invitationDataSource.sendResponse(meetingId, "DECLINED", userPreferences)

                appViewModel.NavigateTo(ViewModelState.Invitations)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}