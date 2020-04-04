package com.example.airside_demo.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseListData<T> : ResponseWrapper<T>(){

    @SerializedName("data")
    @Expose
    var data: List<T>? = null

    override fun toString(): String {
        return "ResponseWrapper{" +
                "data=" + data .toString()
    }

}
