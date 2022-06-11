package com.udacity.asteroidradar.daos

import android.annotation.SuppressLint
import android.content.Context
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.RetrofitApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(context: Context) {

    private val mAsteroidDAO = AppDatabase.getInstance(context).getAsteroidDAO()

    fun getAllAsteroid() = mAsteroidDAO.getAllAsteroid()

}