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
enum class MenuOption { SHOW_WEEK, SHOW_TODAY, SHOW_SAVED}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val pictureOfDayRepository = PictureOfDayRepository(database)

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val asteroidMenuOptions = MutableLiveData<MenuOption>()
    val asteroidList = Transformations.switchMap(asteroidMenuOptions) { menuOption ->
        asteroidRepository.getListAsteroids(menuOption)
    }

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    val pictureOfDay = pictureOfDayRepository.pictureOfDay

    init {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            asteroidMenuOptions.postValue(MenuOption.SHOW_WEEK)
            try {
                pictureOfDayRepository.refreshPictureOfDay()
                asteroidRepository.refreshAsteroids()
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun updateAsteroidFilter(option: MenuOption) {
        asteroidMenuOptions.postValue(option)
    }

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