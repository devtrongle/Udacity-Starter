package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.api.RetrofitApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
