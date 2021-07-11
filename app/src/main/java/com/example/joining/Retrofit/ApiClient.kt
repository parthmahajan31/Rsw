package com.example.xicomtask.Retrofit

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        var BASE_URL = "http://dev1.xicom.us/xttest/"

        var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("data", message)
                }

            })
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(logging).build()
            if (retrofit ==null){
                retrofit = Retrofit.Builder().baseUrl(
                    BASE_URL
                ).client(client).addConverterFactory(
                    GsonConverterFactory.create()).build()
            }
            return retrofit
        }


    }
}