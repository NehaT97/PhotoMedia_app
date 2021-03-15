package com.bridgelabz.photomedia.ui.profile.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.adapter.homepage.PhotoAdapter
import com.bridgelabz.photomedia.data.adapter.profilepage.UserPhotoAdapter
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.User
import com.bridgelabz.photomedia.ui.addPost.viewmodel.PostViewModel
import com.bridgelabz.photomedia.ui.homePage.view.HomeDashboardFragment
import com.bridgelabz.photomedia.ui.login.viewmodel.LoginViewModel
import com.bridgelabz.photomedia.ui.profile.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private var profileViewModel: ProfileViewModel? = null
    private var loginViewModel: LoginViewModel? = null
    private var postViewModel: PostViewModel? = null
    private var profileBottomNavigationBar: BottomNavigationView? = null
    private var profileImage: ImageView? = null
    private var profileImageUri: Uri? = null
    private var imageFileName: String? = null
    private var loggedAuthUser: FirebaseUser? = null
    private var loggedUser: User? = null
    private val OPEN_GALARY = 1

    private var userNameTextView: TextView? = null
    private var totalPostTextView: TextView? = null
    private var totalFollowersTextView: TextView? = null
    private var totalFollowingTextView: TextView? = null
    private var fullNameTextView: TextView? = null
    private var bioTextView: TextView? = null
    private var photoRecyclerView :RecyclerView? = null
    private var photoAdapter :UserPhotoAdapter? = null

    private var posts: List<Post> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViewContents(view)
        setInitialViewListeners()
        return view
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoginViewModelContents()
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel?.isImageUploaded?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            if (it) {
                Toast.makeText(context, "Profile Image Uploaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Profile Image Failed", Toast.LENGTH_SHORT).show()
            }
        }
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel?.fetchAllPostByUserId(listOf(loggedAuthUser?.uid.orEmpty()))
        postViewModel?.userPosts?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            posts = it
            totalPostTextView?.text = it.size.toString()
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        Log.i("Posts", "$posts")
        photoAdapter = UserPhotoAdapter(posts)
        photoRecyclerView?.adapter = photoAdapter
        photoRecyclerView?.layoutManager = GridLayoutManager(context, 3)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                OPEN_GALARY -> if (resultCode == Activity.RESULT_OK && data != null) {
                    profileImageUri = data.data
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    imageFileName =
                        "JPEG_" + timeStamp + "." + profileImageUri?.let { getFileExt(it) }
                    Log.d("tag", "onActivityResult: Gallery Image Uri:  $imageFileName")
                    profileImage?.let { Glide.with(this).load(profileImageUri).into(it) }

                    val userId = loggedAuthUser?.uid
                    if (userId.isNullOrEmpty() || imageFileName.isNullOrEmpty() || profileImageUri == null) {
                        Toast.makeText(context, "Profile Image Failed", Toast.LENGTH_SHORT).show()
                    } else {
                        profileViewModel?.uploadProfileImage(
                            userId, imageFileName.orEmpty(),
                            profileImageUri!!
                        )
                    }
                }
            }
        }
    }


    private fun initLoginViewModelContents() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loggedAuthUser = loginViewModel?.currentAuthUser()
        Log.i("Current Logged User", "$loggedAuthUser")
        loginViewModel?.fetchUserByUserId(loggedAuthUser?.uid.orEmpty())
        loginViewModel?.loggedUser?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                loggedUser = it
                updateViewByUserDetails(loggedUser!!)
            }
        }
    }

    private fun updateViewByUserDetails(user: User) {
        userNameTextView?.text = user.userName
        if (!user.profileImageUrl.isNullOrEmpty()) {
            profileImage?.let { Glide.with(this).load(user.profileImageUrl).into(it) }
        }
        totalFollowingTextView?.text = user.following.size.toString()
        totalFollowersTextView?.text = user.followers.size.toString()
        fullNameTextView?.text = "${user.firstName} ${user.lastName}"
        bioTextView?.text = user.bio
    }

    private fun getFileExt(contentUri: Any): Any? {
        val contentResolver = activity?.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(contentUri as Uri))

    }

    private fun initViewContents(view: View?) {
        profileBottomNavigationBar = view?.findViewById(R.id.profileBottomNavView)
        profileImage = view?.findViewById(R.id.profile_image_profile_fragment)
        userNameTextView = view?.findViewById(R.id.profile_Fragment_Username)
        totalPostTextView = view?.findViewById(R.id.total_post)
        totalFollowersTextView = view?.findViewById(R.id.total_followes)
        totalFollowingTextView = view?.findViewById(R.id.total_followings)
        fullNameTextView = view?.findViewById(R.id.full_name_profile_fragment)
        bioTextView = view?.findViewById(R.id.bio_profile_fragment)

        photoRecyclerView = view?.findViewById(R.id.userPostRecyclerView)
    }

    private fun setInitialViewListeners() {
        setbottomNavigationBarListeners()
        setProfileImageListeners()
    }

    private fun setProfileImageListeners() {
        profileImage?.setOnClickListener {
            val selectProfileImage =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(selectProfileImage, OPEN_GALARY)
        }
    }

    private fun setbottomNavigationBarListeners() {
        profileBottomNavigationBar?.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
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