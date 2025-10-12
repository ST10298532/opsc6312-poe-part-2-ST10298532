package com.example.eventstrack.api

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val category: String? = null,
    val start_utc: String? = null,
    val end_utc: String? = null,
    val venue_name: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val image_url: String? = null
)
