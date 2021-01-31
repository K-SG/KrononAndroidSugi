package com.example.sgapp.api

import com.example.retrofit2_kotlin.Retrofit2.KrononService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KrononClient {
    //API1こに対するクライアントを入れる

    fun retrofitBuilder(): KrononService {

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(KrononService::class.java)
    }

    companion object {
        var BaseUrl = "http://54.199.202.205/"
    }
}