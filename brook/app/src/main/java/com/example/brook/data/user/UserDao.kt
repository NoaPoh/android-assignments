package com.example.brook.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user where id in (select userId from review)")
    fun getAllUsers(): LiveData<MutableList<User>>

    @Query("SELECT * FROM user where id = :userId")
    fun getUserById(userId: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}