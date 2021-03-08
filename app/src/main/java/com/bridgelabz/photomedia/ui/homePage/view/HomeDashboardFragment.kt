package com.bridgelabz.photomedia.ui.homePage.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.adapter.PhotoAdapter
import java.util.*

class HomeDashboardFragment : Fragment() {

    private var photoAdapter: PhotoAdapter? = null
    private var photoRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home_dashboard, container, false)
        initViewContents(view)
        initPostRecyclerView()
        return view
    }

    private fun initViewContents(view:View) {
        photoRecyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun initPostRecyclerView() {
        photoAdapter = PhotoAdapter(listOf("Akshay", "Neha", "Shubham", "Thakur", "Neha1","Pratibha"))
        photoRecyclerView?.adapter = photoAdapter
        photoRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

}