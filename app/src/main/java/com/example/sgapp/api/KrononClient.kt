package com.example.sgapp.api

import com.example.sgapp.NewUserCreateActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KrononClient {
    //API1こに対するクライアントを入れる
    val retrofit = Retrofit.Builder()
        .baseUrl(NewUserCreateActivity.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    companion object {
        var BaseUrl = "http://54.199.202.205/"
    }
}