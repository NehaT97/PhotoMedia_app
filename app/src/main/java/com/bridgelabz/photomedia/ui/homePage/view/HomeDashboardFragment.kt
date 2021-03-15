package com.bridgelabz.photomedia.ui.homePage.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.green
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.adapter.homepage.PhotoAdapter
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.User
import com.bridgelabz.photomedia.ui.addPostPage.view.PostFragment
import com.bridgelabz.photomedia.ui.addPostPage.viewmodel.PostViewModel
import com.bridgelabz.photomedia.ui.loginPage.viewmodel.LoginViewModel
import com.bridgelabz.photomedia.ui.profilePage.view.ProfileFragment
import com.bridgelabz.photomedia.ui.searchPage.view.SearchUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser

class HomeDashboardFragment : Fragment() {

    private var photoAdapter: PhotoAdapter? = null
    private var photoRecyclerView: RecyclerView? = null
    private var bottomNavigationBar: BottomNavigationView? = null

    private var postViewModel: PostViewModel? = null
    private var loginViewModel: LoginViewModel? = null
    private var loggedAuthUser: FirebaseUser? = null
    private var loggedUser: User? = null

    private var uid: String? = null
    private var postList: List<Post>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_dashboard, container, false)
        initViewModels()
        initViewContents(view)
        //initPostRecyclerView()
        setInitialViewLiteners()
        return view
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel?.userPosts?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            postList = it
            Log.i("Fetched posts", "$it")
            initPostRecyclerView()
        }
    }

    private fun setInitialViewLiteners() {
        setbottomNavigationBarListeners()
    }

    private fun initViewModels() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loggedAuthUser = loginViewModel?.currentAuthUser()
        Log.i("Current Logged User", "$loggedAuthUser")
        loginViewModel?.fetchUserByUserId(loggedAuthUser?.uid.orEmpty())

        loginViewModel?.loggedUser?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                loggedUser = it
                postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
                val userIdList: ArrayList<String> = ArrayList()
                Log.i("Loged User", "$loggedUser")
                if (loggedUser != null) {
                    loggedUser?.following?.let { userIdList.addAll(it) }
                }
                loggedAuthUser?.uid?.let { userIdList.add(it) }
                postViewModel?.fetchAllPostByUserId(userIdList)

            }
        }
    }

    private fun setbottomNavigationBarListeners() {
        bottomNavigationBar?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.search_user ->{
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment,SearchUserFragment())
                        .addToBackStack("")
                        .commit()
                    Toast.makeText(context, "Search Users", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_addPost -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, PostFragment())
                        .addToBackStack("")
                        .commit()
                    Toast.makeText(context, "Add Post", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.nav_profile -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, ProfileFragment()).addToBackStack("")
                        .commit()
                    Toast.makeText(context, "Navigate to profile page", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }

    private fun initViewContents(view: View) {
        photoRecyclerView = view.findViewById(R.id.postRecyclerView)
        bottomNavigationBar = view.findViewById(R.id.bottomNavView)

    }

    private fun initPostRecyclerView() {
        Log.i("Posts", "$postList")
        photoAdapter = PhotoAdapter(postList.orEmpty(), loggedAuthUser?.uid.orEmpty())
        photoRecyclerView?.adapter = photoAdapter
        photoRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

}