package com.example.airside_demo.search

import com.example.airside_demo.BuildConfig
import com.example.airside_demo.network.client.ApiInterface
import com.example.airside_demo.base.BaseRepository
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ResponseData
import com.example.airside_demo.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(private val apiInterface: ApiInterface) : BaseRepository() {

    suspend fun callGetProfileAPI(page: Int, query: String?): ResponseHandler<ResponseData<SearchResponse>?> {
        return withContext(Dispatchers.Default) {
            return@withContext makeAPICall(
                call = {
                    val hashMap = HashMap<String, String>()
                    hashMap["method"] = Constant.API_METHOD
                    hashMap["api_key"] = BuildConfig.FlickerApiKey
                    hashMap["format"] = Constant.API_FORMAT
                    hashMap["text"] = query.toString()
                    hashMap["nojsoncallback"] = Constant.API_NO_JSON_CALLBACK
                    hashMap["per_page"] = Constant.API_PER_PAGE
                    hashMap["page"] = page.toString()
                    apiInterface.callSearch(
                        "",
                        hashMap
                    )
                })
        }
    }
}