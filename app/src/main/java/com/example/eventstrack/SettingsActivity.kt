package com.example.eventstrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.eventstrack.utils.PrefKeys
import com.example.eventstrack.utils.Prefs
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // ----- LANGUAGE -----
        val rbEn = findViewById<RadioButton>(R.id.rbEnglish)
        val rbZu = findViewById<RadioButton>(R.id.rbZulu)

        val savedLang = Prefs.getString(this, PrefKeys.LANGUAGE, "en")
        if (savedLang == "zu") rbZu.isChecked = true else rbEn.isChecked = true

        rbEn.setOnClickListener { applyLanguage("en") }
        rbZu.setOnClickListener { applyLanguage("zu") }

        // ----- NOTIFICATIONS -----
        val switchNot = findViewById<Switch>(R.id.switchNotifications)
        val leadEt = findViewById<EditText>(R.id.etLeadHours)

        switchNot.isChecked = Prefs.getBool(this, PrefKeys.NOTIFICATIONS, true)
        leadEt.setText(Prefs.getInt(this, PrefKeys.NOTIF_LEAD_HOURS, 2).toString())

        switchNot.setOnCheckedChangeListener { _, isChecked ->
            Prefs.putBool(this, PrefKeys.NOTIFICATIONS, isChecked)
            if (!isChecked) {
                // cancelAllScheduledNotifications() // optional if you add WorkManager
            }
        }

        findViewById<Button>(R.id.btnSaveSettings).setOnClickListener {
            val lead = leadEt.text.toString().toIntOrNull() ?: 2
            Prefs.putInt(this, PrefKeys.NOTIF_LEAD_HOURS, lead)
            if (Prefs.getBool(this, PrefKeys.NOTIFICATIONS, true)) {
                // scheduleNotificationsForSavedEvents(this) // optional future feature
            }
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }

        // ----- DISCOVERY RADIUS -----
        val radiusEt = findViewById<EditText>(R.id.etRadius)
        radiusEt.setText(Prefs.getInt(this, PrefKeys.DISCOVERY_RADIUS, 20).toString())

        findViewById<Button>(R.id.btnSaveRadius).setOnClickListener {
            val r = radiusEt.text.toString().toIntOrNull() ?: 20
            Prefs.putInt(this, PrefKeys.DISCOVERY_RADIUS, r)
            Toast.makeText(this, "Radius saved", Toast.LENGTH_SHORT).show()
        }

        // ----- THEME -----
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)

        val theme = Prefs.getString(this, PrefKeys.THEME, "light")
        if (theme == "dark") rbDark.isChecked = true else rbLight.isChecked = true

        rbLight.setOnClickListener { applyTheme("light") }
        rbDark.setOnClickListener { applyTheme("dark") }

        // ----- SIGN OUT -----
        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            Prefs.clearToken(this)
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    // ====== LANGUAGE FUNCTION ======
    private fun applyLanguage(lang: String) {
        Prefs.putString(this, PrefKeys.LANGUAGE, lang)
        val locale = Locale(if (lang == "zu") "zu" else "en")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate() // refresh UI strings
    }

    // ====== THEME FUNCTION ======
    private fun applyTheme(theme: String) {
        Prefs.putString(this, PrefKeys.THEME, theme)
        AppCompatDelegate.setDefaultNightMode(
            if (theme == "dark") AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
