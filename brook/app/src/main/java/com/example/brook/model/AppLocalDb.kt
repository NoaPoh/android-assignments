package com.example.brook.model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.brook.MyApplication

@Database(entities = [User::class, Post::class], version = 83)
public abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun PostDao(): PostDAO?
    abstract fun UserDao(): UserDAO?
}

object AppLocalDb {
    fun getAppDb(): AppLocalDbRepository {
        return Room.databaseBuilder<AppLocalDbRepository>(
            MyApplication.getMyContext(),
            AppLocalDbRepository::class.java,
            "dbFileName.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

