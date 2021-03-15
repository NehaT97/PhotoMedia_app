package com.bridgelabz.photomedia.data.model

import android.net.Uri

class Post(
    val userId: String = "",
    val postId: String = "",
    val userName: String = "",
    val userProfileImageUri: String = "",
    var currentPostImageUri: String ="",
    var likedBy: List<String> = ArrayList(),
    var comments: List<Comment> = ArrayList(),
    val createdAt: Long = System.currentTimeMillis()
) {
}