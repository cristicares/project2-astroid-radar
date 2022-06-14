package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus {LOADING, ERROR, DONE}
enum class MenuOption { SHOW_TODAY, SHOW_NEXT_WEEK, SHOW_SAVED, SHOW_DEFAULT}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val pictureOfDayRepository = PictureOfDayRepository(database)

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        //getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                pictureOfDayRepository.refreshPictureOfDay()
                asteroidRepository.refreshAsteroids()
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

    val pictureOfDay = pictureOfDayRepository.pictureOfDay
    val asteroidList = asteroidRepository.asteroids

    /*
    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    fun updateFilter(filter: MarsApiFilter){
        getMarsRealEstateProperties(filter)
    }*/

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}