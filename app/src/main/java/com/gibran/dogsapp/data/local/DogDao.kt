package com.gibran.dogsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DogDao {

    @Query("SELECT * FROM dogs")
    suspend fun getAllDogs(): List<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dogs: List<DogEntity>)
}
