package ru.sicampus.bootcamp2026

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences

class AppViewModel(
    private val userPreferences: UserPreferences
): ViewModel() {
    private val _appState: MutableStateFlow<ViewModelState> = MutableStateFlow(ViewModelState.Loading)
    var selectedInvitationId: String = ""
    var appState = _appState.asStateFlow()

    init{
        checkAuth()
    }

    fun NavigateTo(state: ViewModelState){
        _appState.value = state
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken()
            if (!token.isNullOrEmpty()) {
                AuthLocalDataSource.token = token
                _appState.value = ViewModelState.TimeTable
            } else {
                _appState.value = ViewModelState.Login
            }
        }
    }
}