package com.example.eventstrack.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.88.231:4000/"
    // ⚠️ Use 10.0.2.2 instead of localhost — it points to your PC when running on an Android emulator.

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
