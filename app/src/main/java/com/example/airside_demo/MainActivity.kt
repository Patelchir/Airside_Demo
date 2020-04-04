package com.example.airside_demo

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.airside_demo.base.ActivityBase
import com.example.airside_demo.base.ViewModelBase
import com.example.airside_demo.databinding.ActivityMainBinding
import com.example.airside_demo.utils.Utils

class MainActivity : ActivityBase<ViewModelBase>() {

    private lateinit var binding: ActivityMainBinding
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
}
