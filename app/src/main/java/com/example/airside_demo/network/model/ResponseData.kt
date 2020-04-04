package com.example.airside_demo.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class ResponseData<T> : ResponseWrapper<T>(){

    @SerializedName("photos")
    @Expose
    var data: T? = null

    override fun toString(): String {
        return "ResponseWrapper{" +
                "photos=" + data.toString()
    }

}
