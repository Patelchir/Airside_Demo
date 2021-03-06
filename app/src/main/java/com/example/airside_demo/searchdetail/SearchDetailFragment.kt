package com.example.airside_demo.searchdetail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.airside_demo.MainActivity
import com.example.airside_demo.R
import com.example.airside_demo.base.FragmentBase
import com.example.airside_demo.bind.BindAdapters
import com.example.airside_demo.databinding.SearchDetailFragmentBinding
import com.example.airside_demo.utils.Constant
import com.example.airside_demo.utils.DebugLog

class SearchDetailFragment : FragmentBase<SearchDetailViewModel, SearchDetailFragmentBinding>() {

    private lateinit var viewmodel: SearchDetailViewModel
    private var imageURL = ""
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageURL = SearchDetailFragmentArgs.fromBundle(arguments!!).imageURL
        title = SearchDetailFragmentArgs.fromBundle(arguments!!).title
        (activity as MainActivity).toolbar?.setNavigationOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.search_detail_fragment
    }

    override fun setupToolbar() {
        context?.let {
            ContextCompat.getDrawable(it, R.drawable.ic_arrow_back)?.let {
                (activity as MainActivity).manageToolbar(
                    View.GONE, View.VISIBLE,
                    it
                )
            }
        }
        (activity as MainActivity).txtTitle?.text = title

    }

    override fun initializeScreenVariables() {
        BindAdapters.bindImageData(
            getDataBinding().imgPhoto,
            imageURL
        )
    }

    override fun getViewModel(): SearchDetailViewModel? {
        viewmodel = ViewModelProvider(this).get(SearchDetailViewModel::class.java)
        return viewmodel
    }

    override fun unAuthorizationUser(message: String?, messageCode: String?) {
    }

}
