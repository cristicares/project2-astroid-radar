package com.udacity.asteroidradar.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
) : Parcelable