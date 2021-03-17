package com.bridgelabz.photomedia.ui.editProfilePage.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository

class EditProfileViewModel: ViewModel() {

     val profileDetailsStoreStatus = MutableLiveData<Boolean>()
    private val userRepository:IUserRepository = UserRepository()

     fun saveUserProfileDetails(
         firstNameEditTextValue: String,
         lastNameEditTextValue: String,
         bioValue: String
     ) {
        userRepository.updateUserDetails(firstNameEditTextValue,
            lastNameEditTextValue,bioValue){

        }
    }
}