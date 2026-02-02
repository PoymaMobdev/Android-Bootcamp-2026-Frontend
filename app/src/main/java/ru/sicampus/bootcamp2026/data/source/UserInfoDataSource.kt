package ru.sicampus.bootcamp2026.data.source

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body

class UserInfoDataSource {
    val userId = 1
    suspend fun getUser(): Result<ProfileUpdateDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get { "${Network.HOST}/api/users/$userId" }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body<ProfileUpdateDTO>()

        }
    }
}
