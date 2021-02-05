package com.example.sgapp.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class ShowScheduleResponse (
        @SerializedName("success")
        val success: Boolean?,
        @SerializedName("code")
        val code: Int?,
        @SerializedName("data")
        var data: Array<ShowSchedule>?
)

class ShowSchedule(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("schedules")
        var schedules: Array<String>?
)

class ScheduleDate(
        @SerializedName("date")
        var date: String?
)

