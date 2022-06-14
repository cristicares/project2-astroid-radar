package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from asteroid")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: AsteroidEntity)
}

@Dao
interface PictureOfDayDao {
    @Query("select * from pictureOfDay")
    fun getPictureOfDay(): LiveData<PictureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(pictureOfDay: PictureEntity)
}

@Database(
    entities = [AsteroidEntity::class, PictureEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AsteroidRadarDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidRadarDatabase

fun getDatabase(context: Context): AsteroidRadarDatabase {
    synchronized(AsteroidRadarDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidRadarDatabase::class.java,
                "AsteroidRadar"
            ).build()
        }
    }
    return INSTANCE
}
