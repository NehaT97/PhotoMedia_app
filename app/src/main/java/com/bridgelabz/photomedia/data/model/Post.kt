package com.bridgelabz.photomedia.data.model

import android.net.Uri

class Post
    (val userId:String = "",
     val postId:String = "",
     val imageUri:Uri,
     val imageFileName:String = "",
     val postUri:Uri
){
}