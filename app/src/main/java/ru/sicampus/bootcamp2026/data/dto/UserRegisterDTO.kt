package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName

data class UserRegisterDTO(
    @SerialName("fullName")
    val fullName: String,
    @SerialName("jobTitle")
    val jobTitle: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)