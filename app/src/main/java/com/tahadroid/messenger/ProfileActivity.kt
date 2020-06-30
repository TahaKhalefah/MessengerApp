package com.tahadroid.messenger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    companion object {
        val RES_CODE_OF_IMAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
//        setSupportActionBar(profile_toolbar)
//        supportActionBar?.title = "Me"
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        circleImageView_profile.setOnClickListener {
//            val imageIntent = Intent().apply {
//                type = "image/*"
//                action = Intent.ACTION_GET_CONTENT
//                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
//            }
//            startActivityForResult(
//                Intent.createChooser(imageIntent, "select Image "),
//                RES_CODE_OF_IMAGE
//            )
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode== RES_CODE_OF_IMAGE && resultCode== Activity.RESULT_OK
//            && data!=null&&data.data!=null){
//            circleImageView_profile.setImageURI(data.data)
//        }
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                return true
//            }
//        }
//        return false
//    }
}
