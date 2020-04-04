package com.example.airside_demo.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeRequest {
    //{"customerId":"MTI4","pageNumber":"1","storeCode":"en"}

    @Expose
    @SerializedName("method ")
    var method: String? = "flickr.photos.search"
    @Expose
    @SerializedName("api_key ")
    var apiKey: String? = "asdvaskdg"
    @Expose
    @SerializedName("text")
    var search: String? = "abc"
    @Expose
    @SerializedName("format")
    var format: String? = "json"

}