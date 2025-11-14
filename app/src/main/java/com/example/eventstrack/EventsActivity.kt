package com.example.eventstrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventstrack.api.*
import com.example.eventstrack.utils.LocaleHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsActivity : AppCompatActivity() {

    private lateinit var rvEvents: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load selected language from preferences
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("language", "en")
        LocaleHelper.setLocale(this, lang!!)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        rvEvents = findViewById(R.id.rvEvents)
        progressBar = findViewById(R.id.progressBar)

        adapter = EventAdapter(emptyList(), this) { event, isSaved ->
            val savedPrefs = getSharedPreferences("saved_events", MODE_PRIVATE)
            val saved = savedPrefs.getStringSet("event_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            if (isSaved) {
                saved.remove(event.id.toString())
                Toast.makeText(this, "Event removed", Toast.LENGTH_SHORT).show()
            } else {
                saved.add(event.id.toString())
                Toast.makeText(this, "Event saved!", Toast.LENGTH_SHORT).show()
            }

            savedPrefs.edit().putStringSet("event_ids", saved).apply()
        }

        rvEvents.layoutManager = LinearLayoutManager(this)
        rvEvents.adapter = adapter

        fetchEvents()

        findViewById<Button>(R.id.btnSavedEvents).setOnClickListener {
            startActivity(Intent(this, SavedEventsActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logoutUser()
        }

        findViewById<Button>(R.id.btnAddEvent).setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }
    }

    private fun fetchEvents() {
        progressBar.visibility = View.VISIBLE

        val eventApi = ApiClient.instance.create(EventApi::class.java)
        eventApi.getAllEvents().enqueue(object : Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    adapter.updateData(response.body()!!.events)
                } else {
                    Toast.makeText(this@EventsActivity, "Failed to load events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@EventsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchEvents() // refresh every time user returns to this screen
    }

    private fun logoutUser() {
        val sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPrefs.edit().remove("jwt_token").apply()

        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun saveEventLocally(event: Event) {
        val prefs = getSharedPreferences("saved_events", MODE_PRIVATE)
        val saved = prefs.getStringSet("event_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        saved.add(event.id.toString())
        prefs.edit().putStringSet("event_ids", saved).apply()

        Toast.makeText(this, "Event saved!", Toast.LENGTH_SHORT).show()
    }
}
