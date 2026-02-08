package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.dto.DepartmentDTO
import ru.sicampus.bootcamp2026.data.dto.JobTitleDTO

class JobDepNetworkDataSource {
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


    suspend fun getJobTitles(
    ): List<String> = withContext(Dispatchers.IO)
    {

        val result = Network.client.get("${Network.HOST}/api/job-titles") {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Accept, "application/json")
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                val meetings = Json.decodeFromString<List<JobTitleDTO>>(result.bodyAsText())
                val titles = meetings.map { it.name }


                titles
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }


    suspend fun getDepartment(
    ): List<String> = withContext(Dispatchers.IO)
    {

        val result = Network.client.get("${Network.HOST}/api/departments") {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Accept, "application/json")
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                val meetings = Json.decodeFromString<List<DepartmentDTO>>(result.bodyAsText())
                val departments = meetings.map { it.name }
                departments

            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}