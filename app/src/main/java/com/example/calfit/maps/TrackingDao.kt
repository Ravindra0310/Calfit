package com.example.calfit.maps

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackingDao {
    // 1
    @Query("SELECT * FROM tracking_entity")
    fun getAllTrackingEntities(): LiveData<List<TrackingEntity>>

    // 2
    @Query("SELECT SUM(distanceTravelled) FROM tracking_entity")
    fun getTotalDistanceTravelled(): LiveData<Float?>

    // 3
    @Query("SELECT * FROM tracking_entity ORDER BY timestamp DESC LIMIT 1")
    fun getLastTrackingEntity(): LiveData<TrackingEntity?>

    // 4
    @Query("SELECT * FROM tracking_entity ORDER BY timestamp DESC LIMIT 1")
     fun getLastTrackingEntityRecord(): LiveData<TrackingEntity?>

    // 5
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(trackingEntity: TrackingEntity?)

    // 6
    @Query("DELETE FROM tracking_entity")
     fun deleteAll()

    // 7
    @Query("SELECT * FROM tracking_entity")
     fun getAllTrackingEntitiesRecord(): List<TrackingEntity>
}