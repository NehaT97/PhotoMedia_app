package com.bridgelabz.photomedia.ui.editProfilePage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.ui.editProfilePage.viewModel.EditProfileViewModel
import com.bridgelabz.photomedia.ui.profilePage.view.ProfileFragment

class EditProfileFragment:Fragment() {

    private var firstNameEditText:EditText? = null
    private var lastNameEditText:EditText? = null
    private var userNameEditText:EditText? = null
    private var bioEditText:EditText? = null
    private var saveProfileDetails:ImageView? = null
    private var closeEditProfilePage:ImageView? = null
    private var progressBarEditProfilePage:ProgressBar? = null
    private var profileUpdating:TextView? = null

    private var editProfileViewModel : EditProfileViewModel? = null

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        initViewContents(view)
        setInitialViewListeners()
        return view
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        editProfileViewModel?.profileDetailsStoreStatus?.observe(viewLifecycleOwner){
          if (it == null)  return@observe

            when(it){
                true -> {
                    progressBarEditProfilePage?.visibility = View.GONE
                    profileUpdating?.visibility = View.GONE
                    Toast.makeText(context,"Profile Saved",Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_nav_host_fragment,ProfileFragment()).commit()
                    return@observe
                }

                false ->{
                    Toast.makeText(context,"Profile Saved",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initViewContents(view: View?) {
        firstNameEditText = view?.findViewById(R.id.editUserFirstName)
        lastNameEditText = view?.findViewById(R.id.editUserLastName)
        bioEditText = view?.findViewById(R.id.editBio)
        closeEditProfilePage = view?.findViewById(R.id.editProfile_CloseImage)
        saveProfileDetails = view?.findViewById(R.id.editProfile_SaveImage)
        progressBarEditProfilePage = view?.findViewById(R.id.progressBar_EditProfile)
        profileUpdating = view?.findViewById(R.id.profile_updating)

    }

    private fun setInitialViewListeners(){
        closeEditProfilePageListener()
        saveProfileDetailsListener()

    }

    private fun saveProfileDetailsListener() {
        saveProfileDetails?.setOnClickListener {
            val firstNameEditTextValue = firstNameEditText?.text.toString()
            val lastNameEditTextValue = lastNameEditText?.text.toString()
            val bioValue = bioEditText?.text.toString()
            progressBarEditProfilePage?.visibility = View.VISIBLE
            profileUpdating?.visibility = View.VISIBLE
            editProfileViewModel?.saveUserProfileDetails(firstNameEditTextValue,lastNameEditTextValue,bioValue)
        }
    }

    private fun closeEditProfilePageListener() {
        closeEditProfilePage?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_nav_host_fragment,ProfileFragment()).commit()
        }
    }

}