package com.tahadroid.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tahadroid.messenger.glide.GlideApp
import com.tahadroid.messenger.model.User
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.util.*


class ProfileActivity : AppCompatActivity() {
    companion object {
        val RES_CODE_OF_IMAGE = 1
    }

    private lateinit var username: String
    private val firebaseStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val storge: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val currentUserDonRef: DocumentReference
        get() = firebaseStore.document("users/${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
    private val currentUserStorgeRef: StorageReference
        get() = storge.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.title = "Me"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        signout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@ProfileActivity, SignInActivity::class.java))
            finish()
        }
        getUserInfo {
            username = it.name
            profile_name_text_view.text = it.name
            if (it.profileImage != null) {
                GlideApp.with(this@ProfileActivity)
                    .load(storge.getReference(it.profileImage))
                    .placeholder(R.drawable.taha)
                    .into(circleImageView_profile)
            }
        }

        circleImageView_profile.setOnClickListener {
            val imageIntent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(
                Intent.createChooser(imageIntent, "select Image "),
                RES_CODE_OF_IMAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RES_CODE_OF_IMAGE && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            progressBar_profile.visibility = View.VISIBLE
            circleImageView_profile.setImageURI(data.data)
            val bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream)
            val byteImage = stream.toByteArray()
            uploadProfileImage(byteImage) { path ->
                val userFeildMap = mutableMapOf<String, Any>()
                userFeildMap["name"] = username
                userFeildMap["profileImage"] = path
                currentUserDonRef.update(userFeildMap)
            }
        }
    }

    private fun uploadProfileImage(byteImage: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        val ref = currentUserStorgeRef.child("ProfilePic/${UUID.nameUUIDFromBytes(byteImage)} ")
        ref.putBytes(byteImage).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(ref.path)
                progressBar_profile.visibility = View.GONE
            } else {
                Toast.makeText(
                    this,
                    "Error : ${it.exception?.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }

    private fun getUserInfo(onComplete: (User) -> Unit) {
        currentUserDonRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }
}
