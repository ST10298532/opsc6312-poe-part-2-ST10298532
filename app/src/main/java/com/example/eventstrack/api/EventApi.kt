package com.example.eventstrack.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val category: String?,
    val start_utc: String?,
    val end_utc: String?,
    val venue_name: String?,
    val lat: Double?,
    val lng: Double?,
    val image_url: String?
)

data class EventRequest(
    val name: String,
    val date: String,
    val location: String,
    val description: String
)

interface EventApi {
    @GET("events")
    fun getAllEvents(): Call<EventsResponse>

    @POST("events")
    fun createEvent(@Body event: EventRequest): Call<Event>
}


