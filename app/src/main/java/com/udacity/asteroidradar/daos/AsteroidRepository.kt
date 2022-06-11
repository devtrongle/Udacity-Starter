package com.udacity.asteroidradar.daos

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.ListenableWorker
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.RetrofitApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val context: Context) {

    private val mAsteroidDAO = AppDatabase.getInstance(context).getAsteroidDAO()

    fun getAllAsteroid() = mAsteroidDAO.getAllAsteroid()

    @SuppressLint("SimpleDateFormat")
    suspend fun getAsteroidFromApi() {
        Timber.d("getAsteroidFromApi() start")
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
        val calendar = Calendar.getInstance()
        val startDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val endDate = dateFormat.format(calendar.time)

        try {
            val rs = RetrofitApi.apiService.getFeed(startDate, endDate, Constants.API_KEY)
            val jsonObject = JSONObject(rs as Map<String, Any>)
            val dateList: ArrayList<String> = arrayListOf()
            parseAsteroidsJsonResult(jsonObject).forEach {
                mAsteroidDAO.inserts(it)
                dateList.add(it.closeApproachDate)
            }
            //Delete old data not in list
            mAsteroidDAO.deleteAllOldDataNotInList(dateList)
            Timber.d("getAsteroidFromApi() success")
        } catch (e: HttpException) {
            Timber.e("getAsteroidFromApi() %s", e)
        }
    }
}