package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class ResponseInvitationDataSource {
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

    suspend fun sendResponse(invitationId: String, status: String, usersPreferences: UserPreferences):Boolean = withContext(Dispatchers.IO){
        val token = AuthLocalDataSource.token ?: return@withContext false
        val userId = loadAndReturnUserID(usersPreferences.getUserEmail())

        val result = Network.client.post("${Network.HOST}/api/invitations/$invitationId/respond") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            header(HttpHeaders.ContentType, "application/json")
            setBody(status)
        }

        result.status == HttpStatusCode.OK
    }
}