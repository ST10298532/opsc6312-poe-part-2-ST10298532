package com.example.eventstrack.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Data models (for sending/receiving JSON)
data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val message: String, val token: String? = null)

// API Interface
interface AuthApi {
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>
}
