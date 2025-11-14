package com.example.eventstrack

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eventstrack.api.*
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.eventstrack.utils.LocaleHelper


class AddEventActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var etEventName: EditText
    private lateinit var etDate: EditText
    private lateinit var etLocation: EditText
    private lateinit var etDescription: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSubmit: Button
    private lateinit var btnSelectImage: Button

    // For Firebase image upload
    private lateinit var selectedImageUri: Uri
    private val PICK_IMAGE_REQUEST = 1  // Must be declared at class level



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("language", "en") ?: "en"
        LocaleHelper.setLocale(this, lang)


        setContentView(R.layout.activity_add_event)

        // Bind UI views
        etEventName = findViewById(R.id.etEventName)
        etDate = findViewById(R.id.etDate)
        etLocation = findViewById(R.id.etLocation)
        etDescription = findViewById(R.id.etDescription)
        progressBar = findViewById(R.id.progressBarAddEvent)
        btnSubmit = findViewById(R.id.btnSubmitEvent)
        btnSelectImage = findViewById(R.id.btnSelectImage)

        // 👉 Select image button
        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // 👉 Submit button (uploads image first → then event)
        btnSubmit.setOnClickListener {
            uploadImageAndCreateEvent()
        }
    }

    // Handle image picker result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == RESULT_OK &&
            data != null && data.data != null
        ) {
            selectedImageUri = data.data!!
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    // Upload image → then create event with image URL
    private fun uploadImageAndCreateEvent() {
        val title = etEventName.text.toString().trim()
        val date = etDate.text.toString().trim()
        val venue = etLocation.text.toString().trim()
        val description = etDescription.text.toString().trim()

        // Validate fields
        if (title.isEmpty() || date.isEmpty() || venue.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        // If no image selected → create event without image
        if (!::selectedImageUri.isInitialized) {
            createEvent(null)
            return
        }

        // Upload image to Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
            .child("events/${System.currentTimeMillis()}.jpg")

        storageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                // Fetch download URL after upload success
                storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    createEvent(imageUrl.toString())
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    // Create Event API call
    private fun createEvent(imageUrl: String?) {
        val title = etEventName.text.toString().trim()
        val date = etDate.text.toString().trim()
        val venue = etLocation.text.toString().trim()
        val description = etDescription.text.toString().trim()

        val eventApi = ApiClient.instance.create(EventApi::class.java)

        // Request body sent to backend
        val newEvent = EventRequest(
            title = title,
            description = description,
            category = "General",
            start_utc = date,
            end_utc = date,
            venue_name = venue,
            image_url = imageUrl  // ← includes Firebase image URL
        )

        eventApi.createEvent(newEvent)
            .enqueue(object : Callback<Event> {

                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        Toast.makeText(this@AddEventActivity, "Event created successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddEventActivity,
                            "Failed to create event (${response.code()})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@AddEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
