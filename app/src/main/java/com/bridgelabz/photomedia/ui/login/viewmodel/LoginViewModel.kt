package com.bridgelabz.photomedia.ui.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository
import com.bridgelabz.photomedia.data.externalApis.UserApiService
import com.bridgelabz.photomedia.data.model.User

class LoginViewModel : ViewModel() {
    val loginSuccessFull = MutableLiveData<Boolean>()
    val user = MutableLiveData<User>()

    private val userRepository: IUserRepository = UserRepository()
    private val userApiService: UserApiService = UserApiService()


    fun loginUser(email: String, password: String): Unit {
        userRepository.authenticateUserByLogin(email, password) {
            Log.i("User logeed in view model", "$it")
            loginSuccessFull.value = true

        }
    }

    fun loginUserByGoogle(idToken: String): Unit{
        userRepository.googleLogin(idToken){
            loginSuccessFull.value = it
        }
    }
}