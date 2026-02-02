package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import kotlinx.coroutines.flow.MutableStateFlow
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface MeetingInfoState {
    data class Error(val reason: String) : MeetingInfoState
    data object Loading : MeetingInfoState
    data class Content(
        val name: String,
        val description: String,
        val date: String,
        val place: String,
        val UsersList: List<UserEntity>
    ): MeetingInfoState
}