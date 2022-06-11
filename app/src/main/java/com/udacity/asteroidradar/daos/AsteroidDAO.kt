package com.udacity.asteroidradar.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.entities.Asteroid

@Dao
interface AsteroidDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserts(vararg asteroid: Asteroid)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updates(vararg asteroid: Asteroid)

    @Query("SELECT * FROM Asteroid WHERE id = :id")
    fun getAsteroidById(id: Long) : LiveData<Asteroid>

    @Query("DELETE FROM Asteroid WHERE closeApproachDate NOT IN (:closeApproachDate)")
    fun deleteAllOldDataNotInList(closeApproachDate: List<String>)

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroid() : LiveData<List<Asteroid>>
}