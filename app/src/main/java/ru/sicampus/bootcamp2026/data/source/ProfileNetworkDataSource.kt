package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO

class ProfileNetworkDataSource {
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


    suspend fun updateProfile(
        _userId: Int?,
        updateData: ProfileUpdateDTO,
        userPreferences: UserPreferences,
        currentPassword: String
    ): Boolean = withContext(Dispatchers.IO){
        runCatching {
            val token = AuthLocalDataSource.token?: error("Not authorized")

            val result = Network.client.put("${Network.HOST}/api/users/$_userId"){
                header(HttpHeaders.Authorization, "Basic $token")
                header(HttpHeaders.ContentType, "application/json")
                setBody(updateData)
            }
            if (result.status == HttpStatusCode.OK) {
                val newEmail = updateData.email ?: userPreferences.getUserEmail()

                if (newEmail != null) {
                    AuthLocalDataSource.setToken(newEmail, currentPassword)
                }
                return@withContext true
            }
            false
        }.getOrElse { false }
    }


}