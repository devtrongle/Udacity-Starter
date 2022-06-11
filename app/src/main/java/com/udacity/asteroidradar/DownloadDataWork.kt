package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.RetrofitApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.daos.AppDatabase
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DownloadDataWork(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "DownloadDataWork"
    }

    @SuppressLint("SimpleDateFormat")
    override suspend fun doWork(): Result {
        Timber.d("doWork() start")
        val asteroidDAO = AppDatabase.getInstance(applicationContext).getAsteroidDAO()

        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
        val calendar = Calendar.getInstance()
        val startDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val endDate = dateFormat.format(calendar.time)

        return try {
            val rs = RetrofitApi.apiService.getFeed(startDate, endDate, Constants.API_KEY)
            val jsonObject = JSONObject(rs as Map<String, Any>)
            val dateList: ArrayList<String> = arrayListOf()
            parseAsteroidsJsonResult(jsonObject).forEach {
                asteroidDAO.inserts(it)
                dateList.add(it.closeApproachDate)
            }

            //Delete old data not in list
            asteroidDAO.deleteAllOldDataNotInList(dateList)

            Timber.d("doWork() success")
            Result.success()
        } catch (e: HttpException) {
            Timber.e("doWork() %s", e)
            Result.retry()
        }
    }

}