package com.udacity.asteroidradar.daos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import com.udacity.asteroidradar.entities.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) : AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "AsteroidRadar.db")
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun getAsteroidDAO() : AsteroidDAO
}