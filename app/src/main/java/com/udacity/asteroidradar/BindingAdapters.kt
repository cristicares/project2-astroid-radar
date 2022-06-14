package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("statusLoading")
fun bindStatus(bar: ProgressBar, status: AsteroidApiStatus?) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            bar.visibility = View.VISIBLE
        }
        AsteroidApiStatus.DONE -> {
            bar.visibility = View.GONE
        }
        AsteroidApiStatus.ERROR -> {
            bar.visibility = View.GONE

        }
    }
}

/**
 * Binding adapter used to display images from URL using Glide
*/
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url.let {  Picasso.get().load(url)
        .placeholder(R.drawable.placeholder_picture_of_day)
        .error(R.drawable.placeholder_picture_of_day).into(imageView)
    }

}
