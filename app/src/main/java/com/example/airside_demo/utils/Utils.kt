package com.example.airside_demo.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.airside_demo.R
import com.example.airside_demo.network.model.ListOfJson
import com.google.gson.Gson
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object Utils {
    /**
     * Common progress dialog
     */
    @SuppressLint("InflateParams")
    @JvmStatic
    fun progressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_progress, null)
        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


    /**
     * Remove [] from Error Objects when there are multiple errors
     *
     * @param message as String
     * @return replacedString
     */
    fun removeArrayBrace(message: String): String {
        return message.replace("[\"", "").replace("\"]", "").replace(".", "")
    }


}