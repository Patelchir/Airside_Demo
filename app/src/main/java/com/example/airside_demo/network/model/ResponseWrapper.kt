package com.example.airside_demo.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class ResponseWrapper<T> {


    @SerializedName("code")
    @Expose
    var statusCode: Int? = null

    @SerializedName("stat")
    @Expose
    var stat: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("message_code")
    @Expose
    var message_code: String? = "0"

    @SerializedName(value = "error", alternate = ["errors"])
    @Expose
    var jsonError: JsonObject? = null

}
