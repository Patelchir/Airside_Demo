package com.example.airside_demo

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.airside_demo.base.ActivityBase
import com.example.airside_demo.base.ViewModelBase
import com.example.airside_demo.databinding.ActivityMainBinding
import com.example.airside_demo.utils.Utils
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class MainActivity : ActivityBase<ViewModelBase>() {

    private lateinit var binding: ActivityMainBinding
    private var dialog: Dialog? = null
    var toolbar: Toolbar? = null
    var txtTitle: TextView? = null
    var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        toolbar = binding.layToolbar.toolbar
        txtTitle = binding.layToolbar.toolbarTitle
        searchView = binding.layToolbar.search_view

        setNavaigationHostFragment(supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment)
        toolbar?.setNavigationOnClickListener {
            hideKeyboard()
            onBackPressed()
        }
    }

    fun displayProgress(t: Boolean) {
        // binding.loading = t
        if (t) {
            if (dialog == null)
                dialog = Utils.progressDialog(this)

            dialog?.show()
        } else {
            dialog?.cancel()
        }
    }

    fun manageToolbar(
        isSearchShow: Int, isTitelShow: Int,
        navigationIcon: Drawable?
    ) {
        searchView?.visibility = isSearchShow
        txtTitle?.visibility = isTitelShow
        toolbar?.navigationIcon = navigationIcon
    }
}
