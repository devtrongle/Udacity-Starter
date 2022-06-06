package com.udacity.asteroidradar.api

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException


object RetrofitApi {
    private var retrofit: Retrofit? = null
    val apiService: ApiServices
        get() {
            if (retrofit == null) {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
            }
            return retrofit!!.create(ApiServices::class.java)
        }
}
