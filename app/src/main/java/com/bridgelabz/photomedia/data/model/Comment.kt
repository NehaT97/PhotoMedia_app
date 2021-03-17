package com.bridgelabz.photomedia.data.model

class Comment(
    val userName: String = "",
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
}