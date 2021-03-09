package com.bridgelabz.photomedia.ui.homePage.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.adapter.PhotoAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeDashboardFragment : Fragment() {

    private var photoAdapter: PhotoAdapter? = null
    private var photoRecyclerView: RecyclerView? = null
    private var bottomNavigationBar: BottomNavigationView? = null

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
        setInitialViewLiteners()
        return view
    }

    private fun setInitialViewLiteners() {
        setbottomNavigationBarListeners()

    }

    private fun setbottomNavigationBarListeners() {
        bottomNavigationBar?.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.nav_home -> {
                        Toast.makeText(context,"Home",Toast.LENGTH_SHORT).show()
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.nav_addPost -> {
                        Toast.makeText(context,"Add Post",Toast.LENGTH_SHORT).show()
                        return@setOnNavigationItemSelectedListener true

                    }
                    R.id.nav_profile -> {
                        Toast.makeText(context,"Navigate to profile page",Toast.LENGTH_SHORT).show()
                        return@setOnNavigationItemSelectedListener true

                    }
                }
            false
        }
    }

    private fun initViewContents(view:View) {
        photoRecyclerView = view.findViewById(R.id.recyclerView)
        bottomNavigationBar = view.findViewById(R.id.bottomNavView)

    }

    private fun initPostRecyclerView() {
        photoAdapter = PhotoAdapter(listOf("Akshay", "Neha", "Shubham", "Thakur", "Neha1","Pratibha"))
        photoRecyclerView?.adapter = photoAdapter
        photoRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

}