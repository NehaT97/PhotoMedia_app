package com.bridgelabz.photomedia.data.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val postUserProfileImage: ImageView = itemView.findViewById(R.id.postUserProfileImage)
    private val postImage: ImageView = itemView.findViewById(R.id.postImage)
    private val postMenu: Button = itemView.findViewById(R.id.postMenu)
    private val postLikeButton: Button = itemView.findViewById(R.id.likeButton)
    private val postCommentButton: Button = itemView.findViewById(R.id.commentButton)
    private val postUserNameTextView: TextView = itemView.findViewById(R.id.postUserName)

    fun bind(post: Any) {

    }

}