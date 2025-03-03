package com.example.Brook.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.Brook.BrookApplication
import com.example.Brook.data.review.Review
import com.example.Brook.data.review.ReviewDao
import com.example.Brook.data.user.User
import com.example.Brook.data.user.UserDTO


@Database(entities = [Review::class, User::class], version = 7, exportSchema = true)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun userDto(): UserDTO
}

object AppLocalDatabase {
    val db: AppLocalDbRepository by lazy {
        val context = BrookApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "Brook"
        ).fallbackToDestructiveMigration()
            .build()
    }
}