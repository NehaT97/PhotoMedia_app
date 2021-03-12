package com.bridgelabz.photomedia.data

import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository():IUserRepository{
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    init {
        firebaseService()
    }

    private fun firebaseService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
    }


    override fun registerUserToFirebase(user: User){
        firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if (it.isSuccessful) addUserDetailsToFirestore(user)
        }
    }

    override fun authenticateUserByLogin(email: String, password:String, listener:(Boolean) -> Unit){
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

    override fun googleLogin(idToken:String,listener:(Boolean) -> Unit){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            listener(it.isSuccessful)
        }

    }

    override fun addUserDetailsToFirestore(user: User) {
    }
}