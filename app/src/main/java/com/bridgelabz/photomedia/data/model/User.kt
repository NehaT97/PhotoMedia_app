package com.bridgelabz.photomedia.data.model

import android.net.Uri
import java.net.URI

class User(
    val userName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    var password: String = "",
    var profileImageUrl: String = "",
    var posts: List<String> = ArrayList(),
    var followers: List<String> = ArrayList(),
    var following: List<String> = ArrayList(),
    var userId: String= "",
    var bio: String= ""
    ){

}