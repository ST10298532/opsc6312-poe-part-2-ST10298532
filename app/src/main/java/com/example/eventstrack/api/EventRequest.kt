package com.example.eventstrack.api

data class EventRequest(
    val title: String,
    val description: String,
    val category: String = "general",
    val start_utc: String,
    val end_utc: String,
    val venue_name: String,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val image_url: String? = null
)
