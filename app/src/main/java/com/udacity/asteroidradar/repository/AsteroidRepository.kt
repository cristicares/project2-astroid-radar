package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository() {

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidApi.AsteroidRetrofitService.getAsteroids("2015-09-07", "2015-09-08")
            //val parseAsteroiJsonResult = parseAsteroidsJsonResult(JSONObject(asteroidList))
            //database.videoDao.insertAll(*playlist.asDatabaseModel())

            Log.i("call api", asteroids)
        }
    }
}