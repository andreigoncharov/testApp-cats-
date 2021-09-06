package com.example.myapp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient{
    private const val BASE_URL = "http://mobile-test.itfox-web.com:80/private/"

    private lateinit var token: String

    fun setToken(tok: String){
        token = tok
    }

    var client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response? {
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
    }).build()

    val instance: Api by lazy{
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }
}