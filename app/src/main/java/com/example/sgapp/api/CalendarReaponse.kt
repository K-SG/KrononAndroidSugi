package com.example.sgapp.api

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

class CalendarReaponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    var data: Array<CalendarSchedule>? = null
) {
//    fun getCalendarArray(): Array<CalendarData?>? {
//        val dataArray = this.data ?: return null
//        val scheduleArray: Array<CalendarData?> = arrayOfNulls(dataArray.size)
//        for (i in dataArray.indices) {
//            for (j in scheduleArray.indices) {
//                CalendarData(dataArray[j]?.date!!, dataArray[j]?.start_time!!, dataArray[j]?.title!!)
//            }
//        }
//        return  scheduleArray
//    }
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
