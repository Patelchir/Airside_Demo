package com.example.airside_demo.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.airside_demo.R
import com.example.airside_demo.utils.DebugLog

abstract class ActivityBase<V : ViewModelBase> : AppCompatActivity() {
    val viewModel by viewModels<ViewModelBase>()
    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpHideKeyBoard()
    }

    private fun setUpHideKeyBoard() {
        viewModel.getHideKeyBoardEvent().observe(this, Observer { hideKeyboard() })
    }

    /**
     * This method is used to hide the keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /**
     * This method is used for Navigating from One Screen to Next Screen using Navigation
     * Direction graph.
     * @param navigationId This is the direction Id from Navigation graph
     */
    fun navigateToNextScreenThroughDirections(navigationId: NavDirections) {
        try {
            navHostFragment.findNavController().navigate(navigationId)
        } catch (e: Exception) {
            DebugLog.print(e)
        }
    }



    /**
     * This is the Method to initialize the variable at base level for Navigating from Single Class.
     * @param navHostFragment This is the Id of the NavHost Fragment or  FragmentContainerView.
     */
    fun setNavaigationHostFragment(navHostFragment: NavHostFragment) {
        this.navHostFragment = navHostFragment
    }


    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fragment_container_view)
    }


    /**
     * This is the method used for setup the Configuration change with Language locale.
     *
     */
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}