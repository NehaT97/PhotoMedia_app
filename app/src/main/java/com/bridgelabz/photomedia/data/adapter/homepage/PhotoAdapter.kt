package com.bridgelabz.photomedia.data.adapter.homepage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post

class PhotoAdapter(private var postList: List<Post>, private var currentLoginUserId:String) : RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val displayView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_post_item, parent, false)
        return PhotoViewHolder(displayView, parent.context)
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