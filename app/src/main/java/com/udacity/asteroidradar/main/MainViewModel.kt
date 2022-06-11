package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.RetrofitApi
import com.udacity.asteroidradar.daos.AsteroidRepository
import com.udacity.asteroidradar.entities.Asteroid
import com.udacity.asteroidradar.entities.PictureOfDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
class MainViewModel(private val context: Context) : ViewModel() {

    private val _mUrlPictureOfDay: MutableLiveData<String> = MutableLiveData("")
    private val _mTitlePictureOfDay: MutableLiveData<String> = MutableLiveData("")

    private val asteroidRepository: AsteroidRepository = AsteroidRepository(context)

    val urlPictureOfDate: LiveData<String>
        get() {
            return _mUrlPictureOfDay
        }

    val titlePictureOfDay: LiveData<String>
        get() {
            return _mTitlePictureOfDay
        }

    val asteroidList: LiveData<List<Asteroid>> = asteroidRepository.getAllAsteroid()

    init {
        Timber.d("MainViewModel init")
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "AsteroidRadar",
            MODE_PRIVATE
        )

        //Get old url
        val oldUrl = sharedPreferences.getString("UrlPictureOfDay", "")
        oldUrl?.let {
            _mUrlPictureOfDay.value = it
        }
        val oldTitle = sharedPreferences.getString("TitlePictureOfDay", "")
        oldTitle?.let {
            _mTitlePictureOfDay.value = it
        }


        //Get new url
        RetrofitApi.apiService.getApod(API_KEY)
            .enqueue(object : Callback<PictureOfDay> {
                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.apply {
                            if (mediaType == "image") {
                                _mUrlPictureOfDay.value = url
                                _mTitlePictureOfDay.value = title

                                //Save cache
                                val editor = sharedPreferences.edit()
                                editor.putString("UrlPictureOfDay", url)
                                editor.putString("TitlePictureOfDay", title)
                                editor.apply()

                                Timber.d("_mUrlPictureOfDay.value = %s", url)
                                return@onResponse
                            }
                        }
                    }
                    Timber.d("_mUrlPictureOfDay.value = null")
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    Timber.e(t)
                }
            })
    }
}