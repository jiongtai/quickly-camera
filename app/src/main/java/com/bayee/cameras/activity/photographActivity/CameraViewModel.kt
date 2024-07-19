package com.bayee.cameras.activity.photographActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.api.bean.receive.UserInfoResponse
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.main.MainActivity
import com.bayee.cameras.main.MainViewModel
import com.bayee.cameras.main.MainViewModel.Companion
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.photo.database.SQLDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class CameraViewModel(private val database: SQLDatabase) : BaseViewModel() {


    private val _allPhotos = MutableLiveData<List<Photo>>()
    val allPhotos: LiveData<List<Photo>> get() = _allPhotos

    private val _allVideos = MutableLiveData<List<Video>>()
    val allVideos: LiveData<List<Video>> get() = _allVideos

    private val _insertCallBack = MutableLiveData<Int>()
    val insertCallBack: LiveData<Int> get() = _insertCallBack

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

    fun fetchVideosFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            database.videoDao().getAllVideos().collectLatest {
                _allVideos.postValue(it)
            }
        }
    }

    fun insertPhoto(photo: Photo) {
        CoroutineScope(Dispatchers.IO).launch {
            database.photoDao().insertPhoto(photo)
            viewModelScope.launch {
                _insertCallBack.value = 1
            }
        }
    }

    fun insertVideo(video: Video ) {
        CoroutineScope(Dispatchers.IO).launch {
            database.videoDao().insertVideo(video)
            viewModelScope.launch {
                _insertCallBack.value = 1
            }
        }
    }

    fun decrFreeTime() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = DefaultNetworkRepository.decrFreeTime()
            if (result.code == HttpURLConnection.HTTP_OK){
                Log.d(TAG, "decrFreeTime: ${result.toString()}")
                MainActivity.instance.loadingUserInfo()
            }else{
                Log.d(TAG, "decrFreeTime: ")
            }
        }
    }


    companion object{
        const val TAG = "CameraViewModel"
    }

}












