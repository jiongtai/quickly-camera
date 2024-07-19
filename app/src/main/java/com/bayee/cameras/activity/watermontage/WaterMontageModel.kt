package com.bayee.cameras.activity.watermontage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.main.MainActivity
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.database.SQLDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class WaterMontageModel (private val database: SQLDatabase) : BaseViewModel() {


    private val _allPhotos = MutableLiveData<List<Photo>>()
    val allPhotos: LiveData<List<Photo>> get() = _allPhotos

    init {
        fetchPhotosFromDatabase()
    }

    private fun fetchPhotosFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            database.photoDao().getAllPhotos().collectLatest {
                _allPhotos.postValue(it)
            }
        }
    }


}
