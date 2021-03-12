package com.bridgelabz.photomedia.ui.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.ui.homePage.view.HomeDashboardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileFragment : Fragment() {

    private var profileBottomNavigationBar: BottomNavigationView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViewContents(view)
        setInitialViewListeners()
        return view
    }

    private fun initViewContents(view: View?) {
        profileBottomNavigationBar = view?.findViewById(R.id.profileBottomNavView)
    }

    private fun setInitialViewListeners() {
        setbottomNavigationBarListeners()
    }

    private fun setbottomNavigationBarListeners() {
        profileBottomNavigationBar?.setOnNavigationItemSelectedListener {
            when(it.itemId){

                R.id.nav_home ->{
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, HomeDashboardFragment())
                        .commit()
                    Toast.makeText(context, "Navigating Home", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

}