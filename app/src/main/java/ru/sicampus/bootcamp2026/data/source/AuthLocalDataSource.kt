package ru.sicampus.bootcamp2026.data.source

import kotlin.io.encoding.Base64


object AuthLocalDataSource {

    var token: String? = null

    fun setToken(login: String, password: String): String {
        val credentials = "$login:$password"

        val base64String = Base64.encode(credentials.toByteArray())

        val fullToken = base64String

        token = fullToken
        return fullToken
    }

    fun clearToken() {
        token = null
    }
}