package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitApi {
    private var retrofit: Retrofit? = null

    private const val TIME_OUT = 60L

    val apiService: ApiServices
        get() {
            if (retrofit == null) {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val client = OkHttpClient()
                    .newBuilder()
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
                retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(client)
                    .build()
            }
            return retrofit!!.create(ApiServices::class.java)
        }
}
