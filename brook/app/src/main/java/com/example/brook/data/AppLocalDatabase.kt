package com.example.brook.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.brook.BrookApplication
import com.example.brook.data.review.Review
import com.example.brook.data.review.ReviewDao
import com.example.brook.data.user.User
import com.example.brook.data.user.UserDao


@Database(entities = [Review::class, User::class], version = 7, exportSchema = false)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun userDto(): UserDao
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