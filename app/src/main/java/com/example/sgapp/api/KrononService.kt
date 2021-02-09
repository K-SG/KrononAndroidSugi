package com.example.retrofit2_kotlin.Retrofit2

import com.example.sgapp.api.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*

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

    @POST("api/schedules")
    fun createSchedule(@Body schedule: CreateSchedule,@Header("Authorization") token: String,@Header ("Accept") accept:String): Call<CreateScheduleResponse>;

    @DELETE("api/logout")
    fun logout(@Header("Authorization") token : String?): Call<LogoutUserResponse>

    @GET("api/show-schedules/search-by-day")
    fun showSchedules(@Query("date") date : String, @Header("Authorization") token : String?,@Header("Accept") accept :String): Call<ShowScheduleResponse>

    @GET("api/calendar")
    fun calendar(@Query("date") date : String, @Header("Authorization") token : String?,@Header("Accept") accept :String): Call<CalendarReaponse>

    @GET("api/schedules/{id}")
    fun detailSchedules(@Path("id") id : String, @Header("Authorization") token : String?, @Header("Accept") accept :String): Call<ScheduleDetailReaponse>

    @DELETE("api/schedules/{id}")
    fun deleteSchedules(@Path("id") id : String,@Header("Authorization") token : String?, @Header("Accept") accept :String): Call<ScheduleDeleteReaponse>


//    @GET("user")
//    fun getUser(@Header("Authorization") authorization: String): Call<User>

}