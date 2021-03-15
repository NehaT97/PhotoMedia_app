package com.bridgelabz.photomedia.data.adapter.profilepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post

class UserPhotoAdapter(var userPostList:List<Post>): RecyclerView.Adapter<UserPhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPhotoViewHolder {
        val displayView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_user_post_card, parent, false)
        return UserPhotoViewHolder(displayView)
    }

    override fun getItemCount(): Int {
        return userPostList.size
    }

    override fun onBindViewHolder(holder: UserPhotoViewHolder, position: Int) {
        val post = userPostList[position]
        holder.bind(post)
    }

}