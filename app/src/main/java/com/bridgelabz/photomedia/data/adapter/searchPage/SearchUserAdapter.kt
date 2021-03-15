package com.bridgelabz.photomedia.data.adapter.searchPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.User

class SearchUserAdapter(private val searchUserList:List<User>):RecyclerView.Adapter<SearchUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val displayView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_user_post_card, parent, false)
        return SearchUserViewHolder(displayView)
    }

    override fun getItemCount(): Int {
        return searchUserList.size
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val searchUser = searchUserList[position]
        holder.bind(searchUser)
    }
}