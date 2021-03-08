package com.bridgelabz.photomedia.data.externalApis

import android.util.Log
import com.bridgelabz.photomedia.data.model.LoginDTO
import com.bridgelabz.photomedia.data.model.RegisterDTO
import com.bridgelabz.photomedia.data.model.User
import com.google.gson.Gson
import org.json.JSONObject

class UserApiService {

    private val httpRequestService = HttpRequestService()
    private val gson = Gson()

    fun login(username: String, password: String, listener: (User?) -> Unit): User? {
        val loginDTO = LoginDTO(username, password)
        val userData = gson.toJson(loginDTO).toString()
        Log.i("JSONString", "$userData")
        val response = httpRequestService.post(
            "http://fundoonotes.incubation.bridgelabz.com/api/user/login",
            JSONObject(userData)
        )
        return if (response.isNotEmpty()) {
            val user = gson.fromJson<User>(response, User::class.java)
            listener(user)
            return user
        } else {
            listener(null)
            null
        }
    }

    fun register(registerDTO: RegisterDTO, listener: (User?) -> Unit): User? {
        val userData = gson.toJson(registerDTO).toString()
        Log.i("JSONString", "$userData")
        val response = httpRequestService.post(
            "http://fundoonotes.incubation.bridgelabz.com/api/user/userSignUp",
            JSONObject(userData)
        )
        return if (response.isNotEmpty()) {
            val user = gson.fromJson<User>(response, User::class.java)
            listener(user)
            return user
        } else {
            listener(null)
            null
        }
    }
}