package com.example.sgapp.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class CalendarReaponse (
        @SerializedName("success")
        val success: Boolean?,
        @SerializedName("code")
        val code: Int?,
        @SerializedName("data")
        var data: Array<CalendarSchedule>?
)

class CalendarSchedule(
        @SerializedName("date")
        var date: String? = null,
        @SerializedName("start_time")
        var start_time: String? = null,
        @SerializedName("title")
        var title: String? = null
)

class CalendarDate(
        @SerializedName("date")
        var date: String?
)

