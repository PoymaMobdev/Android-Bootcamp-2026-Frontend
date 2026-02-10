package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvitationResponseDTO(
    @SerialName("invitationId")
    val invitationId: String,
    @SerialName("topic")
    val topic: String,
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("organizerName")
    val organizerName: String
)

@Serializable
data class InvitationResponseBody(
    val status: String
)