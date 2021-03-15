package com.bridgelabz.photomedia.ui.searchPage.viewModel

import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository
import com.google.firebase.firestore.auth.User

class SearchUserViewModel : ViewModel() {

    private val userRepository :IUserRepository = UserRepository()

    private fun fetchAllUsersByUserId(){
        userRepository.fetchAllUsersByUserId(){

        }

    }
}