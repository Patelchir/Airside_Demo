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
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
//        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_progress, null)
//        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


    /*fun openPdf(activity: Context, filePath: String) {
        val customTab = CustomTabsIntent.Builder()
            .setToolbarColor(
                ResourcesCompat.getColor(
                    activity!!.resources,
                    R.color.colorPrimary,
                    null
                )
            )
            .setShowTitle(false)
            .enableUrlBarHiding()
            .build()
        val contentUri: Uri = FileProvider.getUriForFile(
            activity!!,
            BuildConfig.APPLICATION_ID + ".provider",
            File(filePath)
        )
        activity!!.grantUriPermission(
            activity!!.packageName,
            contentUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        customTab.intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        customTab.launchUrl(activity, contentUri)
    }*/

    fun inputStreamToString(inputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            null
        }
    }

    fun <T> toList(
        json: String?,
        typeClass: Class<T>?
    ): List<T>? {
        return Gson().fromJson(json, ListOfJson(typeClass!!))
    }

    fun <T> toClassResponse(
        json: String?,
        typeClass: Class<T>?
    ): T? {
        return if (json == null)
            null
        else
            Gson().fromJson(json, (typeClass!!))
    }

    fun <T> toStringResponse(
        typeClass: T?
    ): String? {
        return if (typeClass == null)
            null
        else
            Gson().toJson(typeClass)
    }

    fun <T> toList(jsonArray: JSONArray?): List<T>? {
        val list: MutableList<T> = ArrayList()
        if (jsonArray != null) {
            val len = jsonArray.length()
            for (i in 0 until len) {
                list.add(jsonArray.opt(i) as T)
            }
        }
        return list

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

    fun marginToPx(mContext: Context, margin: Int): Int {
        val r = mContext.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            mContext.resources.getDimension(margin),
            r.displayMetrics
        ).toInt()
        return px
    }

    fun findWordFromString(
        str: String,
        offset: Int
    ): String { // when you touch ' ', this method returns left word.
        var offset = offset
        if (str.length == offset) {
            offset-- // without this code, you will get exception when touching end of the text
        }

        if (str[offset] == ' ') {
            offset--
        }
        var startIndex = offset
        var endIndex = offset

        try {
            while (str[startIndex] != ' ' && str[startIndex] != '\n') {
                startIndex--
            }
        } catch (e: StringIndexOutOfBoundsException) {
            startIndex = 0
        }

        try {
            while (str[endIndex] != ' ' && str[endIndex] != '\n') {
                endIndex++
            }
        } catch (e: StringIndexOutOfBoundsException) {
            endIndex = str.length
        }

        // without this code, you will get 'here!' instead of 'here'
        // if you use only english, just check whether this is alphabet,
        // but 'I' use korean, so i use below algorithm to get clean word.
        val last = str[endIndex - 1]
        if (last == ',' || last == '.' ||
            last == '!' || last == '?' ||
            last == ':' || last == ';'
        ) {
            endIndex--
        }
        return str.substring(startIndex, endIndex)
    }


    internal fun checkIsImageOrNot(path: String): Boolean {
        val regular_expression =
            "(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*\\.(?:jpg|gif|png|jpeg))(?:\\?([^#]*))?(?:#(.*))?".toRegex()
        return path.matches(regular_expression)
    }

    /**
     * Get Application Name
     * @param context context
     * @return  application name
     */
    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    /**
     * Convert date into desired format
     *
     * @param dateString       input date format as String
     * @param inputDateString  Input Date format
     * @param outputDateString Desired Date format
     * @return converted Date as String
     */

    fun convertDateFormate(
        dateString: String,
        inputDateString: String,
        outputDateString: String
    ): String {
        val date: Date
        val dateFormatLocal = SimpleDateFormat(inputDateString, Locale.US)
        try {
            date = dateFormatLocal.parse(dateString)
            return SimpleDateFormat(outputDateString, Locale.US).format(date)
        } catch (e: ParseException) {
            DebugLog.print(e)
            return ""
        } catch (e: Exception) {
            DebugLog.print(e)
            return ""
        }

    }

    @SuppressLint("NewApi")
    fun whatsApp(context: Context, phone: String?) {
        val packageManager: PackageManager = context.packageManager
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url =
                "https://api.whatsapp.com/send?phone=" + phone.toString() + "&text=" + URLEncoder.encode(
                    "", "UTF-8"
                )
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null) {
                context!!.startActivity(i)
            } else {
                //  ToastUtil.getInstance(context).showToast("WhatsApp not Installed")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            //ToastUtil.getInstance(context).showToast("WhatsApp not Installed")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            //  ToastUtil.getInstance(context).showToast("WhatsApp not Installed")
        }
    }

    fun openDialer(context: Context, phone: String?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        context.startActivity(intent)
    }

    /**
     * Enlarge Hit Area of TextView
     *
     * @param mTextView object of TextView
     */
    fun increaseTextViewHitArea(mTextView: TextView) {
        try {
            val parent = mTextView.parent as View  // button: the view you want to enlarge hit area
            parent.post {
                val rect = Rect()
                mTextView.getHitRect(rect)
                rect.top -= 50    // increase top hit area
                rect.left -= 150   // increase left hit area
                rect.bottom += 100 // increase bottom hit area
                rect.right += 150  // increase right hit area
                parent.touchDelegate = TouchDelegate(rect, mTextView)
            }
        } catch (e: Exception) {
            DebugLog.print(e)
        }

    }

    /**
     * Enlarge Hit Area of ImageView
     *
     * @param mImageView object of Image
     */
    fun increaseImageHitArea(mImageView: ImageView) {
        try {
            val parent = mImageView.parent as View  // button: the view you want to enlarge hit area
            parent.post {
                val rect = Rect()
                mImageView.getHitRect(rect)
                rect.top -= 100    // increase top hit area
                rect.left -= 100   // increase left hit area
                rect.bottom += 100 // increase bottom hit area
                rect.right += 100  // increase right hit area
                parent.touchDelegate = TouchDelegate(rect, mImageView)
            }
        } catch (e: Exception) {
            DebugLog.print(e)
        }
    }

    /**
     * get Current Date
     *
     * @param format desired format for current date
     * @return current date as String
     */
    fun getCurrentDate(format: String): String {
        val c = Calendar.getInstance().time
        DebugLog.print("Current time => $c")

        val df = SimpleDateFormat(format, Locale.US)
        return df.format(c)

    }
}