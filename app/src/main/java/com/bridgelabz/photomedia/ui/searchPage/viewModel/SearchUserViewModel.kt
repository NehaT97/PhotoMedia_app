package com.bridgelabz.photomedia.ui.searchPage.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridgelabz.photomedia.data.IUserRepository
import com.bridgelabz.photomedia.data.UserRepository
import com.bridgelabz.photomedia.data.model.User

class SearchUserViewModel : ViewModel() {

    var users = MutableLiveData<List<User>>()
    var isFollowSuccessFul = MutableLiveData<Boolean>()
    var isUnfollowSuccessFul = MutableLiveData<Boolean>()

    private val userRepository: IUserRepository = UserRepository()

    fun fetchAllUsers() {
        userRepository.fetchAllUsers {
            users.value = it
        }
    }

    fun followUser(followersUserId: String, followingsUserId: String) {
        userRepository.followUser(followersUserId, followingsUserId) { result ->
            if (result) {
                userRepository.addFollower(followersUserId, followingsUserId) {
                    isFollowSuccessFul.value = it
                    fetchAllUsers()
                }
            } else {
                isFollowSuccessFul.value = result
            }
        }
    }

    fun unfollowUser(followersUserId: String, followingsUserId: String) {
        userRepository.unfollowUser(followersUserId, followingsUserId) { result ->
            if (result) {
                userRepository.removeFollower(followersUserId, followingsUserId) {
                    isUnfollowSuccessFul.value = it
                    fetchAllUsers()
                }
            } else {
                isUnfollowSuccessFul.value = result
            }
        }
    }
}