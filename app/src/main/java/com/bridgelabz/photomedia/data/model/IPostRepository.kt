package com.bridgelabz.photomedia.data.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface IPostRepository {


    fun getUser(): FirebaseUser?
    fun uploadImage(imageFileName:String,imageUri: Uri, listener:(Boolean) -> Unit)
    fun UploadPostToFirestore(post: Post,listener: (Boolean) -> Unit)
}