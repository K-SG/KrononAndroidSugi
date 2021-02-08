package com.example.sgapp.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

class ScheduleData(
        @SerializedName("schedule_id")
        val schedule_id: Int?,

        @SerializedName("title")
        val title: String?,

        @SerializedName("schedule_date")
        val schedule_date: String?,

        @SerializedName("place")
        val place: Int?,

        @SerializedName("start_time")
        val start_time: String?,

        @SerializedName("end_time")
        val end_time: String?,

        @SerializedName("content")
        val content: String?
)

class ShowScheduleResponse(
        @SerializedName("success")
        val success: Boolean?,

        @SerializedName("code")
        val code: Int?,

        @SerializedName("data")
        var data: Array<ShowScheduleData>? = null,

        @SerializedName("message")
        var message: String? = null
){
        fun getScheduleShortArray() : Array<Array<ScheduleShortData?>?>?{

                val dataArray = this.data ?: return null

                val scheduleShortArray : Array<Array<ScheduleShortData?>?> = arrayOfNulls(dataArray.size)
                for(i in dataArray.indices){
                        val scheduleArray = dataArray[i].schedules

                        if(scheduleArray == null){
                                scheduleShortArray[i] = arrayOfNulls(0)
                                break
                        }
                        scheduleShortArray[i] = arrayOfNulls(scheduleArray.size)
                        for(j in scheduleArray.indices){

                                val schedule = scheduleArray[j]

                                val df = SimpleDateFormat("HH:mm:ss")
                                val startTime = df.parse(schedule.start_time)
                                val startMinutes = startTime.minutes + startTime.hours*60
                                val endTime = df.parse(schedule.end_time)
                                val endMinutes = endTime.minutes + endTime.hours*60

                                scheduleShortArray[i]?.set(j,
                                        ScheduleShortData(schedule?.schedule_id!!,schedule?.title!!,startMinutes,endMinutes,schedule.place!!)
                                )
                        }
                }
                return scheduleShortArray
        }

        fun getNamesArray() : Array<String>?{
                val dataArray = this.data ?: return null
                var nameArray : Array<String> = Array(dataArray.size){""}

                for(i in dataArray.indices){
                        nameArray[i] = (dataArray[i].name.toString())
                }

                return nameArray
        }
}

class ShowScheduleData(
        @SerializedName("name")
        val name: String?,

        @SerializedName("schedules")
        var schedules: Array<ScheduleData>? = null
)

class ScheduleShortData(
        val scheduleId: Int,
        val title: String,
        val startTime: Int,
        val endTime: Int,
        val place: Int
)

