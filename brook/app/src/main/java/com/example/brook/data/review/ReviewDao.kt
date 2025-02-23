package com.example.brook.data.review

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewDao {
    @Query("SELECT * FROM review order by timestamp desc")
    fun getAll(): LiveData<MutableList<Review>>

    @Query("SELECT * FROM review WHERE userId = :userId order by timestamp desc")
    fun getReviewsByUserId(userId: String): LiveData<MutableList<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(review: Review)

    @Delete
    fun delete(review: Review)
}