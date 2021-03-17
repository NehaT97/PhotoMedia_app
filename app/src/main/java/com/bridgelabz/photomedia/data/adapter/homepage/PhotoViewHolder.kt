package com.bridgelabz.photomedia.data.adapter.homepage

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post
import com.bumptech.glide.Glide

class PhotoViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

    private val postUserProfileImage: ImageView = itemView.findViewById(R.id.postUserProfileImage)
    private val postImage: ImageView = itemView.findViewById(R.id.postImage)
    private val postLikeButton: Button = itemView.findViewById(R.id.likeButton)
    private val postUserNameTextView: TextView = itemView.findViewById(R.id.postUserName)

    fun bind(post: Post, currentLoginUserId: String) {
        postUserNameTextView.text = post.userName
        if (post.likedBy.contains(currentLoginUserId)) {
            postLikeButton.setBackgroundResource(R.drawable.like_heart_vector_red)
        }
        if(!post.currentPostImageUri.isNullOrEmpty()) {
            Glide.with(itemView.context).load(post.currentPostImageUri).into(postImage)
        }
        if (!post.userProfileImageUri.isNullOrEmpty()) {
            Glide.with(itemView.context).load(post.userProfileImageUri).into(postUserProfileImage)
        }
    }

}