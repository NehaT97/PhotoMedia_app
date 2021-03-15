package com.bridgelabz.photomedia.ui.addPostPage.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.model.IPostRepository
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.PostRepository

class PostViewModel : ViewModel() {
    val uploadedPostImageUri = MutableLiveData<Uri>()
    val postStatus = MutableLiveData<Boolean>()
    val userPosts = MutableLiveData<List<Post>>()
    private val postRepository: IPostRepository = PostRepository()

    fun uploadImageFirebaseStorage(post: Post, imageFileName: String, selectedImageUri: Uri){
        postRepository.uploadImage(imageFileName,selectedImageUri){
            uploadedPostImageUri.value = it
            if (it != null) {
                post.currentPostImageUri = it.toString()
                uploadPostToFirestore(post)
            }
        }
    }

    private fun uploadPostToFirestore(post:Post){
       postRepository.uploadPost(post){
           postStatus.value = it
       }
    }

    fun fetchAllPostByUserId(userIds : List<String>){
       postRepository.fetchAllPostByUserIds(userIds) {
            userPosts.value = it
       }
    }
}