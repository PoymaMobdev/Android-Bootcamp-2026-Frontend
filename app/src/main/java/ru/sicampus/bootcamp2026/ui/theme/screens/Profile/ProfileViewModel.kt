package ru.sicampus.bootcamp2026.ui.theme.screens.Profile

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import kotlin.String

class ProfileViewModel( private val appViewModel: AppViewModel): ViewModel() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.NoEdContent("","","",""))

    val uiState = _uiState.asStateFlow()

    init{
        getData()
    }
    fun switchToEditMode(fio: String, jobTitle: String, email: String, password: String) {
        _uiState.value = ProfileState.EdContent(fio, jobTitle, email, password)
    }

    fun cancelChanges(fio: String, jobTitle: String, email: String, photoUrl: String){
        _uiState.value = ProfileState.NoEdContent(fio, jobTitle, email, photoUrl)
    }

    fun saveChanges(fio: String, jobTitle: String, email: String, photoUrl: String){
        // реализовать отправку данных на сервер
        _uiState.value = ProfileState.NoEdContent(fio, jobTitle, email, photoUrl)
    }

    fun toInvitations() {
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }

    fun toTimeTable() {
        appViewModel.NavigateTo(ViewModelState.TimeTable)
    }

    fun getData() {
        viewModelScope.launch {
            val dataSource = UserInfoDataSource()
            val result = dataSource.getUser(page = 0, size = 10)
            val pagingData = result.getOrNull()
            val userDTO = pagingData?.content?.firstOrNull()

            _uiState.emit(ProfileState.NoEdContent(userDTO?.fullName ?: "Имя не указано", userDTO?.jobTitle ?: "Без должности", userDTO?.email ?: "Почта не указана", userDTO?.avatarUrl ?: "None"))

            delay(2000L)

        }
    }
}