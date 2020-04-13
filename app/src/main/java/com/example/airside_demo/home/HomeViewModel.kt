package com.example.airside_demo.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.airside_demo.base.ViewModelBase
import com.example.airside_demo.network.ApiClient
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ResponseData
import kotlinx.coroutines.launch

class HomeViewModel : ViewModelBase() {
    // TODO: Implement the ViewModel
    internal var responseOrgDashboard =
        MutableLiveData<ResponseHandler<ResponseData<HomeResponse>?>>()
    private var homeRepository: HomeRepository? =
        HomeRepository(ApiClient.getApiInterface())

    fun callSearchAPI(page: Int, query: String?) {
        viewModelScope.launch(coroutineContext) {
            responseOrgDashboard.value = ResponseHandler.Loading
            responseOrgDashboard.value =
                homeRepository?.callGetProfileAPI(page,query)
        }
    }
}
