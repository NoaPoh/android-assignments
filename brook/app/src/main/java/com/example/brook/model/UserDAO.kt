package com.example.brook.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("select * from users")
    fun getAll(): LiveData<MutableList<User?>?>?

    @Query("select * from users where email = :email")
    fun getUserById(email: String?): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User?)

    @Delete
    fun delete(user: User?)
}
