package com.bridgelabz.photomedia.data

import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class UserRepository():IUserRepository{
    private lateinit var firebaseAuth: FirebaseAuth

    init {
        firebaseService()
    }

    private fun firebaseService() {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun authenticateUserByLogin(email: String, password:String, listener:(Boolean) -> Unit){
    }

    override fun registerUser(user: User){
        firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if (it.isSuccessful) addUserDetailsToFirestore(user)
        }
    }

    override fun addUserDetailsToFirestore(user: User) {
    }

    override fun googleLogin(idToken:String,listener:(Boolean) -> Unit){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            listener(it.isSuccessful)
        }

    }
}