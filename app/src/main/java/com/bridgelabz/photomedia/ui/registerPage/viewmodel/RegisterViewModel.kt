package com.bridgelabz.photomedia.ui.registerPage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository
import com.bridgelabz.photomedia.data.externalApis.UserApiService
import com.bridgelabz.photomedia.data.model.User

class RegisterViewModel : ViewModel() {

    val registerSuccessFul = MutableLiveData<Boolean>()
    private val userApiService: UserApiService = UserApiService()
    private val userRepository:IUserRepository = UserRepository()

    fun registerUser(user: User): Unit {
        userRepository.registerUserToFirebase(user){
            registerSuccessFul.value = it
        }
    }
}