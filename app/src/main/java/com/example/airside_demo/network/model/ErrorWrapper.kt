package com.example.airside_demo.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ErrorWrapper {
    @SerializedName("errors")
    @Expose
    var errors: JsonObject? = null

    @SerializedName("stat")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("message_code")
    @Expose
    var message_code: String? = null
    @SerializedName("code")
    @Expose
    var status_code: Int? = null

}
