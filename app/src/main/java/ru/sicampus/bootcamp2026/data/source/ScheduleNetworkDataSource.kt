package ru.sicampus.bootcamp2026.data.source

import android.annotation.SuppressLint
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.dto.ScheduleEntryDTO
import java.time.LocalDate

class ScheduleNetworkDataSource {
    val _userId = MutableStateFlow<Long?>(null)
    val data = UsersInfoDataSource()

    suspend fun loadUserIDByEmail(email: String?) {
        data.getUserByEmail(email).onSuccess { user ->
            _userId.value = user.id.toLong()
        }.onFailure {
            _userId.value = 1L
        }
    }

    @SuppressLint("NewApi")
    suspend fun createMeeting(
        schedule: ScheduleEntryDTO
    ): Boolean = withContext(Dispatchers.IO) {
        val token = AuthLocalDataSource.token ?: error("Not authorized")

        val userId = _userId.value ?: 1
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(7)

        val result = Network.client.get("${Network.HOST}/api/schedule") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId)
            header("startDate", startDate)
            header("endDate",endDate)
        }
        result.status == HttpStatusCode.OK
    }
}
