package com.bayee.cameras.record

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.photo.database.SQLDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel(private val database: SQLDatabase): BaseViewModel() {

    private val _deletionResult = MutableLiveData<Boolean>()
    val deletionResult: LiveData<Boolean>
        get() = _deletionResult

    fun deletePhotos(datas: MutableList<Photo>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (photo in datas) {
                    database.photoDao().deletePhotoById(photo.id)
                }
                _deletionResult.postValue(true) // 所有删除操作成功完成
            } catch (e: Exception) {
                _deletionResult.postValue(false) // 发生异常，删除失败
                Log.e("RecordViewModel", "Error deleting photos: ${e.message}")
            }
        }
    }

    fun deleteVideos(datas: MutableList<Video>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (video in datas) {
                    database.videoDao().deleteVideoById(video.id)
                }
                _deletionResult.postValue(true) // 所有删除操作成功完成
            } catch (e: Exception) {
                _deletionResult.postValue(false) // 发生异常，删除失败
                Log.e("RecordViewModel", "Error deleting photos: ${e.message}")
            }
        }
    }


}







