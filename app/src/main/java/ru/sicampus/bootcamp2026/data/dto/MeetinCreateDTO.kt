package ru.sicampus.bootcamp2026.data.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class MeetinCreateDTO(
    @SerialName("topic")
    val topic: String,
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("participantIds")
    val participantIds: List<Long?>

){
    @SuppressLint("NewApi")
    fun parseDateTime(): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }

    @SuppressLint("NewApi")
    fun getDateOnly(): String {
        return parseDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    @SuppressLint("NewApi")
    fun getTimeOnly(): String {
        return parseDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}