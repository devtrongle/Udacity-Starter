package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.RetrofitApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    private val _mUrlPictureOfDay: MutableLiveData<String> = MutableLiveData("")

    private val _asteroidList: MutableLiveData<List<Asteroid>> = MutableLiveData()

    val urlPictureOfDate : LiveData<String>
    get(){
        return _mUrlPictureOfDay
    }

    val asteroidList : LiveData<List<Asteroid>>
    get(){
        return _asteroidList
    }

    init {
        Timber.d("MainViewModel init")
        getPictureOfDay()
        getAsteroidList()
    }

    @SuppressLint("SimpleDateFormat")
    fun getAsteroidList(){
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT)
        val calendar = Calendar.getInstance();
        val startDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val endDate = dateFormat.format(calendar.time)

        RetrofitApi.apiService.getFeed(startDate,
            endDate,
            API_KEY)
            .enqueue(object: Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    Timber.d(response.body().toString())
                    val jsonObject = JSONObject(response.body() as Map<String, Any>)
                    _asteroidList.value = parseAsteroidsJsonResult(jsonObject)
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Timber.e(t);
                }

            })
    }

    private fun getPictureOfDay() = RetrofitApi.apiService.getApod(API_KEY)
        .enqueue(object: Callback<PictureOfDay>{
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                if(response.isSuccessful){
                    response.body()?.apply {
                        if(mediaType == "image"){
                            _mUrlPictureOfDay.value = url
                            Timber.d("_mUrlPictureOfDay.value = %s" , url)
                            return@onResponse
                        }
                    }
                }
                Timber.d("_mUrlPictureOfDay.value = null")
                _mUrlPictureOfDay.value = ""
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                Timber.e(t)
                _mUrlPictureOfDay.value = ""
            }
        })
}