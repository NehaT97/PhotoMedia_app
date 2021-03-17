package com.bridgelabz.photomedia.ui.loginPage.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository
import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    val loginSuccessFull = MutableLiveData<Boolean>()
    val loggedAuthUser = MutableLiveData<FirebaseUser>()
    val loggedUser = MutableLiveData<User>()

    private val userRepository: IUserRepository = UserRepository()
    //private val userApiService: UserApiService = UserApiService()


    fun currentAuthUser(): FirebaseUser? {
        return userRepository.getCurrentAuthUser()
    }

    fun loginUser(email: String, password: String): Unit {
        userRepository.loginByEmailIdAndPassword(email, password) {
            loginSuccessFull.value = it != null
            loggedAuthUser.value = it
        }
    }

    fun loginUserByGoogle(idToken: String): Unit {
        userRepository.loginByGoogle(idToken) {
            loginSuccessFull.value = it != null
            loggedAuthUser.value = it
        }
    }

    fun fetchUserByUserId(userId: String): Unit {
        userRepository.fetchUserByUserId(userId) {
            Log.i("Fetch User By user Id", "$it")
            loggedUser.value = it
        }
    }
}