package ru.sicampus.bootcamp2026.ui.theme.screens.Login

import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Content)

    val uiState = _uiState.asStateFlow()

    init{
        getData()
    }

    fun getData() {
        viewModelScope.launch{

            delay(2000L)

        }
    }

    fun onRegistrationClick(){
        viewModelScope.launch {
            _uiState.emit(LoginState.Reg)
        }
    }

    fun onLoginClick(){
        //прописать что если нет временного токена - переключение на контен
        viewModelScope.launch {
            _uiState.emit(LoginState.Content)
        }

        //если есть - переключение на список приглашений
    }

}