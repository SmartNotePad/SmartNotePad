package com.tez.smartnotepad.data.datasource.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    private var retrofit: Retrofit? = null
    var baseUrl: String = "http://192.168.1.20:8080/api/"
    var logging: HttpLoggingInterceptor = HttpLoggingInterceptor()


    fun getClient(): Retrofit {

        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        if (retrofit == null)
            retrofit =
                Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build()

        return retrofit as Retrofit
    }
}