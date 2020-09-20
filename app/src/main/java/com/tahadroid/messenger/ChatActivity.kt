package com.tahadroid.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tahadroid.messenger.glide.GlideApp
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private var other_uid: String =""
    private val ins: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val insStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val username = intent.getStringExtra("name")
        val imageProfile = intent.getStringExtra("image")
        other_uid = intent.getStringExtra("uid")
        userName.text = username
        if (imageProfile.isNotEmpty()) {
            GlideApp.with(this).load(ins.getReference(imageProfile))
                .into(personalImage)
        } else {
            personalImage.setImageResource(R.drawable.taha)
        }
        createChatChannal()
        imageViewBack.setOnClickListener {
            finish()
        }
    }
    private fun createChatChannal(){
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val newChatChannal = insStore.collection("users").document()

        insStore.collection("users")
            .document(other_uid)
            .collection("chatChannal")
            .document(currentUserId)
            .set(mapOf("channalId" to newChatChannal.id))

        insStore.collection("users")
            .document(currentUserId)
            .collection("chatChannal")
            .document(other_uid)
            .set(mapOf("channalId" to newChatChannal.id))
    }
}