package com.bayee.cameras.activity.photographActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bayee.cameras.photo.database.SQLDatabase


class CameraViewModelFactory(private val db: SQLDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}