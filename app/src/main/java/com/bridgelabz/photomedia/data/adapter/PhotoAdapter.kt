package com.bridgelabz.photomedia.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R

class PhotoAdapter(private var postList: List<Any>) : RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val displayView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_post_item, parent, false)
        return PhotoViewHolder(displayView)
    }

    override fun getItemCount(): Int {
        Log.i("Items Count:","${postList.size}")
        return postList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val post = postList[position]
    }
}