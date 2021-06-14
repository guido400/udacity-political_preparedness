package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient: OkHttpClient() {

    companion object {

        private val apiKey = BuildConfig.API_KEY
        private val logging = HttpLoggingInterceptor()

        fun getClient(): OkHttpClient {
            logging.level = HttpLoggingInterceptor.Level.BASIC

            return Builder()
                .addInterceptor(logging)
                .addInterceptor{ chain ->
                    val original = chain.request()
                    val url = original
                        .url
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