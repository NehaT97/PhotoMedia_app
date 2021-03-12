package com.bridgelabz.photomedia.data

import com.bridgelabz.photomedia.data.model.User

interface IUserRepository {

    fun addUserDetailsToFirestore(user: User,listener: (Boolean) -> Unit)
    fun authenticateUserByLogin(email: String, password: String, listener: (Boolean) -> Unit)
    fun googleLogin(idToken: String,listener: (Boolean) -> Unit)
    fun registerUserToFirebase(user: User, listener: (Boolean) -> Unit)
}