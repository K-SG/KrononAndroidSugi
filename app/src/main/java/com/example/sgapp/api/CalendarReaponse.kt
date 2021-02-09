package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

class CalendarReaponse (
        @SerializedName("success")
        val success: Boolean?,
        @SerializedName("code")
        val code: Int?,
        @SerializedName("data")
        var data: Array<CalendarSchedule>? = null
){
        fun getScheduleShortArray(): Array<CalendarData?>?{
                val dataArray = this.data ?: return null
                val scheduleArray: Array<CalendarData?> = arrayOfNulls(dataArray.size)
                for(i in scheduleArray.indices){
                        val schedule = dataArray[i]
//                        val setDate = schedule?.date
//                        val setStartTime = schedule?.start_time
//                        val setTitle = schedule?.title
                        CalendarData(schedule?.date!!,schedule?.start_time!!,schedule?.title!!)
                }
                return scheduleArray
        }
}

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

class CalendarData(
        var date: String,
        val start_time: String,
        val title: String
)