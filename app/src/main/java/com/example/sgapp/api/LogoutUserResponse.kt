package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

class LogoutUserResponse (
    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("code")
    val code: Int?,

    @SerializedName("message")
    var message: String? = null
)

