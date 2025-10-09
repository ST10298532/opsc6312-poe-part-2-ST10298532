package com.example.eventstrack

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventstrack.api.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEventActivity : AppCompatActivity() {

    private lateinit var etEventName: EditText
    private lateinit var etDate: EditText
    private lateinit var etLocation: EditText
    private lateinit var etDescription: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        etEventName = findViewById(R.id.etEventName)
        etDate = findViewById(R.id.etDate)
        etLocation = findViewById(R.id.etLocation)
        etDescription = findViewById(R.id.etDescription)
        progressBar = findViewById(R.id.progressBarAddEvent)
        btnSubmit = findViewById(R.id.btnSubmitEvent)

        btnSubmit.setOnClickListener {
            createEvent()
        }
    }

    private fun createEvent() {
        val name = etEventName.text.toString().trim()
        val date = etDate.text.toString().trim()
        val location = etLocation.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty() || date.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        val eventApi = ApiClient.instance.create(EventApi::class.java)
        val eventRequest = EventRequest(name, date, location, description)

        eventApi.createEvent(eventRequest).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@AddEventActivity, "Event created successfully!", Toast.LENGTH_SHORT).show()
                    finish()  // Go back to previous screen
                } else {
                    Toast.makeText(this@AddEventActivity, "Failed to create event", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
