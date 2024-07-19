package com.bayee.cameras.photo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.util.Constant.PHOTOS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(photo: Photo)

    @Query("SELECT * FROM $PHOTOS_TABLE_NAME")
    fun getAllPhotos(): Flow<List<Photo>>

    @Query("DELETE FROM $PHOTOS_TABLE_NAME WHERE id = :photoId")
    suspend fun deletePhotoById(photoId: Int)
}