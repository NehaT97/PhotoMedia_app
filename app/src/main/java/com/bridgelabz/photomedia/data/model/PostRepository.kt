package com.bridgelabz.photomedia.data.model

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

 class PostRepository(): IPostRepository {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
    }

    override fun uploadImage(imageFileName:String,imageUri: Uri,listener:(Boolean) -> Unit) {
      /*  val user = firebaseAuth.currentUser
        val emaildid = user.email*/
       // Log.i("Required data:","${firebaseAuth.currentUser.email}")
        val fileRef = storageRef.child("Post_Collection$imageFileName")
        fileRef.putFile(imageUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    /*val myOriganalUri = it.toString()
                    uploadPostToFIrestore(myOriganalUri)*/
                    Log.d("Image Upload","onSuccess: Uploaded Image Uri is : ${it.toString()}")
                }

            }.addOnFailureListener {
                Log.d("Image Upload","onFailed: Uploaded Image Uri is : ${it.toString()}")
            }
    }




}