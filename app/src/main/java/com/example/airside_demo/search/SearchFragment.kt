package com.example.airside_demo.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airside_demo.MainActivity
import com.example.airside_demo.R
import com.example.airside_demo.base.FragmentBase
import com.example.airside_demo.bind.BindAdapters
import com.example.airside_demo.bind.GenericRecyclerViewAdapter
import com.example.airside_demo.databinding.RowSearchBinding
import com.example.airside_demo.databinding.SearchFragmentBinding
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ResponseData
import com.example.airside_demo.utils.Utils

class SearchFragment : FragmentBase<SearchViewModel, SearchFragmentBinding>(),
    SearchView.OnQueryTextListener {

    private lateinit var viewmodel: SearchViewModel
    private var photoList: ArrayList<SearchResponse.Photo> = ArrayList()

    // Pagination
    private var currentPage = 1
    private var query = ""
    var totalRecords = 0
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var loadingMore = true
    var isCallApi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.search_fragment
    }

    override fun setupToolbar() {
        (activity as MainActivity).manageToolbar(View.VISIBLE, View.GONE, null)
        (activity as MainActivity).searchView?.setOnQueryTextListener(this)
    }


    override fun initializeScreenVariables() {
        getDataBinding().txtSearch.text =
            getString(R.string.tap_on_search_icon_to_image)
        setLiveDataObservers()
        rvAddOnScrollListener()
        setAdapter()
    }

    override fun getViewModel(): SearchViewModel? {
        viewmodel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return viewmodel
    }

    override fun unAuthorizationUser(message: String?, messageCode: String?) {
        message?.let { showSnackbar(it) }
    }

    override fun onResume() {
        super.onResume()
        getDataBinding().rvList.visibility = View.VISIBLE
        if (photoList.size == 0) {
            getDataBinding().txtSearch.visibility = View.VISIBLE
        }
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
                    isCallApi = false
                    httpFailedHandler(it.code, it.message, it.messageCode)

                }
                is ResponseHandler.OnSuccessResponse<ResponseData<SearchResponse>?> -> {
                    viewmodel.showProgressBar(false)


                    isCallApi = false
                    totalRecords = it.response?.data?.total!!.toInt()
                    if (currentPage == 1 && it.response.data != null && it.response.data?.photo?.size!! > 0) {
                        photoList.clear()
                        getDataBinding().rvList.visibility = View.VISIBLE
                        it.response.data?.photo?.let { it1 -> photoList.addAll(it1) }

                        getDataBinding().rvList.adapter?.notifyDataSetChanged()

                        getDataBinding().rvList.visibility = View.VISIBLE

                        loadingMore = photoList.size < totalRecords

                    } else if (currentPage > 1 && it.response.data != null && it.response.data?.photo?.size!! > 0) {
                        viewmodel.showProgressBar(false)
                        it.response.data?.photo?.let { it1 -> photoList.addAll(it1) }
                        getDataBinding().rvList.adapter?.notifyDataSetChanged()
                        loadingMore = photoList.size < totalRecords
                    } else {
                        loadingMore = false
                        viewmodel.showProgressBar(false)
                        if (currentPage == 1) {
                            getDataBinding().txtSearch.visibility = View.VISIBLE
                            getDataBinding().rvList.visibility = View.GONE
                            showSnackbar(getString(R.string.no_data_found))
                        }
                    }
                }
            }
        })
    }

    private fun setAdapter() {
        val myAdapter = object :
            GenericRecyclerViewAdapter<SearchResponse.Photo, RowSearchBinding>(
                context!!,
                photoList
            ) {
            override val layoutResId: Int
                get() = R.layout.row_search

            @SuppressLint("SetTextI18n")
            override fun onBindData(
                model: SearchResponse.Photo,
                position: Int,
                dataBinding: RowSearchBinding
            ) {
                dataBinding.executePendingBindings()
                model.imageurl =
                    "https://farm" + model.farm + ".staticflickr.com/" + model.server + "/" + model.id + "_" + model.secret + "_b.jpg"
                BindAdapters.bindImageData(
                    dataBinding.imgPhoto,
                    model.imageurl
                )

            }

            override fun onItemClick(
                model: SearchResponse.Photo,
                position: Int,
                mDataBinding: ViewDataBinding
            ) {

                (activity as MainActivity).navigateToNextScreenThroughDirections(
                    SearchFragmentDirections.actionSeachFragmentToSearchDetailFragment(
                        model.imageurl,
                        model.title
                    )
                )
            }

        }
        getDataBinding().rvList.adapter = myAdapter
    }

    private fun rvAddOnScrollListener() {
        getDataBinding().rvList.setHasFixedSize(true)

        getDataBinding().rvList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount =
                        (getDataBinding().rvList.layoutManager as LinearLayoutManager).childCount
                    totalItemCount =
                        (getDataBinding().rvList.layoutManager as LinearLayoutManager).itemCount
                    firstVisibleItem =
                        (getDataBinding().rvList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (visibleItemCount + firstVisibleItem >= totalItemCount - 4 && loadingMore && !isCallApi
                        && photoList.size < totalRecords
                    ) {
                        isCallApi = true
                        currentPage += 1
                        viewmodel.callSearchAPI(currentPage, query)
                    }
                }
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        (activity as MainActivity).searchView?.clearFocus()
        setPaginationData()
        this.query = query!!
        getDataBinding().txtSearch.visibility = View.GONE
        photoList.clear()
        getDataBinding().rvList.adapter?.notifyDataSetChanged()
        viewmodel.callSearchAPI(currentPage, query)
        return true
    }

    private fun setPaginationData() {
        totalRecords = 0
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        loadingMore = true
        isCallApi = false
        currentPage = 1
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}

