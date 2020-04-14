package com.example.airside_demo

import android.app.Application
import com.example.airside_demo.network.ApiClient

class AirsideApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.initRetrofit()
    }
}