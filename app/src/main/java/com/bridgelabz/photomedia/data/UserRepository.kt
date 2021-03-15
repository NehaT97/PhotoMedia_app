package com.bridgelabz.photomedia.data

import android.util.Log
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository() : IUserRepository {
    private val EMAIL: String = "email"
    private val USERID: String = "userid"
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

    override fun getCurrentAuthUser(): FirebaseUser? {
        Log.i("Current User", "${firebaseAuth.currentUser}")
        return firebaseAuth.currentUser
    }

    override fun registerUserToFirebase(user: User, listener: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful)  {
                    val authUser = it.result!!.user
                    user.userId = authUser.uid
                    addUserDetailsToFirestore(user, listener)
                } else if (it.isCanceled) {
                    Log.e("Register User", "${it.exception?.message}", it.exception)
                }
                listener(it.isSuccessful)
            }
    }

    override fun loginByEmailIdAndPassword(
        email: String,
        password: String,
        listener: (FirebaseUser?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(it.result!!.user)
                } else {
                    listener(null)
                }
            }
    }

    override fun loginByGoogle(idToken: String, listener: (FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(it.result!!.user)
            } else {
                listener(null)
            }
        }
    }

    override fun addUserDetailsToFirestore(userDetails: User, listener: (Boolean) -> Unit) {
        userDetails.password = ""
        fireStore.collection("USER_COLLECTION").document(firebaseAuth?.currentUser.uid)
            .set(userDetails).addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

    override fun fetchUserByUserId(userId: String, listener: (User?) -> Unit) {
        fireStore.collection("USER_COLLECTION").document(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                listener(it.result!!.toObject(User::class.java))
            } else {
                listener(null)
            }
        }
    }

    override fun setProfileImage(imageUrl: String, userId: String, listener: (Boolean) -> Unit) {
        fireStore.collection("USER_COLLECTION").document(userId)
            .update("profileImageUrl", imageUrl).addOnCompleteListener {
                listener(it.isSuccessful)
        }
    }

    override fun fetchAllUsersByUserId(listener: (List<User>) -> Unit) {
        TODO("Not yet implemented")
    }


}