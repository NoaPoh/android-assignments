package com.example.brook.model

import androidx.room.Embedded
import androidx.room.Relation

class PostCard(post: Post?, user: User?) {
    @Embedded
    var post: Post?

    @Relation(parentColumn = "userId", entityColumn = "id")
    var user: User?

    init {
        this.post = post
        this.user = user
    }

    override fun toString(): String {
        return "PostCard{" +
                "post=" + post +
                ", user=" + user +
                '}'
    }
}
