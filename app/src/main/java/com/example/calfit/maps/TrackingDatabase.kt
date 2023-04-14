package com.example.calfit.maps

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1
@Database(entities = [TrackingEntity::class], version = 1, exportSchema = false)
abstract class TrackingDatabase : RoomDatabase() {

    // 3
    abstract fun getTrackingDao(): TrackingDao


    companion object {
        @Volatile private var instance: TrackingDatabase? = null

        fun getDatabase(context: Context): TrackingDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, TrackingDatabase::class.java, "tracking_data")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}