package com.bayee.cameras.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bayee.cameras.photo.database.SQLDatabase


class RecordViewModelFactory(private val db: SQLDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            return RecordViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}