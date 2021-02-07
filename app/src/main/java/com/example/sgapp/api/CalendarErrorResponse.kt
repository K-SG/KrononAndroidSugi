package com.example.sgapp.api

import com.google.gson.annotations.SerializedName


class CalendarErrorResponse (
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?
)
