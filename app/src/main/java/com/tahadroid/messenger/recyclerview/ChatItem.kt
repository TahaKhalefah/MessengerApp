package com.tahadroid.messenger.recyclerview

import android.content.Context
import com.google.firebase.storage.FirebaseStorage
import com.tahadroid.messenger.R
import com.tahadroid.messenger.glide.GlideApp
import com.tahadroid.messenger.model.User
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.recycler_view_item.*

class ChatItem(
    val uid:String,
    val user: User,
    val context: Context
) : Item() {

    private val ins: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.item_name_tv.text = user.name
        if (user.profileImage.isNotEmpty()) {
            GlideApp.with(context)
                .load(ins.getReference(user.profileImage))
                .into(viewHolder.item_circle_iv)
        } else {
            viewHolder.item_circle_iv.setImageResource(R.drawable.taha)
        }
    }

    override fun getLayout(): Int {
        return R.layout.recycler_view_item
    }
}