package com.example.airside_demo.bind

import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.airside_demo.R
import java.io.File

class BindAdapters {
    companion object {

        @SuppressLint("CheckResult")
        @BindingAdapter("bind:imageSet")
        @JvmStatic
        fun bindImageData(imageView: AppCompatImageView, url: String?) {
            if (url == null || url.isEmpty()) {
                imageView.setImageResource(R.mipmap.ic_launcher)
                return
            } else {
                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.drawable.ic_placeholder_image)
                    .error(R.drawable.ic_error_image).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                if ((url.contains("https://") || url.contains("http://"))) {
                    Glide.with(imageView.context).setDefaultRequestOptions(requestOptions).load(url)
                        .into(imageView)
                } else {
                    Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                        .load(File(url)).into(imageView)
                }
            }
        }

    }
}