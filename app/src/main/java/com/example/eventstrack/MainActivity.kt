package com.example.eventstrack

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eventstrack.api.*
import com.example.eventstrack.utils.LocaleHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load saved language preference
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("language", "en")
        LocaleHelper.setLocale(this, lang!!)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an instance of our API
        val authApi = ApiClient.instance.create(AuthApi::class.java)

        // Example login details (make sure this user exists in your backend)
        val loginRequest = LoginRequest(
            email = "testuser@example.com",
            password = "password123"
        )

        // Call the login endpoint
        authApi.login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    Log.d("API_SUCCESS", "Login successful: ${response.body()?.token}")
                } else {
                    Log.e("API_ERROR", "Login failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Error: ${t.message}")
            }
        })
    }
}
