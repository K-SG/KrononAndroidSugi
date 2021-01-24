package com.example.retrofit2_kotlin.Retrofit2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface KrononService{
//    @GET("data/2.5/weather?")
    @GET("api/test_get2?")
//    fun getCurrentWeatherData(@Query("lat") lat : String, @Query("lon") lon : String, @Query("APPID") APPID : String): Call<WeatherResponse>
    fun getCurrentWeatherData(@Query("message") lat:String): Call<KrononRespose>
    @POST("api/users")
//    fun createUser(@Body name: String,@Body email: String,@Body password:String): Call<WeatherResponse>;
    fun createUser(@Query("name") name:String,@Query("email") email:String,@Query("password") password:String): Call<KrononRespose>;

}