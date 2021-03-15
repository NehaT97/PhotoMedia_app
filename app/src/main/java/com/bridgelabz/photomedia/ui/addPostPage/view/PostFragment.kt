package com.bridgelabz.photomedia.ui.addPostPage.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.User
import com.bridgelabz.photomedia.ui.addPostPage.viewmodel.PostViewModel
import com.bridgelabz.photomedia.ui.homePage.view.HomeDashboardFragment
import com.bridgelabz.photomedia.ui.loginPage.viewmodel.LoginViewModel
import com.bridgelabz.photomedia.ui.profilePage.view.ProfileFragment
import com.bridgelabz.photomedia.ui.searchPage.view.SearchUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PostFragment : Fragment() {

    private var postViewModel: PostViewModel? = null
    private var loginViewModel: LoginViewModel? = null
    lateinit var currentPhotoPath: String
    private var addPostBottomNavigationBar: BottomNavigationView? = null
    private var selectImage: ImageView? = null
    private var captureImage: ImageView? = null
    private var closeImage: ImageView? = null
    private var saveImage: ImageView? = null
    private var uploadImageProgressBar:ProgressBar? = null
    private var imageUploading:TextView? = null
    private var selectImageFromGalary: ImageView? = null
    private val REQUEST_IMAGE_CAPTURE = 0
    private val OPEN_GALARY = 1
    private var imageFileName: String? = null
    private var selectedImageUri: Uri? = null
    private var loggedAuthUser: FirebaseUser? = null
    private var loggedUser: User? = null
    private var uploadedPostImageUri: Uri? = null
    private var progress:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        initViewContents(view)
        setInitialViewListeners()
        return view
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPostViewModelContents()
        initLoginViewModelContents()
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
            }
        }
    }

    private fun initPostViewModelContents() {
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel?.uploadedPostImageUri?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                Log.i("Post URL ", "${it.toString()}")
                uploadedPostImageUri = it
            }
        }

        postViewModel?.postStatus?.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            when (it) {
                true -> {
                    //Toast.makeText(context, "Post Uploaded", Toast.LENGTH_SHORT).show()
                    imageUploading?.visibility = View.VISIBLE
                    uploadImageProgressBar?.visibility = View.GONE
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment,HomeDashboardFragment()).commit()
                }
                false -> {
                    Toast.makeText(context, "Post Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initViewContents(view: View?) {
        addPostBottomNavigationBar = view?.findViewById(R.id.addPostBottomNavView)
        selectImage = view?.findViewById(R.id.selectImage)
        captureImage = view?.findViewById(R.id.captureImage)
        selectImageFromGalary = view?.findViewById(R.id.selectImageFromGalary)
        closeImage = view?.findViewById(R.id.closeImage)
        saveImage = view?.findViewById(R.id.saveImage)
        uploadImageProgressBar = view?.findViewById(R.id.progressBar_addPost)
        imageUploading = view?.findViewById(R.id.uploading)

    }

    private fun setInitialViewListeners() {
        setbottomNavigationBarListeners()
        setImageListeners()
    }

    private fun setImageListeners() {
        captureImage?.setOnClickListener {
            dispatchTakePictureIntent()
        }

        selectImageFromGalary?.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, OPEN_GALARY)
        }

        saveImage?.setOnClickListener {
            val userID = loggedAuthUser?.uid
            if (userID == null) {
                Toast.makeText(context, "Unable to upload post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("Logged User from DB", "$loggedUser")
                if (loggedUser != null) {
                    Log.i("Logged User", "$loggedUser")
                    val userName = loggedUser?.userName.orEmpty()
                    val userProfileImageUrl = loggedUser?.profileImageUrl
                    val postId = UUID.randomUUID().toString()
                    uploadImageProgressBar?.visibility = View.VISIBLE
                    setProgressValue(progress)
                    val postToUpload = Post(userID.orEmpty(), postId, userName, userProfileImageUrl.toString(), "")
                    postViewModel?.uploadImageFirebaseStorage(postToUpload, imageFileName!!, selectedImageUri!!)
                }
            }
        }

        closeImage?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_nav_host_fragment,PostFragment()).commit()
        }
    }

    private fun setProgressValue(progress: Int) {
        val thread = Thread(Runnable {
            try {
                uploadImageProgressBar?.progress = progress
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            setProgressValue(progress + 10)
        })
        thread.start()
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> if (resultCode == Activity.RESULT_OK && data != null) {

                    val imageBitmap = data.extras?.get("data") as Bitmap
                    selectImage?.setImageBitmap(imageBitmap)
                    captureImage?.visibility = GONE
                    selectImageFromGalary?.visibility = GONE
                    Log.i("data", "${data.extras?.get("data")}")
                    Log.e("Data[data]", "${data.data}")

                }

                OPEN_GALARY -> if (resultCode == Activity.RESULT_OK && data != null) {
                    selectedImageUri = data.data
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    imageFileName =
                        "JPEG_" + timeStamp + "." + selectedImageUri?.let { getFileExt(it) }
                    Log.d("tag", "onActivityResult: Gallery Image Uri:  $imageFileName")
                    selectImage?.setImageURI(selectedImageUri)
                    selectImageFromGalary?.visibility = GONE
                    captureImage?.visibility = GONE
                }
            }
        }
    }

    private fun getFileExt(contentUri: Any): Any? {
        val contentResolver = activity?.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(contentUri as Uri))
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
            Log.i("Current Photo Path", "$currentPhotoPath")
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            e.printStackTrace()
        }

    }


    private fun setbottomNavigationBarListeners() {
        addPostBottomNavigationBar?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, HomeDashboardFragment())
                        .commit()
                    Toast.makeText(context, "Navigating Home", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.search_user ->{
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment, SearchUserFragment())
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
                        .replace(R.id.activity_main_nav_host_fragment, ProfileFragment())
                        .addToBackStack("")
                        .commit()
                    Toast.makeText(context, "Navigate to profile page", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }


}