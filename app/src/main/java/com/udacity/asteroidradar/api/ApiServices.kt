package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("/neo/rest/v1/feed")
    fun getFeed(@Query("start_date") startDate: String,
                @Query("end_date") endDate: String,
                @Query("api_key") apiKey: String) : Call<Any>

    @GET("/planetary/apod")
    fun getApod(@Query("api_key") apiKey: String): Call<PictureOfDay>
}