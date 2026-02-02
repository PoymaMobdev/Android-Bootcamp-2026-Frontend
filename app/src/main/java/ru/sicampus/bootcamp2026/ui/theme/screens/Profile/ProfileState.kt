package ru.sicampus.bootcamp2026.ui.theme.screens.Profile

sealed interface ProfileState {
    data class Error(val reason: String): ProfileState
    data object Loading:ProfileState
    data class NoEdContent(
        val fio: String,
        val jobTitle: String,
        val email: String,
        val photoUrl: String
    ): ProfileState

    data class EdContent(
        var fio: String,
        var jobTitle: String,
        var email: String,
        var password: String
    ):ProfileState
}