package com.example.eventstrack

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventstrack.api.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedEventsActivity : AppCompatActivity() {

    private lateinit var rvSaved: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_events)

        rvSaved = findViewById(R.id.rvSavedEvents)
        progressBar = findViewById(R.id.progressBarSaved)

        adapter = EventAdapter(emptyList(), this) { event, isSaved ->
            val prefs = getSharedPreferences("saved_events", MODE_PRIVATE)
            val saved = prefs.getStringSet("event_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            saved.remove(event.id.toString())
            prefs.edit().putStringSet("event_ids", saved).apply()

            Toast.makeText(this, "Removed from saved", Toast.LENGTH_SHORT).show()
            loadSavedEvents() // Refresh list
        }

        rvSaved.layoutManager = LinearLayoutManager(this)
        rvSaved.adapter = adapter

        loadSavedEvents()
    }

    private fun loadSavedEvents() {
        progressBar.visibility = View.VISIBLE

        // Load saved IDs from SharedPreferences
        val prefs = getSharedPreferences("saved_events", MODE_PRIVATE)
        val savedIds = prefs.getStringSet("event_ids", emptySet()) ?: emptySet()

        if (savedIds.isEmpty()) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "No saved events found", Toast.LENGTH_SHORT).show()
            return
        }

        val eventApi = ApiClient.instance.create(EventApi::class.java)
        eventApi.getAllEvents().enqueue(object : Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val allEvents = response.body()!!.events
                    val filtered = allEvents.filter { savedIds.contains(it.id.toString()) }
                    adapter.updateData(filtered)
                } else {
                    Toast.makeText(this@SavedEventsActivity, "Failed to load events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@SavedEventsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeSavedEvent(event: Event) {
        val prefs = getSharedPreferences("saved_events", MODE_PRIVATE)
        val saved = prefs.getStringSet("event_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        saved.remove(event.id.toString())
        prefs.edit().putStringSet("event_ids", saved).apply()

        Toast.makeText(this, "Removed from saved", Toast.LENGTH_SHORT).show()
        loadSavedEvents()
    }
}
