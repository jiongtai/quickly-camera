package com.bayee.cameras.activity.watermontage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bayee.cameras.photo.database.SQLDatabase


class WaterMontageModelFactory(private val db: SQLDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaterMontageModel::class.java)) {
            return WaterMontageModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}