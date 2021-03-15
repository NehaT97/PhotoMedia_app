package com.bridgelabz.photomedia.ui.searchPage.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.data.model.Post
import com.bridgelabz.photomedia.data.model.User


class SearchUserFragment : Fragment() {

    private var users: List<User> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user_page, container, false)

        return view
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}