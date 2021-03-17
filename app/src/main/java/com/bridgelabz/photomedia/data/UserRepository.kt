package com.bridgelabz.photomedia.data

import android.util.Log
import com.bridgelabz.photomedia.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository() : IUserRepository {

    private val USERNAME: String = "userName"
    private val FIRSTNAME: String = "firstName"
    private val LASTNAME: String = "lastName"
    private val BIO: String = "bio"

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

   override fun updateUserDetails(
       firstname: String,
       lastname: String,
       bio: String,
       listener: (Boolean) -> Unit
   ) {
        fireStore.collection("USER_COLLECTION").document(firebaseAuth?.currentUser.uid)
            .update("firstName",firstname,
                    "lastName",lastname,
            "bio",bio).addOnCompleteListener {
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

    override fun fetchAllUsers(listener: (List<User>?) -> Unit) {
        fireStore.collection("USER_COLLECTION").get()
            .addOnCompleteListener {
            if (it.isSuccessful){
                val users:List<User> = it.result!!.toObjects(User::class.java)
                listener(users)
            }
            else if (it.isCanceled){
                listener(listOf())
            }
        }

    }

    override fun addFollower(
        currentLoggedInUserId: String,
        followToUserId:String,
        listener: (Boolean) -> Unit
    ){
        fireStore.collection("USER_COLLECTION").document(followToUserId)
            .update("followers",FieldValue.arrayUnion(currentLoggedInUserId))
            .addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

    override fun followUser(
        currentLoggedInUserId: String,
        followedToUserId: String,
        listener: (Boolean) -> Unit
        ){
        fireStore.collection("USER_COLLECTION").document(currentLoggedInUserId)
            .update("following",FieldValue.arrayUnion(followedToUserId))
            .addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

    override fun removeFollower(
        currentLoggedInUserId: String,
        followToUserId: String,
        listener: (Boolean) -> Unit
        ){
        fireStore.collection("USER_COLLECTION")
            .document(followToUserId)
            .update("followers",FieldValue.arrayRemove(currentLoggedInUserId))
            .addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

    override fun unfollowUser(
        currentLoggedInUserId: String,
        followedToUserId: String,
        listener: (Boolean) -> Unit
    ){
        fireStore.collection("USER_COLLECTION")
            .document(currentLoggedInUserId)
            .update("following",FieldValue.arrayRemove(followedToUserId))
            .addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

}