package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

//class CreateUserErrorResponse {
//    @SerializedName("success")
//    var success: Boolean? = null
//    @SerializedName("code")
//    var code: Int? = null
//    @SerializedName("message")
//    var message: ErrorMessage? = null
//}

class LoginUserErrorResponse (
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?
)

//class LoginErrorMessage(
//    @SerializedName("name")
//    var name: Array<String>?,
//    @SerializedName("email")
//    var email: Array<String>? ,
//    @SerializedName("password")
//    var password: Array<String>?
//)
