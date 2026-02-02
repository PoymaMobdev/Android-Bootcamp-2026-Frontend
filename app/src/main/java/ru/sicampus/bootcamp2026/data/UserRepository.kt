package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(): UserEntity{
        val userDTO = userInfoDataSource.getUser().getOrThrow()

        val user =  UserEntity(
            fullName = userDTO.fullName ?: "",
            jobTitle = userDTO.jobTitle ?: "",
            email = userDTO.email ?: "",
            avatarUrl = userDTO.avatarUrl ?: ""
        )
        return user
    }
}



