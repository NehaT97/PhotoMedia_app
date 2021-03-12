package com.bridgelabz.photomedia.data.model

import android.net.Uri

class Post
    (val userId:String = "",
     val postId:String = "",
     val imageFileName:String = "",
     val postUri:Uri,
     val isLiked:Boolean = false,
     val isCommented:Boolean = false

){
}