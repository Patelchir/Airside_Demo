package com.example.airside_demo.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.LayoutInflater
import com.example.airside_demo.R


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