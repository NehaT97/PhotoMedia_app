package com.bridgelabz.photomedia.data.adapter.searchPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.User

class SearchUserAdapter(private val searchUserList:List<User>, val loggedUserUid: String):RecyclerView.Adapter<SearchUserViewHolder>() {

    private var eventListener: OnRecylerViewItemEventListener? = null

    interface OnRecylerViewItemEventListener {

        fun onFollowButtonClicked(view:View, position: Int)

    }

    fun setOnItemEventListener(listener: OnRecylerViewItemEventListener) {
        eventListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val displayView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_search_user_item_cardview, parent, false)
        val viewHolder = SearchUserViewHolder(displayView, loggedUserUid)
        initRecyclerViewListeners(displayView, viewHolder)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return searchUserList.size
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val searchUser = searchUserList[position]
        holder.bind(searchUser)
    }

    private fun initRecyclerViewListeners(view: View, viewHolder: RecyclerView.ViewHolder) {
        val followButton = view.findViewById<Button>(R.id.Follow)
        followButton.setOnClickListener {
            eventListener?.onFollowButtonClicked(it, viewHolder.adapterPosition)
        }
    }
}