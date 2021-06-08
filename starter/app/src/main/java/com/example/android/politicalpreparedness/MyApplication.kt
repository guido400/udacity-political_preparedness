package com.example.android.politicalpreparedness

import android.app.Application
import android.content.Context

class MyApplication:Application() {

    fun getAppContext(): Context {
        return this.applicationContext
    }
}