package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MeetingInfoViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<MeetingInfoState> = MutableStateFlow(MeetingInfoState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            _uiState.emit(MeetingInfoState.Loading)

            delay(2000L)

            _uiState.emit(MeetingInfoState.Error("Error"))
        }
    }
}