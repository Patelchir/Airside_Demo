package com.example.airside_demo.network.client

import com.example.airside_demo.home.HomeResponse
import com.example.airside_demo.home.Test
import com.example.airside_demo.network.model.ResponseData
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiInterface {

    @POST
    suspend fun callSearch(
        @Url value: String,
        @QueryMap getAddressRequest: HashMap<String, String>
    ): Response<ResponseData<Test>>

//    @GET("method=flickr.photos.search&api_key=a6cfd97e203b98bf7d87cf8f4778b31b&format=json&nojsoncallback=1&auth_token=72157713719793822-d93982108bc94e40&api_sig=0267b0156233951d6fbe88e6fa678a04")
//    fun search(): Call<ResponseData<Any>>
}