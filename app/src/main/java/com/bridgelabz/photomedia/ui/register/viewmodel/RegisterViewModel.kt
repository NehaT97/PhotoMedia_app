package com.bridgelabz.photomedia.ui.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.externalApis.UserApiService
import com.bridgelabz.photomedia.data.model.RegisterDTO

class RegisterViewModel : ViewModel() {

    val registerSuccessFul = MutableLiveData<Boolean>()
    private val userApiService: UserApiService = UserApiService()

    fun registerUser(registerDTO: RegisterDTO): Unit {
        userApiService.register(registerDTO) {
            registerSuccessFul.value = it != null
        }
    }
}