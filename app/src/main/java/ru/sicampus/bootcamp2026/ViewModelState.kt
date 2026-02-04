package ru.sicampus.bootcamp2026

sealed class ViewModelState(val route: String) {
    data class  Error(val reason: String):ViewModelState("error")
    data object Loading: ViewModelState("loading")
    data object Login: ViewModelState("login")
    data object Invitations: ViewModelState("invitations")
    data object TimeTable: ViewModelState("timetable")
    data object Profile: ViewModelState("profile")
}