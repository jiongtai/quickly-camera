package com.bayee.cameras.photo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.util.Constant.VIDEOS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideo(video: Video)

    @Query("SELECT * FROM $VIDEOS_TABLE_NAME")
    fun getAllVideos(): Flow<List<Video>>

    @Query("DELETE FROM $VIDEOS_TABLE_NAME WHERE id = :videoId")
    suspend fun deleteVideoById(videoId: Int)
}
