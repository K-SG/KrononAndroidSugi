package com.example.sgapp.api

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

class ScheduleDeleteErrorReaponse (
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?
)
