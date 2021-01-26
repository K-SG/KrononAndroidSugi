package com.example.sgapp.Retrofit2

import com.google.gson.annotations.SerializedName

//class CreateUserErrorResponse {
//    @SerializedName("success")
//    var success: Boolean? = null
//    @SerializedName("code")
//    var code: Int? = null
//    @SerializedName("message")
//    var message: ErrorMessage? = null
//}

class CreateUserErrorResponse (
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: ErrorMessage? = null
)
class ErrorMessage(
    @SerializedName("name")
    var name: Array<String>?,
    @SerializedName("email")
    var email: Array<String>? ,
    @SerializedName("password")
    var password: Array<String>?
)
