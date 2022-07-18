package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.PictureEntity
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

fun List<Asteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun PictureOfDay.asDatabaseModel(): PictureEntity {
    return (PictureEntity(
        url = this.url,
        mediaType = this.mediaType,
        tittle = this.title
    ))
}