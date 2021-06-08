package com.example.android.politicalpreparedness.network

import android.content.res.Resources
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import okhttp3.OkHttpClient

class CivicsHttpClient: OkHttpClient() {

    companion object {

        val apiKey = MyApplication().applicationContext.getString(R.string.civics_api_key)

        fun getClient(): OkHttpClient {
            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", apiKey)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }

    }

}