package com.tahadroid.messenger.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tahadroid.messenger.ProfileActivity
import com.tahadroid.messenger.R

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val title = activity?.findViewById<TextView>(R.id.title_text_view)
        title?.text = "Chats"
        val circleImageVieww = activity?.findViewById<ImageView>(R.id.circle_image_view_profile)
        circleImageVieww!!.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    ProfileActivity::class.java
                )
            )
        }
        return view
    }

}
