package ru.sicampus.bootcamp2026.domain.auth

import android.provider.ContactsContract
import ru.sicampus.bootcamp2026.data.AuthRepository

class CheckAndSaveAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ): Result<Boolean> {
        return runCatching {
            val isLogin = authRepository.checkAndAuth(login, password)
            if (!isLogin) {
                throw Exception("Login or pass incorrect")
            }
            isLogin
        }
    }
}

