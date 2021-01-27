package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

class CreateUserResponse (
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var data: CreateUserData?
)
class CreateUser(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null
)
//class Data{
//    @SerializedName("name")
//    var name: String? = null
//    @SerializedName("email")
//    var email: String? = null
//    @SerializedName("token")
//    var token: String? = null
//}
class CreateUserData(
    @SerializedName("name")
    var name: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("token")
    var token: String?
)