package com.bridgelabz.photomedia.data.adapter.homepage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post

class PhotoAdapter(private var postList: List<Post>, private var currentLoginUserId:String) : RecyclerView.Adapter<PhotoViewHolder>() {

    var listener: OnClickedListener? = null

    fun setOnClickListener(onClickedListener : OnClickedListener) {
        listener = onClickedListener
    }
    interface OnClickedListener {
        fun onLikeButtonClickedListener(view:View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val displayView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_homepost_item_cardview, parent, false)
        val likedButton = displayView.findViewById<Button>(R.id.likeButton)
        val viewHolder = PhotoViewHolder(displayView, parent.context)
        likedButton.setOnClickListener {
            listener?.onLikeButtonClickedListener(displayView, viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.i("Items Count:","${postList.size}")
        return postList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        var post = postList[position]
        holder.bind(post, currentLoginUserId)
    }
}