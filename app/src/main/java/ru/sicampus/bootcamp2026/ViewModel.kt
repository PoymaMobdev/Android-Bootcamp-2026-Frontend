package ru.sicampus.bootcamp2026

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModel: ViewModel() {
    private val _appState: MutableStateFlow<ViewModelState> = MutableStateFlow(ViewModelState.Profile)

    var appState = _appState.asStateFlow()

    init{
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _appState.emit(ViewModelState.Profile)

            delay(2000L)

            //_appState.emit(ViewModelState.Error("Error"))
        }
    }
}