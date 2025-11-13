package com.example.eventstrack.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventApi {
    @GET("events")
    fun getAllEvents(): Call<EventsResponse>

    @POST("events")
    fun createEvent(@Body event: EventRequest): Call<Event>
}
