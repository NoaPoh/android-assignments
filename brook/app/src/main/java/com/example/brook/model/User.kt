package com.example.brook.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.HashMap

@Entity(tableName = "users")
class User {
    @JvmField
    @PrimaryKey
    var email: String
    @JvmField
    var userName: String?
    @JvmField
    var avatarUrl: String?

    constructor() {
        this.email = ""
        this.userName = ""
        this.avatarUrl = ""
    }

    constructor(email: String, userName: String?, userImg: String?) {
        this.email = email
        this.avatarUrl = userImg
        this.userName = userName
    }

    fun toJson(): MutableMap<String?, Any?> {
        val json: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        json.put(EMAIL, getEmail())
        json.put(USERNAME, getUserName())
        json.put(AVATARURL, getAvatarUrl())
        return json
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getAvatarUrl(): String? {
        return avatarUrl
    }

    fun setAvatarUrl(avatarUrl: String?) {
        this.avatarUrl = avatarUrl
    }

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String?) {
        this.userName = userName
    }

    companion object {
        const val USERNAME: String = "userName"
        const val EMAIL: String = "email"
        const val AVATARURL: String = "avatarUrl"
        const val COLLECTION: String = "users"

        fun fromJson(json: MutableMap<String?, Any?>): User {
            val email = json.get(EMAIL) as String?
            val userName = json.get(USERNAME) as String?
            val avatarUrl = json.get(AVATARURL) as String?
            val user = User(email!!, userName, avatarUrl)
            return user
        }
    }
}
