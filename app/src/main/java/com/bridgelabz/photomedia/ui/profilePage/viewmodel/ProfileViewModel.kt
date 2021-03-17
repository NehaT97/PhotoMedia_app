package com.bridgelabz.photomedia.ui.profilePage.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository
import com.bridgelabz.photomedia.data.model.IPostRepository
import com.bridgelabz.photomedia.data.model.PostRepository

class ProfileViewModel : ViewModel() {
    val uploadedProfileImageUri = MutableLiveData<Uri>()
    val isImageUploaded = MutableLiveData<Boolean>()

    private val postRepository: IPostRepository = PostRepository()
    private val userRepository: IUserRepository = UserRepository()

    fun uploadProfileImage(userId:String, imageFileName: String, selectedImageUri: Uri){
        postRepository.uploadImage(imageFileName, selectedImageUri){
            uploadedProfileImageUri.value = it
            if (it != null) {
                val profileImageUrl = it.toString()
                updateProfileUrlInUser(userId, profileImageUrl)
            }
        }
    }

    fun updateProfileUrlInUser(userId:String, uploadedImageUrl:String) {
        userRepository.setProfileImage(uploadedImageUrl, userId) {
            isImageUploaded.value = it
        }
    }
}