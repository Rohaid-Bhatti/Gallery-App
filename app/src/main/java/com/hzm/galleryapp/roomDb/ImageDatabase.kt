package com.hzm.galleryapp.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hzm.galleryapp.models.Hit

@Database(entities = [Hit::class, FavoriteEntity::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {

    abstract fun imageDao() : ImageDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDatabase(context: Context): ImageDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        ImageDatabase::class.java,
                        "imageDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}