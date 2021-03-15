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
}