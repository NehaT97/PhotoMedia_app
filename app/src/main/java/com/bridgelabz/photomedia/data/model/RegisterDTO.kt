package com.bridgelabz.photomedia.data.model

class RegisterDTO(
    val firstName: String,
    val lastName: String,
    val username : String,
    val email: String,
    val password: String,
    val emailVerified: Boolean = true,
    val role: String = "user",
    val service: String = "Advance"
) {
}