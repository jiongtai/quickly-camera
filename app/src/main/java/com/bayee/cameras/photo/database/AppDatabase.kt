package com.bayee.cameras.photo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.photo.dao.PhotoDao
import com.bayee.cameras.photo.dao.VideoDao

@Database(entities = [Photo::class, Video::class], version = 1, exportSchema = false)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: SQLDatabase? = null

        fun getDatabase(context: Context): SQLDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SQLDatabase::class.java,
                    "photo_database"
                )
                    // .allowMainThreadQueries() // 根据实际情况决定是否启用
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

