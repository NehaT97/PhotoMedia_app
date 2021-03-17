package com.bridgelabz.photomedia.data.adapter.profilepage

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post
import com.bumptech.glide.Glide

class UserPhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val postImageView: ImageView = itemView.findViewById<ImageView>(R.id.userPostImage)

    fun bind(post:Post) {
        if (!post.currentPostImageUri.isNullOrEmpty()) {
            Glide.with(itemView.context).load(post.currentPostImageUri).into(postImageView)
        }
    }

}