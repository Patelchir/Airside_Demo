package com.example.airside_demo.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.airside_demo.R
import com.example.airside_demo.base.FragmentBase
import com.example.airside_demo.bind.BindAdapters
import com.example.airside_demo.bind.GenericRecyclerViewAdapter
import com.example.airside_demo.databinding.HomeFragmentBinding
import com.example.airside_demo.databinding.RowHomeBinding
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ResponseData

class HomeFragment : FragmentBase<HomeViewModel, HomeFragmentBinding>() {

    private lateinit var viewmodel: HomeViewModel
    private var homeRequest = HomeRequest()
    private var photoList: ArrayList<Photo> = ArrayList()
    // Pagination
    private var currentPage = 1
    var totalRecords = 0
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var loadingMore = true
    var isCallApi = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.callOrgDashboard(homeRequest)
    }


    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun setupToolbar() {
    }

    override fun initializeScreenVariables() {
        setLiveDataObservers()
        setProjects()
    }

    override fun getViewModel(): HomeViewModel? {
        viewmodel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return viewmodel
    }

    override fun unAuthorizationUser(message: String?, messageCode: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setLiveDataObservers() {

        viewmodel.responseOrgDashboard.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    viewmodel.showProgressBar(true)
                }
                is ResponseHandler.OnFailed -> {
                    viewmodel.showProgressBar(false)
                    isCallApi = false;
                    httpFailedHandler(it.code, it.message, it.messageCode)

                }
                is ResponseHandler.OnSuccessResponse<ResponseData<Test>?> -> {
                    viewmodel.showProgressBar(false)


                    isCallApi = false

                    if (it.response?.data!= null){
                //    totalRecords = it.response?.data?.photos?.total!!.toInt()
//                    if (currentPage == 1 && it.response.data != null && it.response.data?.photos?.photo?.size!! > 0) {
//                        photoList.clear()
//                        it.response.data?.photos?.photo?.let { it1 -> photoList.addAll(it1) }
//
//                        getDataBinding().rvList.adapter?.notifyDataSetChanged()
//
//                        getDataBinding().rvList.visibility = View.VISIBLE
//                    }
//                        getDataBinding().noProduct.visibility = View.GONE

                        //Next Page
                      //  loadingMore = browsingHistoryList.size < totalRecords

                    } /*else if (currentPage > 1 && it.response.data != null && it.response.data?.productsDataList?.size!! > 0) {
                        viewmodel.showProgressBar(false)
                        it.response.data?.products?.let { it1 -> browsingHistoryList.addAll(it1) }
                        getDataBinding().rvBrowsingHistory.adapter?.notifyDataSetChanged()
                        loadingMore = browsingHistoryList.size < totalRecords
                    } else {
                        loadingMore = false
                        viewmodel.showProgressBar(false)
                        getDataBinding().rvBrowsingHistory.visibility = View.GONE
                        getDataBinding().noProduct.visibility = View.VISIBLE
                    }*/

                }
            }
        })
    }
    private fun setProjects() {
        val myAdapter = object :
            GenericRecyclerViewAdapter<Photo, RowHomeBinding>(
                context!!,
                photoList
            ) {
            override val layoutResId: Int
                get() = R.layout.row_home

            @SuppressLint("SetTextI18n")
            override fun onBindData(
                model: Photo,
                position: Int,
                dataBinding: RowHomeBinding
            ) {
                dataBinding.executePendingBindings()
                BindAdapters.bindImageData(dataBinding.imgPhoto,"https://farm"+model.farm+".staticflickr.com/"+model.server+"/"+model.id+"_"+model.secret+".jpg")

            }

            override fun onItemClick(model: Photo, position: Int) {

            }

        }
        getDataBinding().rvList.adapter = myAdapter
    }
}
