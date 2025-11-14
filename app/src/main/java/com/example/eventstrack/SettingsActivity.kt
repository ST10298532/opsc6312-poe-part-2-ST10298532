package com.example.eventstrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eventstrack.utils.LocaleHelper

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadLanguage() // <-- MUST load saved language before setting UI

        setContentView(R.layout.activity_settings)

        val rbEnglish = findViewById<RadioButton>(R.id.rbEnglish)
        val rbZulu = findViewById<RadioButton>(R.id.rbZulu)
        val rgLanguage = findViewById<RadioGroup>(R.id.rgLanguage)

        // Load saved language
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val savedLang = prefs.getString("language", "en")

        if (savedLang == "zu") rbZulu.isChecked = true else rbEnglish.isChecked = true

        // Handle language change
        rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val newLang = if (checkedId == R.id.rbZulu) "zu" else "en"

            prefs.edit().putString("language", newLang).apply()

            LocaleHelper.setLocale(this, newLang)

            // Restart entire app to apply language across all screens
            restartApp()
        }
    }

    private fun loadLanguage() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("language", "en") ?: "en"
        LocaleHelper.setLocale(this, lang)
    }

    private fun restartApp() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
