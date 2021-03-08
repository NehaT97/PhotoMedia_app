package com.bridgelabz.photomedia.data.model

class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val username : String,
    val email: String,
    val emailVerified: Boolean,
    val password: String,
    val role: String,
    val service: String,
    val createdDate: String,
    val modifiedDate:String
){

}