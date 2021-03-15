package com.bridgelabz.photomedia.data.model

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PostRepository() : IPostRepository {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    var postUri: Uri? = null

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
    }

    override fun uploadImage(imageFileName: String, imageUri: Uri, listener: (Uri?) -> Unit) {
        Log.i("Original Uri", "$imageUri")
        val fileRef = storageRef.child("Post_Collection$imageFileName")
        fileRef.putFile(imageUri).addOnCompleteListener {
            if (it.isSuccessful) {
                fileRef.downloadUrl.addOnCompleteListener { ref ->
                    if (ref.isSuccessful) {
                        listener(ref.result!!)
                    } else if (ref.isCanceled) {
                        Log.e("Download URI Error", "${ref.exception}")
                        listener(null)
                    }
                }
            } else if (it.isCanceled) {
                Log.e("Download URI Error", "${it.exception}")
                listener(null)
            }
        }
    }

    override fun uploadPost(post: Post, listener: (Boolean) -> Unit) {
        fireStore.collection("POST_COLLECTION").document(post.postId).set(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(it.isSuccessful)
                } else if (it.isCanceled) {
                    Log.e("Exception caught", "${it.exception?.message}")
                }
                listener(false)
            }
    }

    override fun fetchAllPostByUserIds(userIds: List<String>, listener: (List<Post>) -> Unit) {
        fireStore.collection("POST_COLLECTION").whereIn("userId", userIds).orderBy("createdAt",
            Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val posts :List<Post> = ArrayList(it.result!!.toObjects(Post::class.java))
                    Log.i("Fetched Documents", "$posts")
                    listener(posts)
                } else if (it.isCanceled) {
                    listener(ArrayList())
                    Log.e("Exception caught", "${it.exception}")
                }
            }
    }
}