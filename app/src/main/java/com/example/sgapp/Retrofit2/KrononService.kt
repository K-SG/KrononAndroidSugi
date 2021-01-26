package com.example.retrofit2_kotlin.Retrofit2

import com.example.sgapp.Retrofit2.CreateUser
import com.example.sgapp.Retrofit2.CreateUserResponse
import com.example.sgapp.Retrofit2.LoginUser
import com.example.sgapp.Retrofit2.LoginUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface KrononService{
//    @GET("data/2.5/weather?")
    @GET("api/test_get2?")
//    fun getCurrentWeatherData(@Query("lat") lat : String, @Query("lon") lon : String, @Query("APPID") APPID : String): Call<WeatherResponse>
    fun getCurrentWeatherData(@Query("message") lat:String): Call<CreateUserResponse>
    @POST("api/users")
    fun createUser(@Body user: CreateUser): Call<CreateUserResponse>;
//    fun createUser(@Query("name") name:String,@Query("email") email:String,@Query("password") password:String): Call<KrononRespose>;
    @POST("api/login")
    fun login(@Body user: LoginUser): Call<LoginUserResponse>;
}