package com.example.airside_demo.home

import com.example.airside_demo.BuildConfig
import com.example.airside_demo.network.client.ApiInterface
import com.example.airside_demo.base.BaseRepository
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository(private val apiInterface: ApiInterface) : BaseRepository() {

    suspend fun callGetProfileAPI(): ResponseHandler<ResponseData<Test>?> {
        return withContext(Dispatchers.Default) {
            return@withContext makeAPICall(
                call = {
                    val hashMap = HashMap<String, String>()
                    hashMap["method"] = "flickr.photos.search"
                    hashMap["api_key"] = BuildConfig.FlickerApiKey
                    hashMap["format"] = "json"
                    hashMap["text"] = "gar"
                    hashMap["nojsoncallback"] = "1"
                    hashMap["per_page"] = "30"
                    hashMap["page"] = "1"
                    apiInterface.callSearch(
                        "",
                        hashMap
                    )
                })
        }
    }
}