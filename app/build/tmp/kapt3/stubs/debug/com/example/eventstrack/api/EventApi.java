package com.example.eventstrack.api;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\'J\u0018\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0001\u0010\u0007\u001a\u00020\bH\'\u00a8\u0006\t\u00c0\u0006\u0003"}, d2 = {"Lcom/example/eventstrack/api/EventApi;", "", "getAllEvents", "Lretrofit2/Call;", "Lcom/example/eventstrack/api/EventsResponse;", "createEvent", "Lcom/example/eventstrack/api/Event;", "event", "Lcom/example/eventstrack/api/EventRequest;", "app_debug"})
public abstract interface EventApi {
    
    @retrofit2.http.GET(value = "events")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.eventstrack.api.EventsResponse> getAllEvents();
    
    @retrofit2.http.POST(value = "events")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.eventstrack.api.Event> createEvent(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.eventstrack.api.EventRequest event);
}