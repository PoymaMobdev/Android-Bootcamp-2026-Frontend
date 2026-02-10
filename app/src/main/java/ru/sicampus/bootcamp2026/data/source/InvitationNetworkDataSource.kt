package ru.sicampus.bootcamp2026.data.source

import android.annotation.SuppressLint
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.dto.InvitationResponseBody
import ru.sicampus.bootcamp2026.data.dto.InvitationResponseDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InvitationNetworkDataSource {

    private val _userId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val data = UsersInfoDataSource()

    suspend fun loadAndReturnUserID(email: String?): Long {
        return data.getUserByEmail(email)
            .fold(
                onSuccess = { user ->
                    _userId.value = user.id.toLong()
                    user.id.toLong()
                },
                onFailure = {
                    _userId.value = 1L
                    1L
                }
            )
    }


    suspend fun getInvitationsTitles(
        usersPreferences: UserPreferences
    ): List<String> = withContext(Dispatchers.IO){
        val token = AuthLocalDataSource.token ?: error("Not authorized")

        val userId = loadAndReturnUserID(usersPreferences.getUserEmail())

        val result = Network.client.get("${Network.HOST}/api/invitations") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            header(HttpHeaders.ContentType, "application/json")
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                val meetings = Json.decodeFromString<List<InvitationResponseDTO>>(result.bodyAsText())
                meetings.map { it.topic }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
    suspend fun sendResponse(
        invitationId: String,
        status: String,
        userPreferences: UserPreferences
    ): Boolean = withContext(Dispatchers.IO) {
        val token = AuthLocalDataSource.token ?: return@withContext false

        val userInfoDataSource = UsersInfoDataSource()
        val userId = userInfoDataSource.getUserByEmail(userPreferences.getUserEmail())
            .getOrNull()?.id ?: return@withContext false

        try {
            val response = Network.client.post("${Network.HOST}/api/invitations/$invitationId/respond") {
                header(HttpHeaders.Authorization, "Basic $token")
                header("X-User-Id", userId)
                contentType(ContentType.Application.Json)
                setBody(InvitationResponseBody(status = status))
            }

            return@withContext response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }

    @SuppressLint("NewApi")
    suspend fun getInvitationsDTs(
        usersPreferences: UserPreferences
    ): List<String> = withContext(Dispatchers.IO){
        val token = AuthLocalDataSource.token ?: error("Not authorized")

        val userId = loadAndReturnUserID(usersPreferences.getUserEmail())

        val result = Network.client.get("${Network.HOST}/api/invitations") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            header(HttpHeaders.ContentType, "application/json")
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                val invitations = Json.decodeFromString<List<InvitationResponseDTO>>(result.bodyAsText())
                invitations.map { invitation ->
                    val startTime = LocalDateTime.parse(invitation.dateTime)
                    val endTime = startTime.plusHours(1)

                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    val startFormatted = startTime.format(formatter)
                    val endFormatted = endTime.format(formatter)

                    "$startFormatted - $endFormatted"
                }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }


    @SuppressLint("NewApi")
    suspend fun getInvitationsID(
        usersPreferences: UserPreferences
    ): List<String> = withContext(Dispatchers.IO){
        val token = AuthLocalDataSource.token ?: error("Not authorized")

        val userId = loadAndReturnUserID(usersPreferences.getUserEmail())

        val result = Network.client.get("${Network.HOST}/api/invitations") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            header(HttpHeaders.ContentType, "application/json")
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                val invitations = Json.decodeFromString<List<InvitationResponseDTO>>(result.bodyAsText())
                invitations.map { it.invitationId }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }


    suspend fun getInvitationsFull(
        usersPreferences: UserPreferences
    ): List<InvitationResponseDTO> = withContext(Dispatchers.IO) {
        val token = AuthLocalDataSource.token ?: error("Not authorized")
        val userId = loadAndReturnUserID(usersPreferences.getUserEmail())

        val result = Network.client.get("${Network.HOST}/api/invitations") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            header(HttpHeaders.ContentType, "application/json")
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                Json.decodeFromString<List<InvitationResponseDTO>>(result.bodyAsText())
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}