package com.example.brook.model

import android.content.Context
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brook.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.lang.Exception
import java.util.HashMap

@Entity(tableName = "posts")
class Post {
    @PrimaryKey
    private var id: String? = null
    @JvmField
    var title: String? = null
    @JvmField
    var dishImg: String? = null
    @JvmField
    var countryName: String? = null
    @JvmField
    var fullRecipe: String? = null
    @JvmField
    var time: Long = 0

    @JvmField
    @Embedded
    var user: User? = null
    @JvmField
    var lastUpdated: Long? = null

    constructor()

    constructor(
        id: String,
        title: String?,
        user: User,
        dishImg: String?,
        countryName: String?,
        time: Long,
        fullRecipe: String?
    ) {
        this.id = id
        this.title = title
        this.user = user
        this.dishImg = dishImg
        this.countryName = countryName
        this.time = time
        this.fullRecipe = fullRecipe
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getId(): String {
        return id!!
    }

    fun getUserId(): String? {
        return user!!.getEmail()
    }

    fun getTitle(): String {
        return title!!
    }

    fun getUser(): User {
        return user!!
    }

    fun getDishImg(): String? {
        return dishImg
    }

    fun getCountryName(): String? {
        return countryName
    }

    fun getTime(): Long {
        return time
    }

    fun getFullRecipe(): String? {
        return fullRecipe
    }

    fun setFullRecipe(fullRecipe: String?) {
        this.fullRecipe = fullRecipe
    }

    fun toJson(): MutableMap<String?, Any?> {
        val json: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        json.put("id", getId())
        json.put("title", getTitle())
        json.put("user", getUser().toJson())
        json.put("dishImg", getDishImg())
        json.put("countryName", getCountryName())
        json.put("time", getTime())
        json.put(LAST_UPDATED, FieldValue.serverTimestamp())
        json.put("fullRecipe", getFullRecipe())
        return json
    }

    override fun toString(): String {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dishImg='" + dishImg + '\'' +
                ", countryName='" + countryName + '\'' +
                ", time=" + time +
                ", user=" + user +
                ", lastUpdated=" + lastUpdated +
                '}'
    }

    companion object {
        const val COLLECTION: String = "posts"
        const val LAST_UPDATED: String = "lastUpdated"
        const val LOCAL_LAST_UPDATED: String = "posts_local_last_update"

        fun fromJson(json: MutableMap<String?, Any?>): Post {
            val id = json.get("id") as String?
            val title = json.get("title") as String?
            val dishImg = json.get("dishImg") as String?
            val countryName = json.get("countryName") as String?
            val time = json.get("time") as Long?
            val fullRecipe = json.get("fullRecipe") as String?

            val userJson = json.get("user")
            var user = User()
            if (userJson is HashMap<*, *>) {
                user = User.fromJson(userJson as HashMap<String?, Any?>)
            }

            val post = Post(
                id!!,
                title,
                user,
                dishImg,
                countryName,
                if (time != null) time else 0L,
                fullRecipe
            )
            try {
                val newTime = json.get(LAST_UPDATED) as Timestamp?
                if (newTime != null) {
                    setLocalLastUpdated(newTime.getSeconds())
                }
            } catch (e: Exception) {
            }
            return post
        }

        fun getLocalLastUpdate(): Long {
            val sharedPref =
                MyApplication.getMyContext()!!.getSharedPreferences("TAG", Context.MODE_PRIVATE)
            return sharedPref.getLong(LOCAL_LAST_UPDATED, 0)
        }

        fun setLocalLastUpdated(newTime: Long) {
            MyApplication.getMyContext()!!.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit()
                .putLong(LOCAL_LAST_UPDATED, newTime).apply()
        }
    }
}
