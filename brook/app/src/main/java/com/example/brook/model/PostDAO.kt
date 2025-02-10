package com.example.brook.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDAO {
    @Query("select * from posts")
    fun getAll(): LiveData<MutableList<Post?>?>?

    @Query("select * from posts where email = :email")
    fun getPostsByEmail(email: String?): LiveData<MutableList<Post?>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg Posts: Post?)

    @Delete
    fun delete(Post: Post?)
}
