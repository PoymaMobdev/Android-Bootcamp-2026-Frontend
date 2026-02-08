package ru.sicampus.bootcamp2026.ui.theme.screens.Profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO
import ru.sicampus.bootcamp2026.data.source.JobDepNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.ProfileNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource

class ProfileViewModel(
    private val appViewModel: AppViewModel,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _jobOptions = MutableStateFlow<List<String>>(emptyList())
    val jobOptions = _jobOptions.asStateFlow()

    private val jobDataSource = JobDepNetworkDataSource()
    private val profileNetworkDataSource = ProfileNetworkDataSource()
    private val usersInfoDataSource = UsersInfoDataSource()

    private val defaultAvatarUrl = "http://171.22.31.205:8080/api/users/avatar"

    init {
        getData()
        loadJobTitles()
    }

    private fun loadJobTitles() {
        viewModelScope.launch {
            try {
                _jobOptions.value = jobDataSource.getJobTitles()
            } catch (e: Exception) {
                _jobOptions.value = emptyList()
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            val dataSource = UsersInfoDataSource()
            val myEmail = userPreferences.getUserEmail() ?: "мой@email.com"

            val result = dataSource.getUserByEmail(myEmail)
            val userDTO = result.getOrNull()
            println(userDTO?.avatarUrl)
            _uiState.emit(ProfileState.NoEdContent(
                fullName = userDTO?.fullName ?: "Имя не указано",
                jobTitle = userDTO?.jobTitle ?: "Без должности",
                email = userDTO?.email ?: myEmail,
                avatarUrl = userDTO?.avatarUrl ?: "None"
                ))


            delay(timeMillis = 2000L)

        }
    }

    fun switchToEditMode(fullName: String, jobTitle: String, email: String, password: String, avatarUrl: String) {
        _uiState.value = ProfileState.EdContent(
            fio = fullName,
            jobTitle = jobTitle,
            email = email,
            password = password,
            avatarUrl = avatarUrl
        )
    }

    fun saveChanges(fullName: String, jobTitle: String, email: String,  avatarUrl: String, currentPassword: String){
        viewModelScope.launch {
            _uiState.value = ProfileState.Loading

            val currentEmail = userPreferences.getUserEmail()

            val dataSource = UsersInfoDataSource()

            val result = dataSource.getUserByEmail(currentEmail)
            val userDTO = result.getOrNull()

            val updateData = ProfileUpdateDTO(
                fullName = fullName,
                jobTitle = jobTitle,
                email = email,
                avatarUrl = avatarUrl,
                currentPassword = currentPassword

            )

            val profileDataSource = ProfileNetworkDataSource()

            val success = profileDataSource.updateProfile(
                _userId = userDTO?.id,
                updateData = updateData,
                userPreferences,
                currentPassword
            )

            if (success) {
                _uiState.value = ProfileState.NoEdContent(fullName, jobTitle, email, avatarUrl)
                if (email != currentEmail) {
                    userPreferences.saveUserEmail(email)
                }
            } else {
                // Ошибка
            }

        }
    }

    fun cancelChanges() {
        getData()
    }

    fun toInvitations() {
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }

    fun toTimeTable() {
        appViewModel.NavigateTo(ViewModelState.TimeTable)
    }
}