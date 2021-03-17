package com.bridgelabz.photomedia.ui.searchPage.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.adapter.searchPage.SearchUserAdapter
import com.bridgelabz.photomedia.data.model.User
import com.bridgelabz.photomedia.ui.addPostPage.view.PostFragment
import com.bridgelabz.photomedia.ui.homePage.view.HomeDashboardFragment
import com.bridgelabz.photomedia.ui.profilePage.view.ProfileFragment
import com.bridgelabz.photomedia.ui.searchPage.viewModel.SearchUserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.stream.Collectors


class SearchUserFragment : Fragment(), SearchUserAdapter.OnRecylerViewItemEventListener {

    private var users: List<User> = ArrayList()

    private var searchUserAdapter : SearchUserAdapter? = null
    private var searchViewModel: SearchUserViewModel? = null
    private var searchUserRecyclerView: RecyclerView? = null
    private var bottomNavigationBarSearchFragment: BottomNavigationView? = null

    private var currentLoggedInUserId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user_page, container, false)
        initRecyclerView(view)
        setInitialViewLiteners()
        return view
    }



    private fun initRecyclerView(view: View?) {
        searchUserRecyclerView = view?.findViewById(R.id.searchRecyclerView)
        bottomNavigationBarSearchFragment = view?.findViewById(R.id.SearchBottomNavView)
        searchViewModel = ViewModelProvider(this).get(SearchUserViewModel::class.java)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshUsers()
    }

    private fun refreshUsers() {
        searchViewModel?.fetchAllUsers()
        searchViewModel?.users?.observe(viewLifecycleOwner) { it ->
            if (it == null) {
                return@observe
            }
            if (it.isEmpty()) {
                Toast.makeText(context, "Users not found", Toast.LENGTH_SHORT).show()
            } else {
                users = it
                val sharedPreferences = context?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                val uid = sharedPreferences?.getString("loggedUserUID", "")
                Log.i("Founded users", "$uid")
                currentLoggedInUserId = uid
                val filteredUsers = users.stream().filter{ user ->
                    user.userId != uid
                }.collect(Collectors.toList())
                users = filteredUsers
                searchUserAdapter = SearchUserAdapter(filteredUsers, uid.orEmpty())
                searchUserAdapter?.setOnItemEventListener(this)
                searchUserRecyclerView?.adapter = searchUserAdapter
                searchUserRecyclerView?.layoutManager = LinearLayoutManager(context)
            }
        }

        searchViewModel?.isFollowSuccessFul?.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Followed User", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Followed User Failed", Toast.LENGTH_SHORT).show()
            }
        }

        searchViewModel?.isUnfollowSuccessFul?.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Unfollowed User", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Unollowed User Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFollowButtonClicked(view: View, position: Int) {
        if (users[position].followers.contains(currentLoggedInUserId)) {
            searchViewModel?.unfollowUser(currentLoggedInUserId.orEmpty(), users[position].userId)
        } else {
            Log.i("Follow UserId","${users[position]}")
            searchViewModel?.followUser(currentLoggedInUserId.orEmpty(), users[position].userId)
        }
        searchUserAdapter?.notifyDataSetChanged()
    }

    private fun setInitialViewLiteners() {
        setbottomNavigationBarListeners()
    }

    private fun setbottomNavigationBarListeners() {
        bottomNavigationBarSearchFragment?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment,HomeDashboardFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.search ->{
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment,SearchUserFragment())
                        .commit()
                    Toast.makeText(context, "Search Users", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_addPost -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, PostFragment())
                        .commit()
                    Toast.makeText(context, "Add Post", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.nav_profile -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, ProfileFragment())
                        .commit()
                    Toast.makeText(context, "Navigate to profile page", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }
    }
