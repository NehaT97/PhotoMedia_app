package com.bridgelabz.photomedia.data.model

import android.net.Uri

interface IPostRepository {
    fun uploadImage(imageFileName:String,imageUri: Uri, listener:(Boolean) -> Unit)
}