package ru.sicampus.bootcamp2026.domain.reg

import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.RegisterResponse

class RegistrationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        fullName: String,
        jobTitle: String,
        email: String,
        password: String
    ): Result<Boolean> {
        val request = RegisterResponse(
            fullName = fullName,
            jobTitle = jobTitle,
            email = email,
            password = password
        )
        return authRepository.register(request)
    }
}

