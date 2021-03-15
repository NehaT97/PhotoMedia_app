package com.bridgelabz.photomedia.data.adapter.searchPage

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.User
import com.bumptech.glide.Glide

class SearchUserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    private val searchUserProfileImage:ImageView = itemView.findViewById(R.id.searchUserProfileImage)
    private val searchUserUserName:TextView = itemView.findViewById(R.id.searchUserUserName)
    private val followButton:Button = itemView.findViewById(R.id.Follow)

    fun bind(searchUser: User) {
        searchUserUserName.text = searchUser.userName

        if (!searchUser.profileImageUrl.isNullOrEmpty()){
            Glide.with(itemView.context).load(searchUser.profileImageUrl).into(searchUserProfileImage)
        }

    }
}