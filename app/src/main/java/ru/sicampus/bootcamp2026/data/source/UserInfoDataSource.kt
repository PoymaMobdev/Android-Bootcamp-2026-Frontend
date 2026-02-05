package ru.sicampus.bootcamp2026.data.source

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import ru.sicampus.bootcamp2026.data.dto.PagingUserListDTO

class UserInfoDataSource {
    val userId = 1
    suspend fun getUser(
        page:Int,
        size: Int
    ): Result<PagingUserListDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val token = AuthLocalDataSource.token
            val result = Network.client.get("${Network.HOST}/api/users/paginated"){
                url{
                    parameter("page", page)
                    parameter("size", size)
                }
                header(HttpHeaders.Authorization, token)
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()

        }
    }
}
