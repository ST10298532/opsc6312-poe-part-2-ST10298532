package com.example.eventstrack.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\bH\'\u00a8\u0006\t"}, d2 = {"Lcom/example/eventstrack/api/AuthApi;", "", "login", "Lretrofit2/Call;", "Lcom/example/eventstrack/api/AuthResponse;", "request", "Lcom/example/eventstrack/api/LoginRequest;", "register", "Lcom/example/eventstrack/api/RegisterRequest;", "app_debug"})
public abstract interface AuthApi {
    
    @retrofit2.http.POST(value = "auth/register")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.eventstrack.api.AuthResponse> register(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.eventstrack.api.RegisterRequest request);
    
    @retrofit2.http.POST(value = "auth/login")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.eventstrack.api.AuthResponse> login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.eventstrack.api.LoginRequest request);
}