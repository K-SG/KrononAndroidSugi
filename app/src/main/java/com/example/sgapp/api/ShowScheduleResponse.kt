package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

class ShowScheduleResponse (
        @SerializedName("success")
        val success: Boolean?,
        @SerializedName("code")
        val code: Int?,
        @SerializedName("data")
        var data: ShowSchedule? = null
)

class ShowSchedule(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("schedule_date")
        var schedule_date: String? = null
)

