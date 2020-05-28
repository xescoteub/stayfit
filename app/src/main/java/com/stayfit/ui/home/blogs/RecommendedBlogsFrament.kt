package com.stayfit.ui.home.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stayfit.R

class RecommendedBlogsFrament : Fragment() {
    private val TAG = "RecommendedBlogsFrament"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.recommended_blogs_fragment, container, false)

        return root
    }
}