package com.example.lab5v2.api

import android.content.Context
import com.example.lab5v2.service.TokenService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private lateinit var client: OkHttpClient;

    private lateinit var retrofit: Retrofit


    fun<T> buildService(context: Context, service: Class<T>): T{
        val jwtToken: String? = TokenService(context).getAccessToken()

        client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val request: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $jwtToken")
                        .build()
                    return chain.proceed(request)
                }
            }).build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://94.241.169.172:8085/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        return retrofit.create(service)
    }



}