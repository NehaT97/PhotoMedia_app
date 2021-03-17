package com.bridgelabz.photomedia.data

import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseUser

interface IUserRepository {

    fun addUserDetailsToFirestore(user: User,listener: (Boolean) -> Unit)
    fun loginByEmailIdAndPassword(email: String, password: String, listener: (FirebaseUser?) -> Unit)
    fun loginByGoogle(idToken: String, listener: (FirebaseUser?) -> Unit)
    fun registerUserToFirebase(user: User, listener: (Boolean) -> Unit)
    fun fetchUserByUserId(userId: String, listener: (User?) -> Unit)
    fun getCurrentAuthUser(): FirebaseUser?
    fun setProfileImage(imageUrl: String, userId: String, listener: (Boolean) -> Unit)
    fun updateUserDetails(
        firstName: String,
        lastName: String,
        bio: String,
        listener: (Boolean) -> Unit
    )

    fun fetchAllUsers(listener: (List<User>?) -> Unit)
    fun addFollower(
        currentLoggedInUserId: String,
        followToUserId: String,
        listener: (Boolean) -> Unit
    )

    fun followUser(
        currentLoggedInUserId: String,
        followedToUserId: String,
        listener: (Boolean) -> Unit
    )

    fun removeFollower(
        currentLoggedInUserId: String,
        followToUserId: String,
        listener: (Boolean) -> Unit
    )

    fun unfollowUser(
    currentLoggedInUserId: String,
    followedToUserId: String,
    listener: (Boolean) -> Unit
    )
}