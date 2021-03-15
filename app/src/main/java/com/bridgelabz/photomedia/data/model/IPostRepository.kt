package com.bridgelabz.photomedia.data.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface IPostRepository {
    fun uploadImage(imageFileName: String, imageUri: Uri, listener: (Uri?) -> Unit)
    fun uploadPost(post: Post, listener: (Boolean) -> Unit)
    fun fetchAllPostByUserIds(userIds: List<String>, listener: (List<Post>) -> Unit)
}