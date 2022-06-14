package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidRepository = AsteroidRepository(database)
        val pictureOfDayRepository = PictureOfDayRepository(database)

        return try {
            asteroidRepository.refreshAsteroids()
            pictureOfDayRepository.refreshPictureOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}