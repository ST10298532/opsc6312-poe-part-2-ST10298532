package com.example.eventstrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eventstrack.utils.LocaleHelper


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // 🔥 Load saved language BEFORE super.onCreate()
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("language", "en")
        LocaleHelper.setLocale(this, lang!!)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_activity)

        // Buttons to navigate to other pages
        val btnEvents = findViewById<Button>(R.id.btnEvents)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnSavedEvents = findViewById<Button>(R.id.btnSavedEvents)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnEvents.setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnSavedEvents.setOnClickListener {
            startActivity(Intent(this, SavedEventsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
