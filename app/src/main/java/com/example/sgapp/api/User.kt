package com.example.sgapp.api

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("data")
    var data: LoginUserData?
    //if文でmessageなかったらでERRORも一緒にいれてOK
)

