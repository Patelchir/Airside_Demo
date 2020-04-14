package com.example.airside_demo.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.airside_demo.base.ViewModelBase
import com.example.airside_demo.network.ApiClient
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ResponseData
import kotlinx.coroutines.launch

class SearchViewModel : ViewModelBase() {
    // TODO: Implement the ViewModel
    internal var responseOrgDashboard =
        MutableLiveData<ResponseHandler<ResponseData<SearchResponse>?>>()
    private var searchRepository: SearchRepository? =
        SearchRepository(ApiClient.getApiInterface())

    fun callSearchAPI(page: Int, query: String?) {
        viewModelScope.launch(coroutineContext) {
            responseOrgDashboard.value = ResponseHandler.Loading
            responseOrgDashboard.value =
                searchRepository?.callGetProfileAPI(page,query)
        }
    }
}
