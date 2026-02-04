package ru.sicampus.bootcamp2026.ui.theme.screens.Login

sealed class AuthIntent {
    data class Send(
        val login: String,
        val password: String
    ) : AuthIntent()
    data class Reg(
        val fullName: String,
        val jobTitle: String,
        val email:String,
        val password: String
    )
}