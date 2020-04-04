package com.example.airside_demo.base

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.airside_demo.MainActivity
import com.example.airside_demo.R
import com.example.airside_demo.network.model.HttpErrorCode
import com.example.airside_demo.utils.DebugLog
import com.google.android.material.snackbar.Snackbar

abstract class FragmentBase<V : ViewModelBase, DataBinding : ViewDataBinding> :
    FragmentBaseWrapper() {
    private var _activity: AppCompatActivity? = null
    private var viewModel: V? = null
    private lateinit var dataBinding: DataBinding
    private val MY_REQUEST_CODE = 1111
//    private var appUpdateManager: AppUpdateManager? = null


    /**
     * This is the abstract method by which we are generating the
     * Databinding with View from Single Screen.
     *
     */
    abstract fun getLayoutId(): Int

    /**
     * This is the generic method where we have to setup the toolbar from single place and
     * from all other extended fragment should have to manage the logic related to the toolbar
     * from this method
     */
    abstract fun setupToolbar()

    /**
     * This is the method from where we are initialized the
     * fragment specific data members and methods.
     */
    abstract fun initializeScreenVariables()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        return dataBinding.root
    }


    private fun handleException(code: Int, message: String, messageCode: String) {
        when (code) {
            HttpErrorCode.EMPTY_RESPONSE.code -> dataNotFound(message, messageCode)
            HttpErrorCode.NEW_VERSION_FOUND.code -> newVersionFound()
            HttpErrorCode.UNAUTHORIZED.code -> unAuthorizationUser(message, messageCode)
            HttpErrorCode.NO_CONNECTION.code -> noInternet()
            else -> somethingWentWrong(message)
        }

    }

    fun getDataBinding() = dataBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewModel is set as Binding Variable
        dataBinding.apply {
            lifecycleOwner = this@FragmentBase
        }
        setActivity(activity as AppCompatActivity)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        initializeScreenVariables()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        setUpSnackbar()
        setUpProgressBar()
//        checkForInAppUpdate()
    }




    /**
     * This is the abstract method where we are adding the logic for generating the ViewModel
     * Object.
     */
    abstract fun getViewModel(): V?


    /**
     * This is generic method based on the MutableLive Data Concept where value changed with Snakebar
     * will reflect and display the message with Snakebar.
     */
    private fun setUpSnackbar() {
        viewModel?.getSnakeBarMessage()?.observe(this, Observer { o: Any ->
            if (_activity != null) {
                if (o is Int) {
                    _activity?.resources?.getString(o)?.let { showSnackbar(it) }!!
                } else if (o is String) {
                    showSnackbar(o)
                }
            }

        } as Observer<Any>)
    }

    private fun setUpProgressBar() {
        viewModel?.getProgressBar()?.observe(this, Observer { t: Boolean ->
            if (_activity != null && getBaseActivity() is MainActivity) {
                (_activity as MainActivity).displayProgress(t)
            }
        })
    }

    /**
     * This is generic method based on the MutableLive Data Concept where value changed with Keyboard
     * will reflect and decide if value is false then it will hide the Keyboard.
     */

    override fun onDetach() {
        super.onDetach()
        _activity = null
    }

    private fun setActivity(activity: AppCompatActivity) {
        this._activity = activity
    }

    private fun getBaseActivity(): AppCompatActivity? {
        return _activity
    }




    /**
     * This method is used to display the Snake Bar Toast message to user.
     *
     * @param message string to display.
     */
    fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            _activity?.findViewById(android.R.id.content)!!,
            message,
            Snackbar.LENGTH_SHORT
        )
        val view = snackbar.view
        val snackTV = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTV.maxLines = 5
//        snackTV.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackbar.show()
    }


    /**
     * This is the generic method from where the message will display if something went wrong.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun somethingWentWrong(message: String?) {
        var msg = message
        if (message.isNullOrEmpty()) {
            msg = getString(R.string.msg_500_error_code)
        }
        viewModel?.showSnackbarMessage(msg!!)
    }


    /**
     * This is the generic method from where the message will display if Internet connection not available.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun noInternet() {
        viewModel?.showSnackbarMessage(R.string.msg_no_internet)
    }


    override fun onRetryClicked(retryButtonType: String) {
    }


    /**
     * This is the generic method from where the message will display if No Data Found.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun dataNotFound(message: String?, messageCode: String?) {
        message?.let { viewModel?.showSnackbarMessage(it) }
    }


    /**
     * This is the generic method from where the message will display if user verified or not.
     * This will called based from the make Api called and decide the response with response code.
     */
    override fun verifyUser(message: String) {
        viewModel?.showSnackbarMessage(message)
    }

    override fun newVersionFound() {
    }

    /**
     * This is the generic method from where the message will display if user is unauthorized.
     * This will called based from the make Api called and decide the response with response code.
     */



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                DebugLog.print("Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }

    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    override fun onResume() {
        super.onResume()
    }

}