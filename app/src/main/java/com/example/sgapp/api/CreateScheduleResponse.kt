package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

class CreateScheduleResponse (
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var data: CreateScheduleData?
)
class CreateSchedule(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("schedule_date")
    var schedule_date: String? = null,
    @SerializedName("place")
    var place: Int,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("end_time")
    var end_time: String? = null,
    @SerializedName("content")
    var content: String? = null
)
class CreateScheduleData(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("schedule_date")
    var schedule_date: String? = null,
    @SerializedName("place")
    var place: Int? = null,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("end_time")
    var end_time: String? = null,
    @SerializedName("content")
    var content: String? = null
)