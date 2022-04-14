package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val asteroidRepository = AsteroidRepository()

    init {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
            } catch (e: Exception) {
                e.message?.let { Log.e("main view model", it) }
            }
        }
    }
}