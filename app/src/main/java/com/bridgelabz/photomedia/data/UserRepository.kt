package com.bridgelabz.photomedia.data

import android.util.Log
import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository() : IUserRepository {
    private val EMAIL: String = "email"
    private val USERNAME: String = "userName"
    private val FIRSTNAME: String = "firstName"
    private val LASTNAME: String = "lastName"
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    init {
        firebaseService()
    }


    private fun firebaseService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
    }


    override fun registerUserToFirebase(user: User, listener: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) addUserDetailsToFirestore(user, listener)

            }
    }

    override fun authenticateUserByLogin(
        email: String,
        password: String,
        listener: (Boolean) -> Unit
    ) {
        Log.i("currentUser1:", firebaseAuth.currentUser.email)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

    override fun googleLogin(idToken: String, listener: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            listener(it.isSuccessful)
        }

    }

    override fun addUserDetailsToFirestore(userDetails: User, listener: (Boolean) -> Unit) {
        val user: MutableMap<String, Any> = HashMap()
        user[EMAIL] = userDetails.email
        user[USERNAME] = userDetails.userName
        user[FIRSTNAME] = userDetails.firstName
        user[LASTNAME] = userDetails.lastName
        fireStore.collection("USER_COLLECTION").document(firebaseAuth?.currentUser.uid)
            .set(user).addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }
}