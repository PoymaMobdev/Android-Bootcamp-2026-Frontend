package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase
import ru.sicampus.bootcamp2026.ui.theme.components.userList.UserListState

class MeetingInfoViewModel(private val appViewModel: AppViewModel): ViewModel() {
    private val _uiState: MutableStateFlow<MeetingInfoState> = MutableStateFlow(MeetingInfoState.Loading)

    val uiState = _uiState.asStateFlow()
    val getUsersUseCase = GetUsersUseCase(UserRepository(UserInfoDataSource()))
    init {
        getData()
    }

    fun closeInfo() {
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }


    fun getData(){
        viewModelScope.launch {
            _uiState.emit(MeetingInfoState.Loading)
            getUsersUseCase.invoke(0).fold(
                onSuccess = {data ->
                    _uiState.emit(MeetingInfoState.Content(users = data))
                },
                onFailure = {error ->
                    _uiState.emit(MeetingInfoState.Error(error.message.orEmpty()))
                }

            )
        }
    }
}