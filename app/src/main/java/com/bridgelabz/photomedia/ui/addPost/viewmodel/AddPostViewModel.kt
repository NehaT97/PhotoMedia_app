package com.bridgelabz.photomedia.ui.addPost.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.model.IPostRepository
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.PostRepository

class AddPostViewModel : ViewModel() {
    val imageUploadedStatus = MutableLiveData<Boolean>()
    val post = MutableLiveData<Post>()

    private val postRepository: IPostRepository = PostRepository()

    fun uploadImage(imageFileName: String, selectedImageUri: Uri){
        postRepository.uploadImage(imageFileName,selectedImageUri){
            imageUploadedStatus.value = it
        }
    }

}