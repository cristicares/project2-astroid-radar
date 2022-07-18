package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.MenuOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidRadarDatabase) {

    fun getListAsteroids(filter: MenuOption): LiveData<List<Asteroid>>{
        return when(filter){
            MenuOption.SHOW_WEEK ->
                Transformations.map(database.asteroidDao.getWeekAsteroids(getToday(), getDayPlusWeek())){
                    it.asDomainModel()
                }
            MenuOption.SHOW_TODAY ->
                Transformations.map(database.asteroidDao.getTodayAsteroids(getToday())){
                    it.asDomainModel()
                }
            MenuOption.SHOW_SAVED ->
                Transformations.map(database.asteroidDao.getAsteroids()){
                    it.asDomainModel()
                }

        }
    }

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids =
                AsteroidApi.asteroidRetrofitService.getAsteroids(getToday(), getDayPlusWeek())
            val parseAsteroidJsonResult = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDao.insertAll(*parseAsteroidJsonResult.asDatabaseModel())
        }
    }
}