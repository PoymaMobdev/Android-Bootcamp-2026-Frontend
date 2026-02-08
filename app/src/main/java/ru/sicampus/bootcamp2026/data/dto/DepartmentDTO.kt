package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String
)