package com.tahadroid.messenger

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tahadroid.messenger.fragments.ChatFragment
import com.tahadroid.messenger.fragments.DiscoverFragment
import com.tahadroid.messenger.fragments.PeopleFragment
import com.tahadroid.messenger.glide.GlideApp
import com.tahadroid.messenger.model.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val storeInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val storgeInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val chatFragment = ChatFragment()
    private val peopleFragment = PeopleFragment()
    private val moreFragment = DiscoverFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //status bar
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = Color.WHITE
        }
        //Action bar
        setSupportActionBar(toolbar_main)
        supportActionBar?.title = ""
        //bottom navigation
        bottomNavigationView_main.setOnNavigationItemSelectedListener(this@MainActivity)
        //default seen fragment
        setFragment(chatFragment)

        storeInstance.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val user =it.toObject(User::class.java)
                if(user!!.profileImage.isNotEmpty()){
                    GlideApp.with(this@MainActivity)
                        .load(storgeInstance.getReference(user.profileImage))
                        .into(circle_image_view_profile)
                }else{
                    circle_image_view_profile.setImageResource(R.drawable.taha)
                }
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_chat -> {
                setFragment(chatFragment)
                return true
            }

            R.id.navigation_people -> {
                setFragment(peopleFragment)
                return true
            }

            R.id.navigation_more -> {
                setFragment(moreFragment)
                return true
            }

            else ->
                return false
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fr = supportFragmentManager.beginTransaction()
            .replace(R.id.coordinatorLayout_main, fragment)
            .addToBackStack("")
            .commit()
    }
}
